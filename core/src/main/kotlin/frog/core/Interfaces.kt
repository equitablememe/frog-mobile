package frog.core

interface ToolPolicy {
    fun evaluate(request: ToolRequest): PolicyDecision
}

interface ToolAdapter {
    val name: String
    fun execute(request: ToolRequest): ExecutionResult
    fun observe(request: ToolRequest): Observation
}

interface Verifier {
    fun verify(request: ToolRequest, observation: Observation): VerificationResult
}
