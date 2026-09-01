# M0 upstream Android preflight

This preflight reduces the number of unknowns before the real-device M0 test.

It does **not** complete M0.

## What it tests

FROG's CI checks out one exact commit of Google's public `google/adk-kotlin` repository and builds the official Android examples app without changing Google's source.

Pinned revision:

```text
google/adk-kotlin
2618ab0f094d0f1a9b1dd11771ed59b39b38f1b7
```

The pin is stored canonically in:

```text
m0/adk-kotlin.sha
```

At the time of pinning (2026-09-01), that commit was the head of Google's `main` branch.

## Why this revision is useful

At this revision Google's Android README states that:

- the LiteRT-LM example performs inference fully on-device;
- its model is downloaded on first use;
- the default model is `gemma-4-E2B-it.litertlm`, about 2.5 GB;
- Google recommends a device with 8 GB RAM or more;
- after the model is obtained, the LiteRT-LM example runs offline;
- the official install command with a connected device is:

```bash
./gradlew :google-adk-kotlin-examples-android:installDebug
```

Google's own validation workflow at the pinned revision provisions:

```text
JDK 21
Android platform 36
Android build-tools 36.0.0
```

FROG's preflight mirrors those relevant toolchain choices and runs:

```bash
./gradlew   -PjdkVersion=21   -Pkotlin.daemon.jvmargs="-Xmx4g"   --no-daemon   --stacktrace   :google-adk-kotlin-examples-android:assembleDebug
```

## Evidence produced

If the workflow succeeds, GitHub uploads an artifact containing:

- the unmodified Google sample's debug APK;
- `m0-preflight-metadata.txt`;
- the pinned upstream commit;
- the FROG commit that requested the build;
- SHA-256 of the resulting APK.

This demonstrates only:

> At the pinned upstream revision, the official Google ADK Kotlin Android examples source compiled into an APK in FROG's GitHub Actions environment.

It does **not** demonstrate:

- installation on the target phone;
- model download;
- model loading;
- on-device inference;
- tool calling;
- offline operation;
- FROG's Android skeleton;
- real flashlight control.

## Real M0 remains

The hardware gate remains Issue #2:

1. install/run the official sample on the target phone;
2. open LiteRT-LM chat;
3. acquire the model;
4. demonstrate tool calling;
5. disable network access;
6. demonstrate that the already-acquired model continues to infer and call tools offline;
7. capture the evidence and every limitation.

If any stage fails, record the failing layer instead of skipping ahead.

## Updating the upstream pin

Do not silently float to Google's latest `main`.

When intentionally updating:

1. read the current Android README;
2. inspect Google's current validation workflow;
3. record the new upstream SHA;
4. change `m0/adk-kotlin.sha`;
5. let the preflight build the new revision;
6. preserve the prior evidence in Git history.

That makes upstream drift visible and reviewable.
