package frog.mobile.android

import frog.core.AuthorizationDecision
import frog.core.PolicyDecision
import frog.core.RiskClass
import frog.core.ToolPolicy
import frog.core.ToolRequest

/** Conservative starter policy: unknown tools are denied by default. */
class FrogPolicy : ToolPolicy {
    override fun evaluate(request: ToolRequest): PolicyDecision =
        when (request.toolName) {
            "set_flashlight" -> PolicyDecision(
                authorization = AuthorizationDecision.ALLOW,
                riskClass = RiskClass.REVERSIBLE_LOCAL,
                reversible = true,
                rationale = "Flashlight state is local, visible, and reversible.",
            )
            else -> PolicyDecision(
                authorization = AuthorizationDecision.DENY,
                riskClass = RiskClass.EXTERNAL_SIDE_EFFECT,
                reversible = false,
                rationale = "Unknown tools are denied until explicitly classified.",
            )
        }
}
