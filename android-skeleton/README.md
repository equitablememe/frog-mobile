# Android integration skeleton

These files are an **integration sketch**, not a complete Android Studio module yet.
They show the seam between the verified dependency-free `core` and Android/Google ADK Kotlin.

## Intended dependencies

At implementation time, pin versions from the current `google/adk-kotlin` README rather than copying stale numbers. As checked on 2026-09-01, the official README advertises ADK Kotlin `0.9.0` and the LiteRT-LM backend supports on-device tool calling.

A real app will need, at minimum:

- Android application plugin + Compose (if using Compose UI)
- `com.google.adk:google-adk-kotlin-core:<current>`
- `com.google.adk:google-adk-kotlin-litertlm:<current>`
- the ADK KSP processor for `@Tool`
- the LiteRT-LM Android runtime
- the local `:core` module from this starter

## First integration target

1. Reproduce Google's official LiteRT-LM Android example unmodified.
2. Create a separate FROG Android app.
3. Add the `:core` module.
4. Wire `FrogDeviceTools` to a locally loaded LiteRT-LM model.
5. Ask: “Turn my flashlight on.”
6. Confirm the physical light changes.
7. Confirm the receipt reports `VERIFIED`.
8. Force a mismatch and confirm the receipt reports `CONTRADICTED`.

Do not add messaging, purchases, deletes, accessibility control, or cloud delegation before this proof is green.
