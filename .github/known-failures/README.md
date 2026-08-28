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

## Which lists are used

The list that is applied is the one of the pull request under test, not the one
already merged, so a pull request that fixes a listed failure removes the entry
in the same change, and a pull request that hits a new one can list it right
away.

The test chain of a pull request runs on the `workflow_run` event, so its own
checkout is the base branch. It therefore checks this directory out a second
time from the branch or fork the pull request is built from, into
`known-failures-pr/`, with a sparse checkout that fetches nothing else. Those
lists are only ever read as data: everything the chain runs still comes from
the base branch. A fork that is no longer reachable falls back to the list of
the base branch, and the run log says which of the two was applied.

A push, a nightly build and a manual run have no pull request to take a list
from and simply use the one of their own checkout.
