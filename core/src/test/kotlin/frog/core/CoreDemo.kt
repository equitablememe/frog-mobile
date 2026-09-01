package frog.core

private class FlashlightPolicy : ToolPolicy {
    override fun evaluate(request: ToolRequest): PolicyDecision =
        if (request.toolName == "set_flashlight") {
            PolicyDecision(
                authorization = AuthorizationDecision.ALLOW,
                riskClass = RiskClass.REVERSIBLE_LOCAL,
                reversible = true,
                rationale = "Flashlight state is local and reversible.",
            )
        } else {
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

fun main() {
    val executor = AuditedToolExecutor(FlashlightPolicy())
    val request = ToolRequest(
        invocationId = "demo-001",
        requestedIntent = "Turn the flashlight on",
        toolName = "set_flashlight",
        argumentsCanonical = "{\"enabled\":true}",
        expectedState = "ON",
    )

    val verified = executor.run(
        request = request,
        tool = FakeFlashlightTool(sabotage = false),
        modelId = "demo-model",
        inferenceLocation = InferenceLocation.ON_DEVICE,
    )
    check(verified.verification == VerificationStatus.VERIFIED)

    val contradicted = executor.run(
        request = request.copy(invocationId = "demo-002"),
        tool = FakeFlashlightTool(sabotage = true),
        modelId = "demo-model",
        inferenceLocation = InferenceLocation.ON_DEVICE,
    )
    check(contradicted.executionCompleted)
    check(contradicted.verification == VerificationStatus.CONTRADICTED)

    println("PASS verified=${verified.verification} observed=${verified.observedState}")
    println("PASS contradicted=${contradicted.verification} observed=${contradicted.observedState}")
}
