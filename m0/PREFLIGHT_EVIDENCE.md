# M0 preflight evidence

This file records build evidence for the official Google ADK Kotlin Android sample before real-device M0.

## Preflight run 1 — PASS

**Date:** 2026-09-01  
**FROG PR:** #3  
**Workflow:** M0 upstream Android preflight  
**Workflow run:** 33517666101  
**Result:** SUCCESS

### Upstream source

```text
repository: google/adk-kotlin
commit: 2618ab0f094d0f1a9b1dd11771ed59b39b38f1b7
```

That commit was the observed head of Google's `main` branch when the M0 pin was created.

### Build environment

The workflow mirrored the relevant Android choices in Google's own validation workflow:

```text
JDK: 21
Android platform: 36
Android build-tools: 36.0.0
Gradle task: :google-adk-kotlin-examples-android:assembleDebug
```

### Produced APK

```text
google-adk-kotlin-examples-android-debug.apk
```

APK SHA-256:

```text
cbc05b81a221e743c2b2c3e9fa065f4c19afea04d529ac46af305b46d571fccf
```

GitHub artifact:

```text
google-adk-kotlin-android-2618ab0f094d
artifact id: 9804454714
artifact archive digest:
sha256:18527a4d02ba7c24c26770e41bd8ef249a538b2cd089d5125f6884f4d55c8bbc
retention through: 2026-09-15
```

The artifact ZIP was approximately 48 MB and contained the APK plus the generated preflight metadata file.

## What this proves

At the pinned Google source revision, GitHub Actions successfully compiled the unmodified official ADK Kotlin Android examples source into a debug APK.

## What this does not prove

This is **not M0 completion**. It does not prove:

- APK installation on the target phone;
- LiteRT-LM model download;
- successful model load;
- on-device inference;
- tool calling;
- offline operation;
- FROG Android integration;
- real flashlight control.

## Provenance note

The first PR run recorded GitHub's PR merge-test SHA in the field then named `frog_commit`. That value is useful as a CI test ref, but it is not the same thing as the FROG branch source commit.

The workflow was subsequently hardened to record these separately:

```text
frog_source_commit
ci_test_commit
```

This note preserves the original result rather than silently rewriting its provenance semantics.
