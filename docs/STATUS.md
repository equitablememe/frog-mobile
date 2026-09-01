# FROG Mobile evidence ledger

**Status date:** 2026-09-01

This file separates demonstrated facts from proposed work. Update it when evidence changes.

## Demonstrated

### Four-case core conformance smoke

The dependency-free Kotlin core exercises four cases:

```text
VERIFIED
CONTRADICTED
DENY
READ_ONLY
```

Behavior asserted by the smoke:

- **VERIFIED** — authorized reversible action; execution attempted and completed; observed state matched expected state.
- **CONTRADICTED** — execution attempted and completed; observed state disagreed with expected state.
- **DENY** — unknown tool was denied before execution; an adapter that throws on `execute()` or `observe()` was not invoked; verification = `NOT_ATTEMPTED`.
- **READ_ONLY** — battery-like data was returned under `RiskClass.READ_ONLY` with no side-effect postcondition; verification = `NOT_ATTEMPTED`.

### Dual-path GitHub CI

PR #1, commit `bf0008522f47d133d8af1ebe88fe7118c3bb2385`, was tested by GitHub Actions run `33514465700`.

Both jobs completed successfully:

- **Kotlin CLI smoke** — installed Kotlin compiler 2.3.21 and ran `./scripts/test-core.sh`.
- **Gradle smoke** — ran `gradle :core:coreDemo` with Gradle 9.7.1.

This demonstrates the four-case core behavior through two build paths. It does **not** demonstrate Android hardware control.

### Deny semantics decision

For policy-denied requests:

```text
authorization = DENY
executionAttempted = false
executionCompleted = false
verification = NOT_ATTEMPTED
```

`INDETERMINATE` is reserved for cases where postcondition verification is applicable but the observed state cannot be established.

See `docs/DECISION_DENY_SEMANTICS.md`.

## Implemented but not yet demonstrated

- Android/ADK integration skeleton.
- Android torch adapter using `CameraManager` / `TorchCallback`.
- In-memory receipt storage.

These remain provisional until built and tested in their target environments.

## Next evidence gate — M0

Run Google's official `google/adk-kotlin` Android LiteRT-LM example on the target Android device.

Evidence to record:

- upstream commit SHA tested;
- device model;
- Android version;
- model file / model ID;
- build command;
- successful local tool-call transcript;
- confirmation that inference/tool calling still works after network is disabled;
- any errors or limitations.

## M1

Create the real FROG Android app and make it depend on the local `:core` module and current ADK Kotlin artifacts.

## M2

Run one real reversible action:

`set_flashlight(true|false)`

Required proof:

- policy decision;
- execution attempt;
- independent observed torch state;
- receipt with expected and observed state;
- one `VERIFIED` case;
- one deliberately induced non-verified case.

## Claims we are not making yet

FROG is not yet:

- a general Android automation agent;
- an installed Android APK;
- a production security boundary;
- a complete governance framework;
- an AppFunctions implementation;
- a persistent Android receipt system;
- a Google-supported project;
- an upstream ADK feature;
- a revenue-producing product.
