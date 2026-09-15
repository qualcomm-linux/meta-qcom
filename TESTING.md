# Testing

Images built by CI are booted and tested on real hardware in a
[LAVA](https://lava.infra.foundries.io) lab. This document describes which
build variants are tested, where the results are reported, and how to use the
known failures lists to keep an accepted failure from turning the results red.

## When tests run

| Trigger                  | Workflow                             | Notes                                                                      |
| ------------------------ | ------------------------------------ | -------------------------------------------------------------------------- |
| Pull request to `master` | `pr.yml` builds, `test-pr.yml` tests | The test chain runs on `workflow_run` so that the build stays unprivileged |
| Push to `master`         | `push.yml`                           | Builds, tests and publishes in one run                                     |
| Nightly                  | `nightly-build.yml`                  | Scheduled at 00:22 UTC, Sunday to Friday                                   |
| Weekly                   | `weekly-build.yml`                   | Saturday's slot, building a superset of the nightly matrix                 |

All four end up in the same place: `test.yml` runs one `test-distro.yml` job
per tested build variant, and `publish-results.yml` publishes the results.

## Tested build variants

CI builds many more distro and kernel combinations than it tests. Only the
three variants below are booted and tested on hardware. A variant is named
after the distro and the kernel directory, `<distro_name><distro_suffix>`, and
that name is used consistently for its build artifacts, its test job summary
and its known failures list.

| Build variant                 | Distro                        | Kernel                                           | Image                   | Known failures list                                       |
| ----------------------------- | ----------------------------- | ------------------------------------------------ | ----------------------- | --------------------------------------------------------- |
| `nodistro`                    | plain OpenEmbedded, no distro | default                                          | `core-image-base`       | `.github/known-failures/nodistro.yaml`                    |
| `qcom-distro`                 | `ci/qcom-distro.yml`          | default                                          | `qcom-multimedia-image` | `.github/known-failures/qcom-distro.yaml`                 |
| `qcom-distro_linux-qcom-6.18` | `ci/qcom-distro.yml`          | `linux-qcom` 6.18, from `ci/linux-qcom-6.18.yml` | `qcom-multimedia-image` | `.github/known-failures/qcom-distro_linux-qcom-6.18.yaml` |

To change the set of variants, or the devices a variant runs on, edit the
`test-*` jobs in `.github/workflows/test.yml`. A new variant needs a known
failures list of its own, even an empty one - the validation workflow described
below fails until it exists.

### Devices per variant

| Device             | `nodistro` | `qcom-distro`    | `qcom-distro_linux-qcom-6.18` |
| ------------------ | ---------- | ---------------- | ----------------------------- |
| `dragonboard-410c` | boot       | -                | -                             |
| `dragonboard-820c` | boot       | -                | -                             |
| `glymur-crd`       | -          | boot + pre-merge | -                             |
| `iq-8275-evk`      | boot       | boot + pre-merge | boot + pre-merge              |
| `iq-9075-evk`      | boot       | boot + pre-merge | boot + pre-merge              |
| `iq-x7181-evk`     | boot       | boot + pre-merge | boot + pre-merge              |
| `kaanapali-mtp`    | -          | boot + pre-merge | -                             |
| `qcm6490-idp`      | boot       | boot + pre-merge | boot + pre-merge              |
| `qcs615-ride`      | boot       | boot + pre-merge | boot + pre-merge              |
| `qcs8300-ride-sx`  | boot       | boot + pre-merge | boot + pre-merge              |
| `qcs9100-ride-sx`  | boot       | boot + pre-merge | boot + pre-merge              |
| `rb1-core-kit`     | boot       | boot + pre-merge | boot + pre-merge              |
| `rb3gen2-core-kit` | boot       | boot + pre-merge | boot + pre-merge              |
| `shikra-evk`       | -          | boot             | -                             |
| `sm8750-mtp`       | -          | boot + pre-merge | -                             |

The device name is the LAVA device type. It is what a known failures list keys
on, and what the columns of the test job summary are named after.

## Test stages

Each variant runs in two stages:

1. **boot** - one LAVA job per device that flashes the image and boots it. The
   summary reports the outcome as a test named `boot`.
2. **pre-merge** - the actual test suites, run only when every boot job of the
   variant passed. A variant with no `devices_premerge` never reaches this
   stage.

The test jobs themselves come from two pinned external repositories, both
referenced at the top of `.github/workflows/test.yml`:

- [`qualcomm-linux/lava-test-plans`](https://github.com/qualcomm-linux/lava-test-plans)
  (`LAVA_TEST_PLANS_REF`) renders the LAVA job definitions from the
  `meta-qcom/<distro>/boot` and `meta-qcom/<distro>/pre-merge` test plans.
- [`qualcomm-linux/qcom-linux-testkit`](https://github.com/qualcomm-linux/qcom-linux-testkit)
  (`TESTKIT_REF`) holds the test scripts that run on the device.

Adding or changing a test case is done in those repositories; this repository
only pins the revision to use.

## Where results are reported

- **Test job summary** - one collapsible section per variant on the workflow
  run summary page, with a test-by-device table, a "Known failures" section and
  the list of all LAVA jobs with links.
- **"Test Results" check** - published from the JUnit XML that LAVA produced,
  by `publish-results.yml`. This is the check that turns a pull request red.
- **PR comment** - a single comment per pull request, updated in place.

## Known failures

A test that is known to fail, and whose failure is accepted for now, can be
listed as a *known failure*. It then no longer fails the build, but it stays
visible in the reports, and it starts failing again as soon as it passes - so
the list cannot silently rot.

Every build variant has its own list in `.github/known-failures/`, named after
the variant. Full details are in
[`.github/known-failures/README.md`](.github/known-failures/README.md); what
follows is what you need to use them.

### Adding a known failure

1. Find the failing test in the test job summary: the row gives the test name,
   the column gives the device type.
2. Open the list of the variant, e.g. `.github/known-failures/qcom-distro.yaml`
   for the `qcom-distro` section of the summary.
3. Add an entry under the device, with a link to the issue it is tracked in:

   ```yaml
   qcm6490-idp:
     - test: AudioRecord_Config01
       comment: https://github.com/qualcomm-linux/meta-qcom/issues/1234
   ```

   Use `"*"` instead of a device name when the test fails on every device of
   the variant. A device entry overrides the `"*"` entry for the same test.
   Quote a comment containing a `#`, otherwise YAML swallows the rest of the
   line.
4. Run `python3 .github/scripts/apply-known-failures.py --validate` before
   pushing.

The entry takes effect on the pull request that adds it: the test chain checks
the lists out from the branch or fork the pull request is built from, so a
pull request that fixes a listed failure deletes the entry in the same change
and stays green. The lists of the base branch are used only when the pull
request has none, or when the ones it has do not parse - the run log says which
of the two was applied.

### What it changes

| Result in LAVA | Listed | Test job summary                        | "Test Results" check                       |
| -------------- | ------ | --------------------------------------- | ------------------------------------------ |
| `fail`         | no     | :x: `fail`                              | failed                                     |
| `fail`         | yes    | :ballot_box_with_check: `known failure` | skipped, with the comment in the message   |
| `pass`         | no     | :white_check_mark: `pass`               | passed                                     |
| `pass`         | yes    | :x: `unexpected pass`                   | failed, asking for the entry to be removed |

A known failure counts as a pass in the summary totals, and is reported as a
skipped test in the check, so neither turns red because of it.

### Removing a known failure

When the underlying issue is fixed, the test starts passing while it is still
listed. Both reports then flag it as an *unexpected pass* and the check goes
red. Delete the entry (and close the issue it points at) to make it green
again. This is deliberate: it is what stops the lists from growing forever.

### Validation

`.github/workflows/known-failures.yml` validates the lists on every change to
them, to the script, or to `test.yml`. Besides the syntax it checks that every
list matches a tested variant and every device is one the variant actually
tests, because such an entry is never applied and would otherwise look like it
suppressed a failure. Run the same check locally with:

```shell
python3 .github/scripts/apply-known-failures.py --validate
```

### Limitations

- A `boot` entry only affects the test job summary. The JUnit XML holds the
  tests that ran inside a LAVA job, so it has nothing to suppress for a boot
  failure, and the boot job itself still fails the workflow.
- The lists apply to the variants of this branch. The `wrynose` branch runs its
  own test workflow with its own variants and keeps its own lists.
- Only the syntax of the lists taken from a pull request is checked before they
  are applied. That they name a tested variant and a tested device is checked by
  `known-failures.yml` on the pull request itself, against the variants of the
  branch it targets.

## Reproducing a failure

Every entry of the summary table links into the log of its LAVA job, at the
line the result was reported on. The surrounding lines hold the output of the
test itself, and the rest of the page the serial console log and the exact job
definition, which is the fastest way to tell a real regression from lab
flakiness. A boot entry links to the top of the log, there being no test case
to point at. A job can be resubmitted from that page to check whether a failure
is reproducible.
