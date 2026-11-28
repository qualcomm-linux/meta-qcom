+++
title = "CI and workflows"
paths = [".github/", "ci/"]
symbols = ["workflow_run", "LAVA_TEST_PLANS_REF"]
+++

Review notes for workflows, actions and the kas files under `ci/`. Many of
these changes are not exercised by the pull request that makes them.

- Find what triggers the changed workflow and in which context it runs. A
  change to a workflow that runs on `workflow_run`, on push or on a
  schedule is first exercised after merge. Ask for a run from a branch
  that does exercise it.
- Expand a changed matrix and compare the job list before and after. An
  `include` entry can replace a row instead of adding one, and a profile
  marking decides whether a row runs for pull requests at all.
- A new job that must gate merging has to be in the `needs` of
  `build_successful`. Otherwise its failure does not fail the required
  check.
- Look for results that cannot fail: a pipe into `tee`, `|| true`, or a
  step that processes zero inputs and reports success.
- Jobs that hold secrets must not run code from the pull request. The test
  chain consumes build artifacts as data. Give each job only the
  permissions it uses, and pin third-party actions to a commit.
- For kas files, check the merged result with `kas dump` rather than
  reading one file. Later files win, and `local_conf_header` blocks are
  ordered by key.
- For a `ci/base.lock.yml` update, read what each layer bump brings in and
  check the patches this layer carries against it.
- For a test plan or test kit bump, compare the device and test lists with
  `ci/known-failures/`, and judge it from a run that uses the new
  revision.
