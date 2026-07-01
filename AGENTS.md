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

Follow the contribution workflow documented in
[CONTRIBUTING.md](CONTRIBUTING.md):

1. Target branch: **master**.
2. Fork `qualcomm-linux/meta-qcom`, create a topic branch, implement changes.
3. Rebase on latest upstream `master`.
4. Open a GitHub pull request.
5. Use PR discussion for review iteration.

Before opening/updating a PR, run CI-equivalent checks in this order:

```sh
ci/kas-container-shell-helper.sh ci/yocto-patchreview.sh
ci/kas-container-shell-helper.sh ci/yocto-check-layer.sh
ci/kas-container-shell-helper.sh ci/oe-selftest.sh
```

## 7) Commit message best practices (project style)

Follow the commit subject and message requirements documented in
[CONTRIBUTING.md](CONTRIBUTING.md): an atomic change per commit, a
`recipe-name: summary of the changes` subject, a plain-English body that
explains the problem before the imperative actions, and the mandatory
`Signed-off-by` (and, when applicable, `Assisted-by`) trailers.

When committing programmatically, take the `Signed-off-by` identity from the
local git configuration and append the trailer explicitly:

```text
Signed-off-by: $(git config user.name) <$(git config user.email)>
```

Never fabricate a name or email; always read them from `git config`.

Fixups within the same patch series are not allowed; changes should be
corrected in the patch where they are introduced.
