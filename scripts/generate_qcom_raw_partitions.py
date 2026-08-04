#!/usr/bin/env python3
# Copyright (c) 2026 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: MIT

"""Generate the reviewed raw-partition match list from qcom-ptool."""

from __future__ import annotations

import argparse
import difflib
import shlex
import subprocess
import sys
from collections import defaultdict
from collections.abc import Set as AbstractSet
from pathlib import Path
from typing import TypeAlias


BEGIN_MARKER = "# BEGIN GENERATED QCOM RAW PARTITIONS"
END_MARKER = "# END GENERATED QCOM RAW PARTITIONS"
MIN_PLATFORM_COUNT = 2
MAX_RULE_LENGTH = 120

# These legacy layouts contain non-slotted names that would otherwise look
# common because both platforms use them. Names shared with current layouts
# are still discovered through those layouts.
EXCLUDED_PLATFORMS = frozenset({
    "apq8016-sbc",  # DragonBoard 410c
    "apq8096-db820c",  # DragonBoard 820c
})

# This is the safety policy for disabling blkid. A name newly appearing in
# qcom-ptool is not necessarily raw, so it must be reviewed and added here
# before the generator can emit it.
RAW_PARTITION_FAMILIES = frozenset({
    "ALIGN_TO_128K_*",
    "SYSFW_VERSION",
    "TZAPPS",
    "abl",
    "aop",
    "aop_config",
    "apdp",
    "catecontentfv",
    "cdt",
    "cmnlib",
    "cmnlib64",
    "cpucp",
    "ddr",
    "devcfg",
    "devinfo",
    "diag_log",
    "dip",
    "dtb",
    "emac",
    "featenabler",
    "fsc",
    "fsg",
    "gearvm",
    "gvm_log",
    "hyp",
    "imagefv",
    "keymaster",
    "limits",
    "limits-cdsp",
    "logdump",
    "modemst1",
    "modemst2",
    "multiimgoem",
    "multiimgqti",
    "pvm_log",
    "qmcs",
    "quantumfv",
    "quantumsdk",
    "questdatafv",
    "qupfw",
    "qweslicstore",
    "recoveryinfo",
    "secdata",
    "shrm",
    "softsku",
    "splash",
    "spunvm",
    "storsec",
    "toolsfv",
    "tz",
    "uefi",
    "uefisecapp",
    "vbmeta",
    "xbl",
    "xbl_config",
    "xbl_logs",
    "xbl_ramdump",
})

# One-off variants are omitted unless their exact pattern is explicitly approved.
# Keep this list small and document why probing is unnecessary.
APPROVED_ONE_OFF_PATTERNS = frozenset({
    "storsec_[ab]",  # Raw storage-security firmware on sdm845-db845c.
    "vbmeta_[ab]",  # Android Verified Boot metadata on qrb5165-rb5.
})

Layout: TypeAlias = tuple[str, str]


class GenerationError(RuntimeError):
    """Raised for invalid input or an unsafe generation request."""


def parse_partition_name(line: str, path: Path, line_number: int) -> str | None:
    """Return --name from a qcom-ptool --partition line."""
    try:
        fields = shlex.split(line, comments=True)
    except ValueError as error:
        raise GenerationError(f"{path}:{line_number}: {error}") from error

    if not fields or fields[0] != "--partition":
        return None

    for index, field in enumerate(fields[1:], start=1):
        if field.startswith("--name="):
            name = field.removeprefix("--name=")
            if name:
                return name
        if field == "--name" and index + 1 < len(fields):
            return fields[index + 1]

    raise GenerationError(f"{path}:{line_number}: partition has no --name")


def collect_partition_layouts(
    qcom_ptool: Path,
) -> dict[str, set[Layout]]:
    """Map each label to the non-excluded platform/storage layouts defining it."""
    platforms_dir = qcom_ptool / "platforms"
    if not platforms_dir.is_dir():
        raise GenerationError(f"qcom-ptool platforms directory not found: {platforms_dir}")

    labels: defaultdict[str, set[Layout]] = defaultdict(set)
    config_paths = sorted(platforms_dir.glob("*/*/partitions.conf"))
    if not config_paths:
        raise GenerationError(f"no platforms/*/*/partitions.conf files in {qcom_ptool}")

    for path in config_paths:
        relative_path = path.relative_to(platforms_dir)
        platform, storage = relative_path.parts[:2]
        if platform in EXCLUDED_PLATFORMS:
            continue
        for line_number, line in enumerate(
            path.read_text(encoding="utf-8").splitlines(), start=1
        ):
            name = parse_partition_name(line, path, line_number)
            if name is not None:
                labels[name].add((platform, storage))

    return dict(labels)


def _is_approved_variant(
    pattern: str,
    layouts: set[Layout],
    approved_one_offs: AbstractSet[str],
) -> bool:
    platforms = {platform for platform, _storage in layouts}
    return len(platforms) >= MIN_PLATFORM_COUNT or pattern in approved_one_offs


