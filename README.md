# FROG Mobile

**FROG** = **Forensic Runtime for On-device Governance**.

FROG Mobile is a local-first Android agent experiment built around one engineering rule:

> A tool returning normally is evidence of attempted execution, not proof that the intended device state exists.

The project separates:

**intent → authorization → execution → observation → verification → receipt**

## Why this exists

Many agent demos stop at “the tool call returned.” FROG asks the next question:

**Did the requested state actually happen?**

The first proof is intentionally small: an on-device Android agent requests a reversible flashlight action, FROG applies policy, Android executes it, the device state is observed independently, and an action receipt records whether the requested postcondition was **VERIFIED**, **CONTRADICTED**, **INDETERMINATE**, or **FAILED**.

## Current evidence

### Verified before repository bootstrap

The dependency-free Kotlin core was compiled and executed with JDK 21 / `kotlinc` in the preparation environment. Two smoke cases passed:

```text
PASS verified=VERIFIED observed=ON
PASS contradicted=CONTRADICTED observed=OFF
```

The second case deliberately simulates a platform call that returns normally while the requested state does not occur.

### Not yet verified

- GitHub Actions smoke workflow
- Android integration skeleton compilation
- Google ADK Kotlin LiteRT-LM example on the target phone
- Real flashlight execution and postcondition verification on hardware
- Any upstream contribution to Google

See [`docs/STATUS.md`](docs/STATUS.md) for the evidence ledger.

## Repository structure

```text
frog-mobile/
├── core/                 # dependency-free Kotlin policy/verification core
├── android-skeleton/     # Android + Google ADK integration sketch
├── docs/                 # decisions, status, handoff, setup, upstream objective
├── .github/              # issue templates, PR template, CI
└── scripts/test-core.sh  # local smoke test
```

## Run the verified core demo

Requires JDK 21+ and `kotlinc`:

```bash
./scripts/test-core.sh
```

Expected output:

```text
PASS verified=VERIFIED observed=ON
PASS contradicted=CONTRADICTED observed=OFF
```

## First real-device milestone — M0

Reproduce Google's official ADK Kotlin **LiteRT-LM Android example unmodified** on an Android device with 8 GB+ RAM. After its model is downloaded, demonstrate local tool calling with network access disabled.

Only after M0 is evidenced do we connect FROG's Android flashlight adapter.

## Upstream goal

The long-term objective is not to advertise FROG inside Google's repository. It is to discover one concrete developer problem, prove it locally, and contribute the smallest generally useful fix, test, example, or documentation improvement to [`google/adk-kotlin`](https://github.com/google/adk-kotlin).

## Project rule

**Build the smallest thing that produces falsifiable evidence, then advance one boundary at a time.**

## License

Not selected yet. This is a deliberate bootstrap decision, not an omission.
