# Known test failures

Each build variant tested by `.github/workflows/test.yml` has its own list of
known failures in this directory. The file name is
`<distro_name><distro_suffix>.yaml`, i.e. the same value that is used to name
the test job summary:

| Build variant               | File                               |
| --------------------------- | ---------------------------------- |
| nodistro                    | `nodistro.yaml`                    |
| qcom-distro                 | `qcom-distro.yaml`                 |
| qcom-distro_linux-qcom-6.18 | `qcom-distro_linux-qcom-6.18.yaml` |

## Format

The file is a YAML mapping of LAVA device type to the list of tests that are
expected to fail on that device. Every entry carries the test name and a
`comment` pointing at the issue the failure is tracked in:

```yaml
qcm6490-idp:
  - test: some_failing_test
    comment: https://github.com/qualcomm-linux/meta-qcom/issues/1234
  - test: another_failing_test
    comment: "waiting for the firmware uprev, issue #1235"

# "*" applies to every device tested in this variant
"*":
  - test: test_failing_everywhere
    comment: https://github.com/qualcomm-linux/meta-qcom/issues/1236
```

`comment` is free text. A link to the reported issue is what makes the list
reviewable, so add one for every entry. Quote a comment that contains a `#`,
otherwise YAML treats the rest of the line as a comment of its own. A bare test
name is also accepted for an entry that has nothing to say:

```yaml
qcm6490-idp:
  - some_failing_test
```

The test name is the name reported by LAVA, i.e. the value shown in the first
column of the test job summary table. The device type is the LAVA
`requested_device_type`, i.e. the column header of that table. When a test is
listed both for a device and under `"*"`, the device entry and its comment win.

An empty file (or a file containing only comments) means that no failure is
known for that variant.

## How the list is used

Two consumers apply the list.

### The test job summary

`.github/actions/test-job-summary` applies the list when it renders the summary
table of a build variant:

| Result in LAVA | Listed as known failure | Reported as                             |
| -------------- | ----------------------- | --------------------------------------- |
| `fail`         | no                      | :x: `fail`                              |
| `fail`         | yes                     | :ballot_box_with_check: `known failure` |
| `pass`         | no                      | :white_check_mark: `pass`               |
| `pass`         | yes                     | :x: `unexpected pass`                   |

A known failure is counted like a pass, so it does not add to the failure
count. A test that passes while it is listed as a known failure is counted as
a failure - that is the signal to remove the entry from this list.

Every entry that was applied is listed with its comment in a "Known failures"
section below the table.

The summary also reports the result of the boot test under the name `boot`, so
`boot` can be listed here like any other test name.

### The "Test Results" check

`.github/workflows/publish-results.yml` publishes the JUnit XML files that LAVA
produced for every test job. Before they are published,
`.github/scripts/apply-known-failures.py` rewrites them in place: a known
failure becomes a skipped test, so it no longer fails the check, and a test
that passes while it is listed becomes a failure. The comment of the entry is
appended to the message of the rewritten test, so the issue is one click away
in the check report. The script maps a result file to a variant and a device
through the file name, which lava-test-plans builds as
`<prefix>-<variant>-<device>-<job>.yaml`.

The XML files only contain the tests that ran inside a LAVA job, so a `boot`
entry has no effect on this check.

## Which lists are used

Both consumers apply the lists of the pull request under test, not the ones
already merged, so a pull request that fixes a listed failure removes the entry
in the same change, and a pull request that hits a new one can list it right
away.

The test chain of a pull request runs on the `workflow_run` event, so its own
checkout is the base branch. It therefore checks this directory out a second
time from the branch or fork the pull request is built from, into
`known-failures-pr/`, with a sparse checkout that fetches nothing else. Those
lists are only ever read as data: the scripts applying them, and everything
else the chain runs, still come from the base branch.

Before they are applied to the "Test Results" check the lists are checked with
`--validate --syntax-only`, i.e. for their syntax alone - which variants exist
is a property of the base branch, and it is `known-failures.yml`, running on
the pull request itself, that checks the lists against them. A list that does
not parse, or a fork that is no longer reachable, falls back to the lists of
the base branch. The run log says which lists were applied.

A push, a nightly build and a manual run have no pull request to take lists
from and simply use the ones of their own checkout.

## Validation

`.github/workflows/known-failures.yml` validates the lists on every change to
this directory, to the script, or to the test workflow. It rejects:

* a file that is not valid YAML, or that does not follow the format above,
* a test listed twice for the same device,
* a file whose name is not one of the build variants tested by
  `.github/workflows/test.yml`, and a variant that has no file at all,
* a device that the variant does not test.

The last two matter because such an entry is not an error at test time, it is
simply never applied - the list would look like it suppresses a failure while
the check stays red. An entry without a comment is reported as a warning.

Run the same check locally with:

```shell
python3 .github/scripts/apply-known-failures.py --validate
```

Add `--syntax-only` to check the format of the lists without checking them
against the build variants of this branch. That is what the test chain uses on
the lists it takes from a pull request.
