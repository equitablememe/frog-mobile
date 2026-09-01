package frog.mobile.android

import android.content.Context
import com.google.adk.kt.annotations.Param
import com.google.adk.kt.annotations.Tool
import frog.core.AuditedToolExecutor
import frog.core.InferenceLocation
import frog.core.ToolRequest
import java.util.UUID

/**
 * The model sees one simple function. Behind it, FROG performs policy,
 * execution, observation, verification, and receipt creation.
 */
class FrogDeviceTools(
    context: Context,
    private val modelId: String,
    private val receipts: ReceiptStore = ReceiptStore(),
) : AutoCloseable {

    private val torch = TorchController(context)
    private val executor = AuditedToolExecutor(FrogPolicy())

    @Tool(
        name = "set_flashlight",
        description = "Turn this Android device's flashlight on or off and report whether the resulting state was verified.",
    )
    fun setFlashlight(
        @Param("true to turn the flashlight on; false to turn it off") enabled: Boolean,
    ): Map<String, String> {
        val expected = if (enabled) "ON" else "OFF"
        val request = ToolRequest(
            invocationId = UUID.randomUUID().toString(),
            requestedIntent = "Set flashlight to $expected",
            toolName = "set_flashlight",
            argumentsCanonical = "{\"enabled\":$enabled}",
            expectedState = expected,
        )

        val receipt = executor.run(
            request = request,
            tool = torch,
            modelId = modelId,
            inferenceLocation = InferenceLocation.ON_DEVICE,
        )
        receipts.append(receipt)

        return buildMap {
            put("receipt_id", receipt.invocationId)
            put("authorization", receipt.authorization.name)
            put("execution_completed", receipt.executionCompleted.toString())
            put("verification", receipt.verification.name)
            receipt.observedState?.let { put("observed_state", it) }
            receipt.error?.let { put("error", it) }
        }
    }

    override fun close() {
        torch.close()
    }
}
