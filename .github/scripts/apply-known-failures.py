#!/usr/bin/env python3
"""Apply the known failures lists to the LAVA JUnit XML result files.

The LAVA JUnit files are published as a check by publish-unit-test-result-action,
which fails the workflow on any <failure>. Without this script a failure that is
already known and accepted for a build variant would keep that check red forever.

Every build variant has its own list of known failures in
.github/known-failures/<variant>.yaml, see the README in that directory. This
script rewrites the result files in place so that:

  * a failing test that is on the list becomes <skipped> ("known failure"), so
    it no longer fails the check but stays visible in the report,
  * a passing test that is on the list becomes <failure> ("unexpected pass"),
    which is the signal that the entry has to be removed from the list.

The variant and the device a result file belongs to are taken from its name.
lava-test-plans names the result files "<prefix>-<variant>-<device>-<job>.yaml"
(see .github/actions/lava-test-plans/action.yml), and lava-action saves them
with an .xml suffix under an artifact directory of the same name.

With --validate the script does not touch any result file. It only checks the
lists themselves: their syntax, and that every file and every device in them
matches a build variant that the test workflow really tests. An entry that names
a variant or a device that is not tested would silently never be applied. Add
--syntax-only to check the syntax alone, which is all that can be asked of lists
that do not come from this branch.
"""

import argparse
import os
import pathlib
import sys
import xml.etree.ElementTree as ET

import yaml

# suite holding the LAVA infrastructure steps rather than the actual tests
LAVA_SUITE = "lava"

# workflow calling test-distro.yml once per tested build variant
TEST_WORKFLOW = ".github/workflows/test.yml"

# device type standing for every device tested in a variant
ANY_DEVICE = "*"


def annotation(path, message):
    """Format a message as a GitHub annotation when running in a workflow."""
    if os.environ.get("GITHUB_ACTIONS") == "true":
        return f"::error file={path}::{message}"
    return f"{path}: {message}"


def fail(path, message):
    sys.exit(annotation(path, message))


def load_entry(path, device, entry):
    """Return (test name, comment) for one entry of a known failures list.

    An entry is either a bare test name or a mapping with a "test" and an
    optional "comment" naming the issue the failure is tracked in.
    """
    if isinstance(entry, str):
        return entry, ""
    if not isinstance(entry, dict):
        fail(path, f"{device}: expected a test name or a mapping, got {entry!r}")
    unknown = set(entry) - {"test", "comment"}
    if unknown:
        fail(path, f"{device}: unknown key(s) {', '.join(sorted(unknown))}")
    test = entry.get("test")
    if not isinstance(test, str) or not test:
        fail(path, f"{device}: entry {entry!r} is missing a test name")
    return test, str(entry.get("comment", ""))


def load_known_failures(known_failures_dir):
    """Return {variant: {device: {test name: comment}}} for every list found."""
    known_failures = {}
    for path in sorted(pathlib.Path(known_failures_dir).glob("*.yaml")):
        try:
            content = yaml.safe_load(path.read_text()) or {}
        except yaml.YAMLError as error:
            fail(path, f"not valid YAML: {error}")
        if not isinstance(content, dict):
            fail(path, "expected a mapping of device to list of tests")
        variant = {}
        for device, entries in content.items():
            if not isinstance(entries, list):
                fail(path, f"{device}: expected a list of test names")
            tests = {}
            for entry in entries:
                test, comment = load_entry(path, device, entry)
                if test in tests:
                    fail(path, f"{device}: {test} is listed more than once")
                tests[test] = comment
            variant[str(device)] = tests
        known_failures[path.stem] = variant
    return known_failures


