# Agent Guide for meta-qcom

This file guides automation agents to run builds / checks the same way CI does:

- use **kas-container** (isolated from host),
- keep `DL_DIR` and `SSTATE_DIR` outside the repo so caches are shared,
- run `yocto-patchreview` and `oe-selftest` routinely, and run
  `yocto-check-layer` before opening/updating a PR, via the CI helper scripts.

## Project Overview

meta-qcom is an OpenEmbedded / Yocto Project hardware enablement layer for Qualcomm based platforms.

## 1) Prerequisites

1. `kas-container` available on PATH, or set `KAS_CONTAINER=/abs/path/to/kas-container`
   (from [kas-container](https://github.com/siemens/kas/blob/master/kas-container)).
2. Container runtime access (Docker/Podman backend used by `kas-container`).
3. Work directories outside the repository for build outputs and shared caches.

### Container runtime smoke test (required order)

Run Docker first:

```sh
docker run --rm hello-world
```

Then check Podman:

```sh
if command -v podman >/dev/null 2>&1; then
  podman run --rm hello-world
else
  echo "podman not installed; continue with Docker backend"
fi
```

Notes:

- Do not use `sudo` unless the host setup explicitly requires it.
- Do not create or modify user groups as part of this workflow.
- If Podman is unavailable, Docker-only operation is acceptable.

## 2) Recommended environment

If `KAS_WORK_DIR`, `DL_DIR`, and `SSTATE_DIR` are already set in the environment, use them
directly — do not override them. Only set defaults when they are absent:

```sh
export REPO_DIR="$(pwd)"                               # meta-qcom checkout
export KAS_WORK_DIR="${KAS_WORK_DIR:-/path/to/kas-work}"      # outside repo to avoid polling the checkout
export DL_DIR="${DL_DIR:-/path/to/shared-cache/downloads}"
export SSTATE_DIR="${SSTATE_DIR:-/path/to/shared-cache/sstate-cache}"
mkdir -p "${DL_DIR}" "${SSTATE_DIR}" "${KAS_WORK_DIR}"
```

## 3) Build with kas-container (CI style)

CI build composition pattern:
`:ci/<machine>.yml[:distro.yml][:kernel.yml]`

Example:

```sh
export KAS_YAMLS="ci/rb3gen2-core-kit.yml:ci/qcom-distro.yml"
"${KAS_CONTAINER:-kas-container}" build "${KAS_YAMLS}"
```

## 4) Run routine checks via CI helper scripts

For routine local validation, run:

```sh
ci/kas-container-shell-helper.sh ci/yocto-patchreview.sh
ci/kas-container-shell-helper.sh ci/oe-selftest.sh
```

Run `yocto-check-layer` only before opening/updating a pull request:

```sh
ci/kas-container-shell-helper.sh ci/yocto-check-layer.sh
```

### oe-selftest details

- Script: `ci/oe-selftest.sh`
- Auto-discovers tests in `lib/oeqa/selftest/cases/` when no test list is given.
- Honors `DL_DIR` and `SSTATE_DIR` from environment (recommended for shared cache).

Run a subset:

```sh
"${KAS_CONTAINER:-kas-container}" shell ci/base.yml \
  --command "/repo/ci/oe-selftest.sh /repo /work qcom_fitimage.QcomFitImageMatrixTests"
```

If passing explicit tests directly (without helper), call:

```sh
ci/oe-selftest.sh "$REPO_DIR" "$KAS_WORK_DIR" qcom_fitimage.QcomFitImageMatrixTests
```

## 5) Direct kas shell alternative (no helper wrapper)

For one-off commands:

```sh
kas-container shell --skip repos_checkout ci/rb3gen2-core-kit.yml -c "bitbake <target>"
kas-container shell --skip repos_checkout ci/rb3gen2-core-kit.yml -c "oe-selftest --run-tests qcom_fitimage"
```

Use the helper scripts for CI parity whenever possible.

## 6) Pull request / contribution workflow

Follow the repository `README.md` contribution flow:

1. Target branch: **master**.
2. Fork `qualcomm-linux/meta-qcom`, create a topic branch, implement changes.
3. Rebase on latest upstream `master`.
4. Open a GitHub pull request.
5. Use PR discussion for review iteration.

Important:

- Follow Yocto submission guidance referenced in README:
  [Preparing Changes for Submission](https://docs.yoctoproject.org/dev/contributor-guide/submit-changes.html#preparing-changes-for-submission)

Before opening/updating a PR, run CI-equivalent checks in this order:

```sh
ci/kas-container-shell-helper.sh ci/yocto-patchreview.sh
ci/kas-container-shell-helper.sh ci/yocto-check-layer.sh
ci/kas-container-shell-helper.sh ci/oe-selftest.sh
```

## 7) Commit message best practices (project style)

Use the style seen in recent history:

- `component: imperative summary` (preferred when scoped), e.g.
  - `ci/qcom-distro: Include meta-dpdk layer (#1902)`
  - `fit-dtb-compatible: drop SoC version suffixes from compatible strings`
  - `debug.yml: enable FTrace settings in kernel cmdline (#2155)`
- Or concise imperative summary when cross-cutting, e.g.
  - `Drop SoC version suffixes from FIT DTB compatible strings (#2159)`

Every commit **must** include a `Signed-off-by` trailer using the identity from
the local git configuration:

```sh
git commit -s   # or pass --signoff; fetches user.name / user.email from git config
```

If committing programmatically, append the trailer explicitly:

```text
Signed-off-by: $(git config user.name) <$(git config user.email)>
```

Never fabricate a name or email; always read from `git config`.

The `Signed-off-by` identity must match the commit author, and must be a real,
routable identity:

- The author email must appear in a `Signed-off-by` trailer on the same commit.
- Do not author commits through the GitHub web editor: it produces numbered
  `NNNN+user@users.noreply.github.com` addresses and arbitrary display names,
  which are rejected. Configure `git config user.name`/`user.email` locally and
  commit from a checkout.
- Write the trailer as `Signed-off-by: Name <email>` with a space before `<`.
- `Signed-off-by`, `Acked-by`, `Reviewed-by` and similar trailers for anyone
  other than you must reflect approvals that were actually given. This cannot
  be verified automatically and is left to maintainer review; do not fabricate
  a review or sign-off trail.

Guidelines:

- Keep subject line short and specific; capture intent, not a file-by-file dump.
- Use imperative mood (`Add`, `Update`, `Drop`, `Enable`, `Revert`).
- Prefix scoped changes with `component: summary` (note the space after the
  colon).
- Do not use Conventional Commits prefixes (`feat:`, `fix(scope):`, `chore:`);
  follow the OE commit policy and the `component: summary` style above.
- Do not use kernel-tree subject prefixes (`FROMLIST:`, `UPSTREAM:`,
  `BACKPORT:`, `FROMGIT:`).
- Add a body for non-trivial changes explaining **why** and key design decisions.
- Wrap body lines for readability (~72 chars).
- Use consistent recipe bump wording for version updates, e.g.
  `recipe-name: Update to vX.Y.Z`.
- Include PR reference in subject when appropriate: `(#NNNN)`.
- Avoid mixing unrelated changes in one commit; split logically.
- Each patch must be logically coherent, self-contained, and independently buildable.
- The tree must remain in a functional state after every commit.
- Fixups within the same patch series are not allowed; changes should be corrected in the patch where they are introduced.
- Rebase your branch on upstream `master`; do not merge `master` into it. Merge
  commits in a pull request are rejected.
- Every patch file added under a recipe must carry an `Upstream-Status:` header
  with a valid value (`Submitted`, `Backport`, `Pending`, `Inappropriate`,
  `Denied`, `Accepted`).

### Authoring commits

Beyond the rules above, these make a series easier to review:

- Keep each commit atomic: one self-contained logical change, so a regression
  can be pinned to it with `git bisect` and reverted on its own. If the
  subject needs an "and also" (`Foo, and while we are at it, bar and baz`), it
  should have been more than one commit.
- Start the commit message with the problem or issue being solved, then
  explain the change and why it is the right fix, not just restate the diff.
- Keep a valid `Signed-off-by` chain. Sign off with `git commit -s`, which takes
  the trailer from `git config`.
- When you pick or apply a commit written by someone else, keep their authorship
  and add your own sign-off with `-s`.
- Backport with `git cherry-pick -x` so the message records the upstream commit
  it came from.
