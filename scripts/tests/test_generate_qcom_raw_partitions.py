# Copyright (c) 2026 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: MIT

"""Unit tests for the qcom raw-partition rules generator."""

import importlib.util
import tempfile
import unittest
from pathlib import Path


SCRIPT = Path(__file__).parents[1] / "generate_qcom_raw_partitions.py"
SPEC = importlib.util.spec_from_file_location("raw_partition_generator", SCRIPT)
assert SPEC is not None and SPEC.loader is not None
generator = importlib.util.module_from_spec(SPEC)
SPEC.loader.exec_module(generator)


class GeneratorTests(unittest.TestCase):
    """Verify provenance, policy, rendering, and file updates."""

    def test_collects_platform_provenance_and_excludes_legacy_layouts(self):
        """Excluded platforms must not contribute partition labels."""
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            for platform, label in (
                ("platform-a", "firmware_a"),
                ("platform-b", "firmware_a"),
                ("apq8016-sbc", "legacy"),
            ):
                path = root / "platforms" / platform / "ufs" / "partitions.conf"
                path.parent.mkdir(parents=True)
                path.write_text(
                    f"--disk --type=ufs --size=1GB\n"
                    f"--partition --name={label} --size=1KB\n",
                    encoding="utf-8",
                )

            self.assertEqual(
                generator.collect_partition_layouts(root),
                {"firmware_a": {("platform-a", "ufs"), ("platform-b", "ufs")}},
            )

    def test_render_packs_patterns_without_exceeding_limit(self):
        """Rendered udev rules must remain within the configured limit."""
        patterns = ["alpha_[ab]", "beta_[ab]", "gamma", "omega"]
        lines = generator.render_rule_lines(patterns)

        self.assertTrue(all(len(line) <= generator.MAX_RULE_LENGTH for line in lines))
        self.assertIn("alpha_[ab]|beta_[ab]|gamma|omega", "\n".join(lines))

    def test_omits_one_off_variant_unless_explicitly_approved(self):
        """Approval must apply only to the exact one-off pattern."""
        labels = {
            "common_a": {("platform-a", "ufs"), ("platform-b", "ufs")},
            "common_b": {("platform-a", "ufs"), ("platform-b", "ufs")},
            "special": {("platform-a", "ufs")},
            "special_a": {("platform-a", "ufs")},
            "special_b": {("platform-a", "ufs")},
        }

        with self.assertRaisesRegex(generator.GenerationError, "special"):
            generator.derive_patterns(labels, {"common", "special"}, set())

        self.assertEqual(
            generator.derive_patterns(
                labels, {"common", "special"}, {"special_[ab]"}
            ),
            ["common_[ab]", "special_[ab]"],
        )

    def test_does_not_pair_slots_from_different_storage_layouts(self):
        """A/B slots in different storage layouts must not be collapsed."""
        labels = {
            "firmware_a": {
                ("platform-a", "emmc"),
                ("platform-b", "emmc"),
            },
            "firmware_b": {
                ("platform-a", "ufs"),
                ("platform-b", "ufs"),
            },
        }

        with self.assertRaisesRegex(generator.GenerationError, "firmware"):
            generator.derive_patterns(labels, {"firmware"}, set())

    def test_update_replaces_only_generated_section(self):
        """Updating must preserve content outside the generated markers."""
        content = "\n".join(
            (
                "before",
                generator.BEGIN_MARKER,
                "old",
                generator.END_MARKER,
                "after",
                "",
            )
        )

        updated = generator.update_generated_section(content, "abc123", ["xbl_[ab]"])

        self.assertEqual(
            updated,
            "\n".join(
                (
                    "before",
                    generator.BEGIN_MARKER,
                    "# Source: qcom-ptool commit abc123",
                    'ENV{PARTNAME}=="xbl_[ab]", GOTO="qcom_raw_noblkid"',
                    generator.END_MARKER,
                    "after",
                    "",
                )
            ),
        )


if __name__ == "__main__":
    unittest.main()
