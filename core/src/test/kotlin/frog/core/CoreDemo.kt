package frog.core

private class DemoPolicy : ToolPolicy {
    override fun evaluate(request: ToolRequest): PolicyDecision =
        when (request.toolName) {
            "set_flashlight" ->
                PolicyDecision(
                    authorization = AuthorizationDecision.ALLOW,
                    riskClass = RiskClass.REVERSIBLE_LOCAL,
                    reversible = true,
                    rationale = "Flashlight state is local and reversible.",
                )
            "get_battery_level" ->
                PolicyDecision(
                    authorization = AuthorizationDecision.ALLOW,
                    riskClass = RiskClass.READ_ONLY,
                    reversible = true,
                    rationale = "Battery level is read-only device state.",
                )
            else ->
                PolicyDecision(
                    authorization = AuthorizationDecision.DENY,
                    riskClass = RiskClass.EXTERNAL_SIDE_EFFECT,
                    reversible = false,
                    rationale = "Unknown tool is denied by default.",
                )
        }
}

private class FakeFlashlightTool(
    private val sabotage: Boolean = false,
) : ToolAdapter {
    override val name: String = "set_flashlight"
    private var enabled: Boolean = false

    override fun execute(request: ToolRequest): ExecutionResult {
        val requestedOn = request.argumentsCanonical.contains("true")
        if (!sabotage) enabled = requestedOn
        return ExecutionResult(
            attempted = true,
            completed = true,
            detail = "Fake platform call returned normally.",
        )
    }

    override fun observe(request: ToolRequest): Observation =
        Observation(state = if (enabled) "ON" else "OFF")
}

private class ExplodingUnknownTool : ToolAdapter {
    override val name: String = "unknown_tool"

    override fun execute(request: ToolRequest): ExecutionResult =
        error("DENIED tool executed; policy gate failed.")

    override fun observe(request: ToolRequest): Observation =
        error("DENIED tool was observed; policy gate failed.")
}

private class FakeBatteryTool(
    private val batteryPercent: Int = 67,
) : ToolAdapter {
    override val name: String = "get_battery_level"
    private var value: Int? = null

    override fun execute(request: ToolRequest): ExecutionResult {
        value = batteryPercent
        return ExecutionResult(
            attempted = true,
            completed = true,
            detail = "Read fake battery state.",
        )
    }

    override fun observe(request: ToolRequest): Observation =
        Observation(state = value?.toString())
}

fun main() {
    val executor = AuditedToolExecutor(DemoPolicy())
    val flashlightRequest =
        ToolRequest(
            invocationId = "demo-001",
            requestedIntent = "Turn the flashlight on",
            toolName = "set_flashlight",
            argumentsCanonical = "{\"enabled\":true}",
            expectedState = "ON",
        )

    val verified =
        executor.run(
            request = flashlightRequest,
            tool = FakeFlashlightTool(sabotage = false),
            modelId = "demo-model",
            inferenceLocation = InferenceLocation.ON_DEVICE,
        )
    check(verified.authorization == AuthorizationDecision.ALLOW)
    check(verified.executionAttempted)
    check(verified.executionCompleted)
    check(verified.verification == VerificationStatus.VERIFIED)

    val contradicted =
        executor.run(
            request = flashlightRequest.copy(invocationId = "demo-002"),
            tool = FakeFlashlightTool(sabotage = true),
            modelId = "demo-model",
            inferenceLocation = InferenceLocation.ON_DEVICE,
        )
    check(contradicted.authorization == AuthorizationDecision.ALLOW)
    check(contradicted.executionAttempted)
    check(contradicted.executionCompleted)
    check(contradicted.verification == VerificationStatus.CONTRADICTED)

    val denied =
        executor.run(
            request =
                ToolRequest(
                    invocationId = "demo-003",
                    requestedIntent = "Invoke an unknown tool",
                    toolName = "unknown_tool",
                    argumentsCanonical = "{}",
                    expectedState = "DONE",
                ),
            tool = ExplodingUnknownTool(),
            modelId = "demo-model",
            inferenceLocation = InferenceLocation.ON_DEVICE,
        )
    check(denied.authorization == AuthorizationDecision.DENY)
    check(!denied.executionAttempted)
    check(!denied.executionCompleted)
    check(denied.observedState == null)
    check(denied.verification == VerificationStatus.NOT_ATTEMPTED)

    val readOnly =
        executor.run(
            request =
                ToolRequest(
                    invocationId = "demo-004",
                    requestedIntent = "Read the battery level",
                    toolName = "get_battery_level",
                    argumentsCanonical = "{}",
                    expectedState = null,
                ),
            tool = FakeBatteryTool(batteryPercent = 67),
            modelId = "demo-model",
            inferenceLocation = InferenceLocation.ON_DEVICE,
        )
    check(readOnly.authorization == AuthorizationDecision.ALLOW)
    check(readOnly.riskClass == RiskClass.READ_ONLY)
    check(readOnly.executionAttempted)
    check(readOnly.executionCompleted)
    check(readOnly.observedState == "67")
    check(readOnly.verification == VerificationStatus.NOT_ATTEMPTED)

    println("PASS VERIFIED auth=${verified.authorization} exec=${verified.executionCompleted} observed=${verified.observedState}")
    println("PASS CONTRADICTED auth=${contradicted.authorization} exec=${contradicted.executionCompleted} observed=${contradicted.observedState}")
    println("PASS DENY attempted=${denied.executionAttempted} verification=${denied.verification}")
    println("PASS READ_ONLY observed=${readOnly.observedState} verification=${readOnly.verification}")
}
