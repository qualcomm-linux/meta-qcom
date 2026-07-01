# Contributing to meta-qcom

Please submit any patches against the `meta-qcom` layer (branch **master**)
by using the GitHub pull-request feature. Fork the repo, create a branch,
do the work, rebase from upstream, and create the pull request.

For some useful guidelines when submitting patches, please refer to:
[Preparing Changes for Submission](https://docs.yoctoproject.org/dev/contributor-guide/submit-changes.html#preparing-changes-for-submission)

Pull requests will be discussed within the GitHub pull-request infrastructure.

## Commit messages

Each commit must be atomic: it must contain exactly one logical change. Do not
squash multiple features, fixes, or otherwise unrelated changes into a single
commit — split them into separate commits, one per logical change. Each patch
must be logically coherent, self-contained, and independently buildable, and
the tree must remain in a functional state after every commit.

Each commit must contain a well-formed commit subject and message.

The commit subject must follow the form `recipe-name: summary of the changes`,
where `recipe-name` identifies the recipe or component being touched and the
summary concisely describes the change. Keep the subject short and specific,
capturing intent rather than a file-by-file dump. For example:

- `ci/qcom-distro: Include meta-dpdk layer`
- `fit-dtb-compatible: drop SoC version suffixes from compatible strings`
- `debug.yml: enable FTrace settings in kernel cmdline`

Use consistent wording for version upgrades, e.g.
`recipe-name: upgrade vX.Y.Z -> vA.B.C`.

The commit message (the body) must:

- be written in plain English;
- first describe the issue or the problem that is being solved, so that a
  reader can understand *why* the change is needed;
- then use the imperative mood (e.g. "add", "drop", "enable", "update")
  to describe the actions to be performed in order to solve the problem;
- not merely restate *what* the diff changes line by line — the diff
  already shows that;
- avoid unnecessary bullet lists; prefer prose paragraphs;
- wrap body lines for readability (~72 chars).

## Sign-off and trailers

Every commit must also carry a `Signed-off-by` trailer matching the
author identity from your local `git config` (use `git commit -s`). Never
fabricate a name or email; always read them from `git config`.

If an AI coding assistant or other advanced tool was used to help create the
change, acknowledge that use by adding an `Assisted-by` trailer in the form:

```text
Assisted-by: AGENT_NAME:MODEL_VERSION [TOOL1] [TOOL2]
```

Where `AGENT_NAME` is the name of the AI tool or framework, `MODEL_VERSION` is
the specific model version used, and `[TOOL1] [TOOL2]` are optional specialized
analysis tools. Basic development tools (git, gcc, make, editors) should not be
listed. For example:

```text
Assisted-by: ExampleAgent:example-model-1.0
```
