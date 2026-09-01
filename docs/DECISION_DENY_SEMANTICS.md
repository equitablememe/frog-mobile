# Decision: verification semantics for policy-denied requests

**Status:** accepted for FROG v0.1 core conformance  
**Decision:** a request denied before execution records `verification = NOT_ATTEMPTED`.

## Question

When policy returns `DENY` (or another non-`ALLOW` decision) before a tool executes, should the receipt say:

- `NOT_ATTEMPTED`, or
- `INDETERMINATE`?

Authorization and verification remain separate fields either way.

## Option A — NOT_ATTEMPTED

Meaning:

> FROG did not attempt to verify the requested postcondition because policy prevented execution.

### Strengths

- Matches the causal sequence: no authorized execution occurred.
- Distinguishes policy prevention from observational uncertainty.
- Makes `INDETERMINATE` available for a more useful meaning: an execution/observation path existed, but the resulting state could not be established.
- Avoids implying that FROG tried and failed to verify a denied action.

### Failure mode

A consumer that looks only at `verification` and ignores `authorization` may miss the reason verification was not attempted.

Mitigation: consumers must read authorization and verification as separate dimensions.

## Option B — INDETERMINATE

Meaning:

> The requested postcondition is not known to be true or false.

### Strengths

- Logically, the world state may indeed be unknown after a denial.
- Preserves the idea that FROG cannot assert the requested state merely because it refused to act.

### Failure mode

It collapses two very different situations:

1. policy deliberately prevented execution; and
2. execution was allowed/attempted but observation could not establish the result.

That makes receipts less diagnostic.

## Decision

Use:

```text
authorization = DENY
executionAttempted = false
executionCompleted = false
verification = NOT_ATTEMPTED
```

Reserve `INDETERMINATE` for cases where postcondition verification is applicable but FROG cannot establish the observed state.

## Migration consequence

The bootstrap executor previously returned `INDETERMINATE` for a denied request that carried an `expectedState`. v0.1 conformance changes that behavior to `NOT_ATTEMPTED`.

Any downstream consumer depending on the old behavior must instead inspect `authorization` to distinguish denial from other non-verified outcomes.

## Preserved alternative

`INDETERMINATE` remains a defensible description of the external world state after denial. FROG rejects it here because the verification field describes the verification process, not omniscient truth about the world.