def match_result_file(name, known_failures):
    """Return the {test name: comment} of the known failures of a result file.

    The longest matching variant wins so that "qcom-distro_linux-qcom-6.18" is
    not shadowed by "qcom-distro". Returns None when the file does not belong to
    any variant that has a known failures list.
    """
    for variant in sorted(known_failures, key=len, reverse=True):
        marker = f"-{variant}-"
        if marker not in name:
            continue
        devices = known_failures[variant]
        # everything after the variant is "<device>-<job file name>"
        remainder = name.split(marker, 1)[1]
        tests = dict(devices.get("*", {}))
        for device, device_tests in devices.items():
            # a device specific entry overrides the one listed for all devices
            if device != "*" and remainder.startswith(f"{device}-"):
                tests.update(device_tests)
        return tests
    return None


def annotate(message, comment):
    """Append the comment of a known failures entry to a JUnit message."""
    return f"{message} ({comment})" if comment else message


def apply_to_testcase(testcase, suite_name, comment):
    """Rewrite a single testcase. Returns a description of the change or None."""
    name = testcase.get("name")
    failure = testcase.find("failure")
    if failure is None:
        failure = testcase.find("error")
    if failure is not None:
        # known failure: report it as skipped so it does not fail the check
        original = failure.get("message", "failed")
        testcase.remove(failure)
        skipped = ET.SubElement(testcase, "skipped")
        skipped.set("type", "known failure")
        skipped.set(
            "message",
            annotate(
                "known failure: listed in .github/known-failures, "
                f"original result: {original}",
                comment,
            ),
        )
        return f"{suite_name}/{name}: fail -> known failure (skipped)"
    if testcase.find("skipped") is not None:
        # the test did not run, nothing to say about the known failure
        return None
    # the test passed although it is expected to fail
    failure = ET.SubElement(testcase, "failure")
    failure.set("type", "unexpected pass")
    failure.set(
        "message",
        annotate(
            "unexpected pass: the test is listed as a known failure in "
            ".github/known-failures, remove it from the list",
            comment,
        ),
    )
    return f"{suite_name}/{name}: pass -> unexpected pass (failure)"


def count(testsuite, tag):
    return len(testsuite.findall(f"./testcase/{tag}"))


def refresh_counters(testsuites):
    """Recompute the counters of every testsuite and of the root element."""
    totals = {"tests": 0, "failures": 0, "errors": 0, "skipped": 0}
    for testsuite in testsuites.findall("testsuite"):
        counters = {
            "tests": len(testsuite.findall("testcase")),
            "failures": count(testsuite, "failure"),
            "errors": count(testsuite, "error"),
            "skipped": count(testsuite, "skipped"),
        }
        for key, value in counters.items():
            testsuite.set(key, str(value))
            totals[key] += value
    for key, value in totals.items():
        # the root element of the LAVA JUnit output carries no "skipped" counter
        if key != "skipped" or "skipped" in testsuites.attrib:
            testsuites.set(key, str(value))


def process(path, known_tests):
    """Apply known_tests to one result file. Returns the list of changes."""
    tree = ET.parse(path)
    testsuites = tree.getroot()
    changes = []
    for testsuite in testsuites.findall("testsuite"):
        suite_name = testsuite.get("name", "")
        if suite_name == LAVA_SUITE:
            continue
        for testcase in testsuite.findall("testcase"):
            name = testcase.get("name")
            if name in known_tests:
                change = apply_to_testcase(testcase, suite_name, known_tests[name])
                if change:
                    changes.append(change)
    if changes:
        refresh_counters(testsuites)
        tree.write(path, encoding="utf-8", xml_declaration=True)
    return changes


def load_tested_variants(workflow_path):
    """Return {variant: set(devices)} for every variant tested by the workflow.

    The build variants are the jobs of the test workflow that call
    test-distro.yml, named after the distro and its suffix, exactly like the
    known failures lists.
    """
    workflow = yaml.safe_load(pathlib.Path(workflow_path).read_text())
    variants = {}
    for job in workflow.get("jobs", {}).values():
        if not str(job.get("uses", "")).endswith("test-distro.yml"):
            continue
        inputs = job.get("with", {})
        variant = f"{inputs.get('distro_name', '')}{inputs.get('distro_suffix', '')}"
        devices = set()
        for key in ("devices", "devices_premerge"):
            devices |= {
                device.strip()
                for device in str(inputs.get(key, "")).split(",")
                if device.strip()
            }
        variants[variant] = devices
    return variants


