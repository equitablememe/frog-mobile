# FROG Mobile evidence ledger

**Status date:** 2026-09-01

This file separates demonstrated facts from proposed work. Update it when evidence changes.

## Demonstrated

### Core invariant smoke test

The dependency-free Kotlin core was compiled and executed with JDK 21 / `kotlinc` in the preparation environment.

Observed output:

```text
PASS verified=VERIFIED observed=ON
PASS contradicted=CONTRADICTED observed=OFF
```

Interpretation:

- `VERIFIED` means the observed state matched the requested postcondition.
- `CONTRADICTED` means execution reported completion but observation disagreed with the requested postcondition.

This demonstrates the core distinction. It does **not** demonstrate Android hardware control.

## Implemented but not yet demonstrated

- Gradle project metadata for the core module.
- GitHub Actions smoke workflow.
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
- a production security boundary;
- a complete governance framework;
- an AppFunctions implementation;
- a Google-supported project;
- an upstream ADK feature.
