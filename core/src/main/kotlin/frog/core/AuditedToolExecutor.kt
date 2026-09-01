package frog.core

import java.time.Clock
import java.time.Instant

class AuditedToolExecutor(
    private val policy: ToolPolicy,
    private val verifier: Verifier = StateVerifier(),
    private val clock: Clock = Clock.systemUTC(),
) {
    fun run(
        request: ToolRequest,
        tool: ToolAdapter,
        modelId: String,
        inferenceLocation: InferenceLocation,
    ): ActionReceipt {
        require(tool.name == request.toolName) {
            "Tool adapter '${tool.name}' does not match request '${request.toolName}'."
        }

        val startedAt = Instant.now(clock)
        val decision = policy.evaluate(request)

        if (decision.authorization != AuthorizationDecision.ALLOW) {
            val status = if (request.expectedState == null) {
                VerificationStatus.NOT_ATTEMPTED
            } else {
                VerificationStatus.INDETERMINATE
            }
            return ActionReceipt(
                invocationId = request.invocationId,
                requestedIntent = request.requestedIntent,
                toolName = request.toolName,
                argumentsSha256 = sha256(request.argumentsCanonical),
                authorization = decision.authorization,
                riskClass = decision.riskClass,
                reversible = decision.reversible,
                policyRationale = decision.rationale,
                executionAttempted = false,
                executionCompleted = false,
                executionDetail = null,
                expectedState = request.expectedState,
                observedState = null,
                verification = status,
                verificationRationale = "Execution was not authorized, so no postcondition was asserted.",
                startedAt = startedAt,
                completedAt = Instant.now(clock),
                modelId = modelId,
                inferenceLocation = inferenceLocation,
            )
        }

        val execution = tool.execute(request)
        val observation = tool.observe(request)
        val verification = verifier.verify(request, observation)

        return ActionReceipt(
            invocationId = request.invocationId,
            requestedIntent = request.requestedIntent,
            toolName = request.toolName,
            argumentsSha256 = sha256(request.argumentsCanonical),
            authorization = decision.authorization,
            riskClass = decision.riskClass,
            reversible = decision.reversible,
            policyRationale = decision.rationale,
            executionAttempted = execution.attempted,
            executionCompleted = execution.completed,
            executionDetail = execution.detail,
            expectedState = request.expectedState,
            observedState = observation.state,
            verification = verification.status,
            verificationRationale = verification.rationale,
            startedAt = startedAt,
            completedAt = Instant.now(clock),
            modelId = modelId,
            inferenceLocation = inferenceLocation,
            error = execution.error ?: observation.error,
        )
    }
}
