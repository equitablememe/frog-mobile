package frog.mobile.android

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import frog.core.ExecutionResult
import frog.core.Observation
import frog.core.ToolAdapter
import frog.core.ToolRequest
import java.io.Closeable
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Android-specific adapter for the first FROG action.
 *
 * This is deliberately thin: platform code executes and observes; policy and
 * verification stay in the dependency-free core module.
 *
 * Starter assumption: API 28+ so registerTorchCallback can use an Executor.
 */
class TorchController(
    context: Context,
    private val observationTimeoutMs: Long = 1_500,
) : ToolAdapter, Closeable {

    override val name: String = "set_flashlight"

    private val cameraManager = context.getSystemService(CameraManager::class.java)
    private val executor = context.mainExecutor
    private val cameraId: String = findTorchCameraId()

    @Volatile private var latestEnabled: Boolean? = null
    @Volatile private var unavailable: Boolean = false
    @Volatile private var pendingLatch: CountDownLatch? = null

    private val callback = object : CameraManager.TorchCallback() {
        override fun onTorchModeChanged(changedCameraId: String, enabled: Boolean) {
            if (changedCameraId != cameraId) return
            latestEnabled = enabled
            unavailable = false
            pendingLatch?.countDown()
        }

        override fun onTorchModeUnavailable(changedCameraId: String) {
            if (changedCameraId != cameraId) return
            unavailable = true
            pendingLatch?.countDown()
        }
    }

    init {
        cameraManager.registerTorchCallback(executor, callback)
    }

    override fun execute(request: ToolRequest): ExecutionResult {
        val enabled = parseEnabled(request.argumentsCanonical)
            ?: return ExecutionResult(
                attempted = false,
                completed = false,
                error = "Expected canonical arguments containing enabled=true or enabled=false.",
            )

        pendingLatch = CountDownLatch(1)
        return try {
            cameraManager.setTorchMode(cameraId, enabled)
            ExecutionResult(
                attempted = true,
                completed = true,
                detail = "CameraManager.setTorchMode returned normally.",
            )
        } catch (t: Throwable) {
            pendingLatch = null
            ExecutionResult(
                attempted = true,
                completed = false,
                error = "${t::class.simpleName}: ${t.message}",
            )
        }
    }

    override fun observe(request: ToolRequest): Observation {
        val latch = pendingLatch
        if (latch != null) {
            latch.await(observationTimeoutMs, TimeUnit.MILLISECONDS)
            pendingLatch = null
        }

        if (unavailable) {
            return Observation(error = "Torch became unavailable during observation.")
        }

        return when (latestEnabled) {
            true -> Observation(state = "ON")
            false -> Observation(state = "OFF")
            null -> Observation(error = "No torch state callback was observed before timeout.")
        }
    }

    override fun close() {
        cameraManager.unregisterTorchCallback(callback)
    }

    private fun findTorchCameraId(): String =
        cameraManager.cameraIdList.firstOrNull { id ->
            cameraManager.getCameraCharacteristics(id)
                .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
        } ?: error("No camera with an available flash unit was found.")

    private fun parseEnabled(canonical: String): Boolean? {
        val normalized = canonical.lowercase().replace(" ", "")
        return when {
            "\"enabled\":true" in normalized || "enabled=true" in normalized -> true
            "\"enabled\":false" in normalized || "enabled=false" in normalized -> false
            else -> null
        }
    }
}
