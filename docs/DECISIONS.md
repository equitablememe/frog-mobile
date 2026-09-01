# Decision log

## D-001 — One repository first
**Decision:** Start with `frog-mobile` as the canonical project. Do not split FROG/LILYPAD/CROAK/POND into separate repositories until a module has an independent release boundary.

**Reason:** Multiple empty repositories create administrative surface without producing working software.

**Revisit when:** A module has its own users, versioning, API contract, or upstream reuse case.

## D-002 — Verify postconditions
**Decision:** A tool call returning normally is evidence that execution was attempted, not proof that the intended state exists.

**Reason:** Agentic systems routinely conflate “tool returned” with “world changed as intended.”

## D-003 — Deny unknown tools by default
**Decision:** Unclassified tools are denied in the starter policy.

**Reason:** Adding capability should require an explicit risk and authorization decision.

## D-004 — Local first, cloud optional
**Decision:** The first proof uses on-device LiteRT-LM with tool calling. Cloud models are a later delegation option, not a dependency of the proof.