def validate(known_failures_dir, known_failures, workflow_path):
    """Check the lists against the build variants the test workflow tests."""
    variants = load_tested_variants(workflow_path)
    if not variants:
        sys.exit(f"{workflow_path}: no build variant found, is it still the test workflow?")
    print(f"{workflow_path} tests {len(variants)} build variant(s): {', '.join(sorted(variants))}")

    errors = []
    for variant in sorted(set(variants) - set(known_failures)):
        errors.append(
            annotation(
                pathlib.Path(known_failures_dir) / f"{variant}.yaml",
                f"missing known failures list for the {variant} build variant, "
                "add the file even when it holds no entry",
            )
        )
    for variant in sorted(set(known_failures) - set(variants)):
        errors.append(
            annotation(
                pathlib.Path(known_failures_dir) / f"{variant}.yaml",
                f"{variant} is not a build variant tested by {workflow_path}, "
                "the entries of this file would never be applied. Tested: "
                f"{', '.join(sorted(variants))}",
            )
        )
    for variant, devices in sorted(known_failures.items()):
        for device in sorted(set(devices) - {ANY_DEVICE} - variants.get(variant, set())):
            errors.append(
                annotation(
                    pathlib.Path(known_failures_dir) / f"{variant}.yaml",
                    f"{device} is not tested in the {variant} build variant, the "
                    "entries listed for it would never be applied",
                )
            )
        for device, tests in sorted(devices.items()):
            for test, comment in sorted(tests.items()):
                if not comment:
                    print(
                        f"::warning file={known_failures_dir}/{variant}.yaml::"
                        f"{device}: {test} has no comment, add the issue it is "
                        "tracked in"
                    )

    for error in errors:
        print(error)
    if errors:
        sys.exit(f"{len(errors)} error(s) in {known_failures_dir}")
    print(f"{known_failures_dir}: all lists are valid")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument(
        "--known-failures-dir",
        default=".github/known-failures",
        help="directory holding the per variant known failures lists",
    )
    parser.add_argument(
        "--results-dir",
        help="directory holding the downloaded LAVA JUnit result files",
    )
    parser.add_argument(
        "--validate",
        action="store_true",
        help="only check the known failures lists, do not touch any result file",
    )
    parser.add_argument(
        "--test-workflow",
        default=TEST_WORKFLOW,
        help="workflow the tested build variants are read from with --validate",
    )
    parser.add_argument(
        "--syntax-only",
        action="store_true",
        help="with --validate, only check that the lists are well formed, not "
        "that they match the build variants of the test workflow",
    )
    args = parser.parse_args()
    if not args.validate and not args.results_dir:
        parser.error("either --results-dir or --validate is required")

    known_failures = load_known_failures(args.known_failures_dir)
    if not known_failures:
        sys.exit(f"no known failures list found in {args.known_failures_dir}")
    for variant, devices in sorted(known_failures.items()):
        listed = sum(len(tests) for tests in devices.values())
        print(f"{variant}: {listed} known failure(s) listed")

    if args.validate:
        # The syntax is a property of the lists themselves, the build variants
        # are a property of this branch. Lists coming from somewhere else - the
        # branch or fork a pull request is built from - are only checked for
        # syntax, so that they can add or drop a variant without being rejected.
        if not args.syntax_only:
            validate(args.known_failures_dir, known_failures, args.test_workflow)
        return

    total = 0
    for path in sorted(pathlib.Path(args.results_dir).rglob("*.xml")):
        known_tests = match_result_file(path.name, known_failures)
        if not known_tests:
            continue
        for change in process(path, known_tests):
            print(f"{path.name}: {change}")
            total += 1
    print(f"applied the known failures lists to {total} test result(s)")


if __name__ == "__main__":
    main()
