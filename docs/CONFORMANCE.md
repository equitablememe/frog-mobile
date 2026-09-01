# FROG v0.1 core conformance

FROG conformance is about observable behavior, not a specific UI, model, Android API, or storage engine.

The core invariant is:

> A tool return is not a changed world.

A conforming execution preserves these dimensions separately:

```text
intent
→ authorization
→ execution attempt
→ execution completion
→ expected postcondition
→ observation
→ verification
→ receipt
```

## Required receipt dimensions

For each tool request, a conforming implementation must preserve enough information to determine:

- authorization decision;
- risk classification;
- whether execution was attempted;
- whether execution completed;
- expected state, when a side-effecting postcondition is defined;
- observed state, when available;
- verification result;
- model/inference provenance;
- error information without converting it into a false success.

## Verification states

### VERIFIED

Use when a postcondition is defined and the observed state matches it.

```text
expected = ON
observed = ON
verification = VERIFIED
```

`VERIFIED` does not erase execution metadata. A consumer can still see whether execution completed normally.

### CONTRADICTED

Use when a postcondition is defined and the observed state conflicts with it.

```text
executionCompleted = true
expected = ON
observed = OFF
verification = CONTRADICTED
```

This is the signature FROG case: a normal tool return does not override contradictory observation.

### NOT_ATTEMPTED

Use when postcondition verification is not applicable or was deliberately not attempted.

Examples:

- policy denied execution before the tool ran;
- a read-only operation has no side-effect postcondition.

For denied actions, authorization explains *why* verification was not attempted.

### INDETERMINATE

Use when a side-effecting postcondition should be verified but FROG cannot establish an observed state.

Example:

```text
authorization = ALLOW
executionAttempted = true
expected = ON
observed = <unavailable>
verification = INDETERMINATE
```

### FAILED

Use when the observation/verification mechanism itself reports an error rather than an absent or ambiguous state.

The receipt must preserve the underlying error.

## Read-only tools

Read-only tools may execute and return data, but they must not fabricate a side-effect postcondition merely to produce `VERIFIED`.

For v0.1:

```text
riskClass = READ_ONLY
expectedState = null
verification = NOT_ATTEMPTED
```

A read result may still be carried in `observedState` by the current schema. This is a known v0.1 simplification; a future schema may separate returned data from postcondition observation.

## Policy denial

A denied request must not execute or observe the denied tool.

Minimum behavior:

```text
authorization = DENY
executionAttempted = false
executionCompleted = false
observedState = null
verification = NOT_ATTEMPTED
```

The conformance smoke uses an adapter that throws if `execute()` or `observe()` is called, so an accidental policy bypass fails loudly.

## Execution failure

Execution failure and postcondition verification are separate dimensions.

If execution reports an error, the receipt must preserve:

- `executionAttempted`;
- `executionCompleted = false`;
- the execution error.

If independent observation is still possible, verification may still describe the observed postcondition. Implementations must not rewrite `executionCompleted` to true merely because the final state happens to match.

## Observation failure

If an observation attempt reports an error, verification is `FAILED`.

If observation provides no usable state without reporting an error, verification is `INDETERMINATE`.

## Core truth table

| Authorization | Risk | Exec attempted | Exec completed | Expected | Observed | Verification |
|---|---|---:|---:|---|---|---|
| ALLOW | reversible side effect | true | true | ON | ON | VERIFIED |
| ALLOW | reversible side effect | true | true | ON | OFF | CONTRADICTED |
| DENY | any | false | false | any/null | null | NOT_ATTEMPTED |
| ALLOW | READ_ONLY | true | true | null | data | NOT_ATTEMPTED |
| ALLOW | side effect | true | any | ON | unavailable | INDETERMINATE |
| ALLOW | side effect | true | any | ON | observation error | FAILED |

## Minimum v0.1 conformance smoke

A conforming core smoke must exercise at least:

1. **VERIFIED** — authorized reversible action; execution completes; observed state matches.
2. **CONTRADICTED** — execution completes; observed state disagrees.
3. **DENY** — unknown tool is denied and cannot execute.
4. **READ_ONLY** — read-only data is returned without a false side-effect verification claim.

## Non-claims

Passing core conformance does not prove:

- Android hardware control;
- an installed APK;
- model correctness;
- security hardening;
- receipt persistence;
- Google ADK compatibility;
- M0 or M2 completion.
