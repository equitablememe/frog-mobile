# FROG Mobile handoff prompt

Canonical repository: `equitablememe/frog-mobile`

You are continuing FROG Mobile, an Android local-agent experiment centered on verifiable tool execution.

## Current invariant
Never equate “a tool returned without error” with “the intended postcondition is true.” Keep intent, authorization, execution, observation, verification, and receipt provenance separate.

## Current scope
Only advance the next smallest milestone that produces working evidence. Do not add broad phone control until the flashlight proof is green.

## First tasks
1. Run `./scripts/test-core.sh` and preserve its output.
2. Reproduce the official Google ADK Kotlin Android LiteRT-LM example on a real 8 GB+ Android device.
3. Create a clean FROG Android application that depends on ADK Kotlin rather than copying Google sample code.
4. Integrate the core module and torch adapter.
5. Demonstrate VERIFIED and deliberately CONTRADICTED receipts.

## Evidence discipline
For each milestone record:
- exact commit SHA,
- device and Android version,
- model identifier,
- inference location,
- commands run,
- test output,
- expected state,
- observed state,
- unresolved failures.

Do not hide or smooth contradictions. If a claim is unverified, label it unverified.

## Upstream rule
Do not send a Google PR merely to advertise FROG. First reproduce a concrete missing primitive or developer pain point, prove it locally, compare against `adk-python` where relevant, then submit the smallest generally useful change.