def derive_patterns(
    labels: dict[str, set[Layout]],
    families: AbstractSet[str] = RAW_PARTITION_FAMILIES,
    approved_one_offs: AbstractSet[str] = APPROVED_ONE_OFF_PATTERNS,
) -> list[str]:
    """Derive exact and complete A/B patterns from the reviewed policy."""
    patterns: set[str] = set()
    missing_families: list[str] = []

    for family in sorted(families, key=str.casefold):
        family_patterns: set[str] = set()

        if family.endswith("*"):
            prefix = family[:-1]
            layouts = set().union(
                *(sources for name, sources in labels.items() if name.startswith(prefix))
            )
            if layouts and _is_approved_variant(
                family, layouts, approved_one_offs
            ):
                family_patterns.add(family)
        else:
            if family in labels and _is_approved_variant(
                family, labels[family], approved_one_offs
            ):
                family_patterns.add(family)

            slot_a = f"{family}_a"
            slot_b = f"{family}_b"
            if slot_a in labels and slot_b in labels:
                pattern = f"{family}_[ab]"
                paired_layouts = labels[slot_a] & labels[slot_b]
                if paired_layouts and _is_approved_variant(
                    pattern, paired_layouts, approved_one_offs
                ):
                    family_patterns.add(pattern)

        if not family_patterns:
            missing_families.append(family)
        patterns.update(family_patterns)

    if missing_families:
        raise GenerationError(
            "approved families not found on enough included platforms: "
            + ", ".join(missing_families)
        )

    return sorted(patterns, key=str.casefold)


def render_rule_lines(patterns: list[str]) -> list[str]:
    """Pack sorted patterns into deterministic, readable udev rules."""
    prefix = 'ENV{PARTNAME}=="'
    suffix = '", GOTO="qcom_raw_noblkid"'
    lines: list[str] = []
    current: list[str] = []

    for pattern in patterns:
        candidate = "|".join([*current, pattern])
        if current and len(prefix + candidate + suffix) > MAX_RULE_LENGTH:
            lines.append(prefix + "|".join(current) + suffix)
            current = [pattern]
        else:
            current.append(pattern)

    if current:
        lines.append(prefix + "|".join(current) + suffix)
    return lines


def qcom_ptool_revision(qcom_ptool: Path) -> str:
    """Return a reproducible source revision, rejecting dirty platform data."""
    try:
        status = subprocess.run(
            ["git", "-C", str(qcom_ptool), "status", "--porcelain", "--", "platforms"],
            check=True,
            capture_output=True,
            text=True,
        ).stdout
        revision = subprocess.run(
            ["git", "-C", str(qcom_ptool), "rev-parse", "HEAD"],
            check=True,
            capture_output=True,
            text=True,
        ).stdout.strip()
    except (OSError, subprocess.CalledProcessError) as error:
        raise GenerationError(f"cannot read qcom-ptool git revision: {error}") from error

    if status:
        raise GenerationError("qcom-ptool has uncommitted changes under platforms/")
    return revision


def update_generated_section(content: str, revision: str, patterns: list[str]) -> str:
    """Replace the single generated section while preserving hand-written rules."""
    lines = content.splitlines()
    begin = [index for index, line in enumerate(lines) if line == BEGIN_MARKER]
    end = [index for index, line in enumerate(lines) if line == END_MARKER]
    if len(begin) != 1 or len(end) != 1 or begin[0] >= end[0]:
        raise GenerationError("rules file must contain one valid generated section")

    generated = [
        BEGIN_MARKER,
        f"# Source: qcom-ptool commit {revision}",
        *render_rule_lines(patterns),
        END_MARKER,
    ]
    return "\n".join([*lines[: begin[0]], *generated, *lines[end[0] + 1 :]]) + "\n"


def parse_args() -> argparse.Namespace:
    """Parse command-line options."""
    repo_root = Path(__file__).resolve().parents[1]
    default_output = (
        repo_root
        / "recipes-core/systemd/systemd/55-qcom-raw-partitions-noblkid.rules"
    )
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument(
        "--qcom-ptool",
        required=True,
        type=Path,
        help="path to a clean qcom-ptool git checkout",
    )
    parser.add_argument("--output", type=Path, default=default_output)
    parser.add_argument(
        "--check",
        action="store_true",
        help="fail instead of updating an out-of-date output file",
    )
    return parser.parse_args()


def main() -> int:
    """Generate or check the configured rules file."""
    args = parse_args()
    try:
        labels = collect_partition_layouts(args.qcom_ptool.resolve())
        patterns = derive_patterns(labels)
        revision = qcom_ptool_revision(args.qcom_ptool.resolve())
        old_content = args.output.read_text(encoding="utf-8")
        new_content = update_generated_section(old_content, revision, patterns)
    except (GenerationError, OSError) as error:
        print(f"error: {error}", file=sys.stderr)
        return 2

    if old_content == new_content:
        print(f"up to date: {args.output}")
        return 0

    if args.check:
        sys.stdout.writelines(
            difflib.unified_diff(
                old_content.splitlines(keepends=True),
                new_content.splitlines(keepends=True),
                fromfile=str(args.output),
                tofile=f"{args.output} (generated)",
            )
        )
        return 1

    args.output.write_text(new_content, encoding="utf-8")
    print(f"updated: {args.output}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
