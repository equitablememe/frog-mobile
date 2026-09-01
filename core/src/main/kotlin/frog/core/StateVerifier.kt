package frog.core

class StateVerifier : Verifier {
    override fun verify(request: ToolRequest, observation: Observation): VerificationResult {
        val expected = request.expectedState
            ?: return VerificationResult(
                VerificationStatus.NOT_ATTEMPTED,
                "No expected postcondition was supplied.",
            )

        observation.error?.let {
            return VerificationResult(
                VerificationStatus.FAILED,
                "Observation failed: $it",
            )
        }

        val observed = observation.state
            ?: return VerificationResult(
                VerificationStatus.INDETERMINATE,
                "No observable state was returned.",
            )

        return if (observed == expected) {
            VerificationResult(
                VerificationStatus.VERIFIED,
                "Observed state matches the expected postcondition.",
            )
        } else {
            VerificationResult(
                VerificationStatus.CONTRADICTED,
                "Observed state '$observed' contradicts expected state '$expected'.",
            )
        }
    }
}
