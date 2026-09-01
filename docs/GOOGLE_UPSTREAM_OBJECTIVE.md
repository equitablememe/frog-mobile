# Google ADK Kotlin upstream objective

## Objective
Earn credibility through one small, generally useful contribution to `google/adk-kotlin` after FROG proves the need locally.

## Current upstream hypothesis
A postcondition-verification example or helper may be useful for side-effecting Android tools, but this is a **hypothesis**, not an assumed gap. The repository may evolve before we reach this milestone.

## Required sequence
1. Clone and run Google's Android sample without modifications.
2. Read the current `README.md`, `examples/android/README.md`, `CONTRIBUTING.md`, open issues, and open PRs again on the day work starts.
3. Inspect `adk-python` for an existing equivalent; Google explicitly treats it as a source of truth for validation.
4. Reproduce a concrete problem with a minimal test or example.
5. Implement the smallest fix/example in a fork.
6. Run format and relevant tests.
7. Ensure the PR contains exactly one commit.
8. Sign/check the Google CLA.
9. Write a short PR description: problem, why it matters to ADK users, test evidence, non-goals.
10. Respond to review using `git commit --amend`, then `git push --force-with-lease` so the PR remains one commit.

## Google contribution constraints verified 2026-09-01
- Google CLA is required.
- Code must follow the Google Kotlin Style Guide.
- PRs must contain a single commit.
- AI-generated first drafts are allowed, but Google asks for human refinement.
- ADK Kotlin asks contributors to check alignment with `adk-python`.

## First candidate PR shapes
Prefer one of these only if the evidence shows a real gap:
- `examples(android): demonstrate verified postconditions for a side-effecting tool`
- `testing: add a small helper for postcondition assertions around tool execution`
- `docs(android): clarify execution result vs observed device state`

Do not submit an AXR/FROG manifesto as the PR. The upstream patch should solve an ADK developer problem on its own merits.
