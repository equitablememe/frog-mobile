# FROG Mobile

**FROG** = **Forensic Runtime for On-device Governance**.

FROG Mobile is a local-first Android agent experiment built around one engineering rule:

> **A tool return is not a changed world.**

A tool returning normally is evidence of attempted execution, not proof that the intended device state exists.

The project separates:

**intent → authorization → execution → observation → verification → receipt**

## Why this exists

Many agent demos stop at “the tool call returned.” FROG asks the next question:

**Did the requested state actually happen?**

The first proof is intentionally small: an on-device Android agent requests a reversible flashlight action, FROG applies policy, Android executes it, the device state is observed independently, and an action receipt records whether the requested postcondition was **VERIFIED**, **CONTRADICTED**, **INDETERMINATE**, **FAILED**, or **NOT_ATTEMPTED**.

## Current core evidence

The dependency-free Kotlin core now exercises four conformance cases:

```text
PASS VERIFIED
PASS CONTRADICTED
PASS DENY
PASS READ_ONLY
```

GitHub Actions validates the same core through two independent build paths:

- Kotlin CLI: `./scripts/test-core.sh` → `kotlinc`
- Gradle: `gradle :core:coreDemo`

Both jobs passed on the core-conformance PR before merge.

The cases mean:

- **VERIFIED** — authorized execution completed and observed state matched the expected postcondition.
- **CONTRADICTED** — execution completed but observed state disagreed with the expected postcondition.
- **DENY** — unknown tool was blocked before execution; verification was `NOT_ATTEMPTED`.
- **READ_ONLY** — data was read without inventing a side-effect verification claim.

See [`docs/CONFORMANCE.md`](docs/CONFORMANCE.md) for the behavioral contract and [`docs/STATUS.md`](docs/STATUS.md) for the evidence ledger.

## Still not verified

- Android integration skeleton compilation
- Google ADK Kotlin LiteRT-LM example on the target phone
- Real flashlight execution and postcondition verification on hardware
- Android receipt persistence
- Any upstream contribution to Google

## Repository structure

```text
frog-mobile/
├── core/                 # dependency-free Kotlin policy/verification core
├── android-skeleton/     # Android + Google ADK integration sketch
├── docs/                 # decisions, conformance, status, handoff, setup
├── .github/              # issue templates, PR template, CI
└── scripts/test-core.sh  # Kotlin CLI smoke test
```

## Run the core smoke

Requires JDK 21+ and `kotlinc`:

```bash
./scripts/test-core.sh
```

You can also run the Gradle path:

```bash
gradle :core:coreDemo
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
