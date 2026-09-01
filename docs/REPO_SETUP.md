# Repository setup playbook

## Canonical repository

The first repository is:

`equitablememe/frog-mobile`

The project starts as one repository. Do not split FROG/LILYPAD/CROAK/POND into separate repositories until a module earns its own users, API contract, versioning, or release boundary.

## Why one repo first

Multiple empty repositories create administrative surface. Working commits create evidence.

## If this project is ever recreated elsewhere

Recommended repository name: `frog-mobile`

GitHub web flow:
1. Open GitHub.
2. Press **+** → **New repository**.
3. Choose the owner.
4. Repository name: `frog-mobile`.
5. Description: `FROG — Forensic Runtime for On-device Governance. A local-first Android agent that authorizes actions, executes them, verifies the result, and records auditable receipts.`
6. Use Public visibility when discoverability and open collaboration are intended.
7. If a prepared working tree already contains README / .gitignore / license files, do not initialize duplicates in GitHub.
8. Create the repository.
9. Use the exact remote URL GitHub shows.

Typical local flow:

```bash
git init
git add .
git commit -m "chore: bootstrap FROG Mobile"
git branch -M main
git remote add origin <URL-GITHUB-SHOWS-YOU>
git push -u origin main
```

## Names reserved conceptually — not repositories yet

- `CROAK` — action-receipt / provenance vocabulary.
- `LILYPAD` — policy and authorization layer.
- `TADPOLE` — possible experimental/incubator layer.
- `POND` — persistence vocabulary.

Create a separate repository only when the boundary becomes technically useful.

## Learning checkpoint

When you create or clone a repository, make sure you can explain these five terms in your own words:

- **repository** — the project's versioned history and files;
- **commit** — one recorded snapshot/change set;
- **branch** — a movable line of development;
- **remote** — another copy/location of the repository, such as GitHub;
- **pull request** — a proposal to merge one branch's changes into another.
