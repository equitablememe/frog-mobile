package frog.core

import java.time.Instant

enum class AuthorizationDecision { ALLOW, ASK, DENY }
enum class RiskClass { READ_ONLY, REVERSIBLE_LOCAL, EXTERNAL_SIDE_EFFECT, DESTRUCTIVE, FINANCIAL }
enum class VerificationStatus { NOT_ATTEMPTED, VERIFIED, CONTRADICTED, INDETERMINATE, FAILED }
enum class InferenceLocation { ON_DEVICE, CLOUD, HYBRID, UNKNOWN }

data class ToolRequest(
    val invocationId: String,
    val requestedIntent: String,
    val toolName: String,
    val argumentsCanonical: String,
    val expectedState: String? = null,
)

data class PolicyDecision(
    val authorization: AuthorizationDecision,
    val riskClass: RiskClass,
    val reversible: Boolean,
    val rationale: String,
)

data class ExecutionResult(
    val attempted: Boolean,
    val completed: Boolean,
    val detail: String? = null,
    val error: String? = null,
)

data class Observation(
    val state: String? = null,
    val error: String? = null,
)

data class VerificationResult(
    val status: VerificationStatus,
    val rationale: String,
)

data class ActionReceipt(
    val schemaVersion: String = "0.1",
    val invocationId: String,
    val requestedIntent: String,
    val toolName: String,
    val argumentsSha256: String,
    val authorization: AuthorizationDecision,
    val riskClass: RiskClass,
    val reversible: Boolean,
    val policyRationale: String,
    val executionAttempted: Boolean,
    val executionCompleted: Boolean,
    val executionDetail: String?,
    val expectedState: String?,
    val observedState: String?,
    val verification: VerificationStatus,
    val verificationRationale: String,
    val startedAt: Instant,
    val completedAt: Instant,
    val modelId: String,
    val inferenceLocation: InferenceLocation,
    val error: String? = null,
)
