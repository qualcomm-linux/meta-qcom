#
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
#
# SPDX-License-Identifier: BSD-3-Clause-Clear
#
# Test cases for the wic layout generated from the qcom-ptool partitions.xml
# (lib/qcom/partitions_wks.py, used by image_types_qcom.bbclass).
#

import glob
import os
import shlex
import tempfile

from oeqa.selftest.case import OESelftestTestCase
from oeqa.utils.commands import bitbake, get_bb_var

PARTITIONS_XML = """<?xml version="1.0" ?>
<configuration>
    <parser_instructions>
    SECTOR_SIZE_IN_BYTES={sector_size}
    GROW_LAST_PARTITION_TO_FILL_DISK={grow}
    ALIGN_PARTITIONS_TO_PERFORMANCE_BOUNDARY=true
    PERFORMANCE_BOUNDARY_IN_KB=4
    </parser_instructions>
    <physical_partition>
        <partition label="xbl_a" size_in_kb="4096" type="DEA0BA2C-CBDD-4805-B4F9-F428251C3E98" uniqueguid="0B3D5A2C-1E4F-4A6B-8C9D-0E1F2A3B4C5D" readonly="true" filename="xbl.elf" sparse="false"/>
        <partition label="{misc_label}" size_in_kb="1024" type="82ACC91F-357C-4A68-9C8F-689E1B1A23A1" readonly="true" filename="" sparse="false" active="true" successful="true"/>
        {esp}
        <partition label="rootfs" size_in_kb="16777216" type="B921B045-1DF0-41C3-AF44-4C6F280D3FAE" readonly="true" filename="rootfs.img" sparse="false"/>
    </physical_partition>
    <physical_partition>
        <partition label="cdt" size_in_kb="128" type="A19F205F-CCD8-4B6D-8F1E-2D9BC24CFFB1" readonly="true" filename="cdt.bin" sparse="false"/>
        <partition label="last_parti" size_in_kb="0" type="00000000-0000-0000-0000-000000000000" readonly="true" filename="" sparse="false"/>
    </physical_partition>
</configuration>
"""

ESP_PARTITION = '<partition label="efi" size_in_kb="524288" type="C12A7328-F81F-11D2-BA4B-00A0C93EC93B" readonly="true" filename="efi.bin" sparse="false"/>'


def create_files(file_dir, names):
    for name in names:
        open(os.path.join(file_dir, name), "w").close()


def parse_wks(wks):
    """Return {part-name: {option: value}} for the part lines of a wks."""
    parts = {}
    for line in wks.splitlines():
        args = shlex.split(line)
        if not args or args[0] != "part":
            continue
        options = {}
        for i, arg in enumerate(args):
            if not arg.startswith("--"):
                continue
            if "=" in arg:
                key, value = arg.split("=", 1)
                options[key] = value
            else:
                options[arg] = args[i + 1] if i + 1 < len(args) and not args[i + 1].startswith("--") else None
        parts[options["--part-name"]] = options
    return parts


class QcomPartitionsWksTests(OESelftestTestCase):
    """Unit tests for the partitions.xml to wks conversion."""

    def generate(self, sector_size=512, grow="true", misc_label="misc", esp=False,
                 files=("xbl.elf", "rootfs.img", "cdt.bin", "efi.bin")):
        from qcom.partitions_wks import generate_wks

        tmpdir = tempfile.mkdtemp(dir=self.builddir)
        self.track_for_cleanup(tmpdir)
        xml = os.path.join(tmpdir, "partitions.xml")
        with open(xml, "w") as f:
            f.write(PARTITIONS_XML.format(sector_size=sector_size, grow=grow, misc_label=misc_label,
                                          esp=ESP_PARTITION if esp else ""))
        create_files(tmpdir, files)
        return generate_wks(xml, tmpdir)

    def test_boot_area_partitions_before_esp(self):
        for grow in ("true", "false"):
            wks, _ = self.generate(grow=grow, esp=True)
            self.assertEqual(list(parse_wks(wks)), ["xbl_a", "misc", "cdt", "efi", "rootfs"])

    def test_boot_area_partitions_before_grown_partition(self):
        wks, _ = self.generate()
        self.assertEqual(list(parse_wks(wks)), ["xbl_a", "misc", "cdt", "rootfs"])
        self.assertTrue(wks.startswith("bootloader --ptable gpt\n"))

    def test_boot_area_partitions_appended_without_grow(self):
        wks, _ = self.generate(grow="false")
        self.assertEqual(list(parse_wks(wks)), ["xbl_a", "misc", "rootfs", "cdt"])
        self.assertEqual(parse_wks(wks)["rootfs"]["--fixed-size"], "16777216K")

    def test_partition_options(self):
        wks, _ = self.generate()
        parts = parse_wks(wks)
        self.assertEqual(parts["xbl_a"]["--part-type"], "DEA0BA2C-CBDD-4805-B4F9-F428251C3E98")
        self.assertEqual(parts["xbl_a"]["--fixed-size"], "4096K")
        self.assertEqual(parts["xbl_a"]["--align"], "4")
        self.assertEqual(parts["xbl_a"]["--uuid"], "0B3D5A2C-1E4F-4A6B-8C9D-0E1F2A3B4C5D")
        self.assertNotIn("--uuid", parts["misc"])
        self.assertEqual(parts["xbl_a"]["--source"], "rawcopy")
        self.assertTrue(parts["xbl_a"]["--sourceparams"].endswith("/xbl.elf"))
        self.assertEqual(parts["misc"]["--source"], "empty")
        self.assertEqual(parts["misc"]["--fixed-size"], "1024K")

    def test_grown_partition_has_no_fixed_size(self):
        wks, _ = self.generate()
        self.assertNotIn("--fixed-size", parse_wks(wks)["rootfs"])

    def test_gpt_attributes(self):
        _, attributes = self.generate()
        # read-only is bit 60, active bit 50 and successful bit 54
        self.assertEqual(attributes, [
            (1, "GUID:60"),
            (2, "GUID:50,GUID:54,GUID:60"),
            (3, "GUID:60"),
            (4, "GUID:60"),
        ])

    def test_sfdisk_attribute_names(self):
        from qcom.partitions_wks import gpt_attributes, sfdisk_attributes, PartitionsWksError
        attrs = gpt_attributes({"system": "true", "priority": "3", "triesremaining": "7"})
        self.assertEqual(sfdisk_attributes(attrs), "RequiredPartition,GUID:48,GUID:49,GUID:51,GUID:52,GUID:53")
        self.assertEqual(sfdisk_attributes(0b110), "NoBlockIOProtocol,LegacyBIOSBootable")
        with self.assertRaisesRegex(PartitionsWksError, "reserved GPT attribute bit 3"):
            sfdisk_attributes(1 << 3)

    def test_rejects_invalid_layout(self):
        from qcom.partitions_wks import generate_wks, PartitionsWksError
        tmpdir = tempfile.mkdtemp(dir=self.builddir)
        self.track_for_cleanup(tmpdir)
        xml = os.path.join(tmpdir, "partitions.xml")
        with open(xml, "w") as f:
            f.write("<configuration><physical_partition>")
        with self.assertRaisesRegex(PartitionsWksError, "invalid layout .*ParseError"):
            generate_wks(xml, tmpdir)
        with open(xml, "w") as f:
            f.write('<configuration><physical_partition><partition label="a" size_in_kb="x" type="1"/>'
                    '</physical_partition></configuration>')
        with self.assertRaisesRegex(PartitionsWksError, "invalid layout .*ValueError"):
            generate_wks(xml, tmpdir)

    def test_rejects_4096_byte_sectors(self):
        from qcom.partitions_wks import PartitionsWksError
        with self.assertRaisesRegex(PartitionsWksError, "512 byte sector"):
            self.generate(sector_size=4096)

    def test_rejects_missing_file(self):
        from qcom.partitions_wks import PartitionsWksError
        with self.assertRaisesRegex(PartitionsWksError, "cdt.bin not found"):
            self.generate(files=("xbl.elf", "rootfs.img"))

    def test_rejects_duplicate_labels(self):
        from qcom.partitions_wks import PartitionsWksError
        with self.assertRaisesRegex(PartitionsWksError, "duplicate partition labels: cdt"):
            self.generate(misc_label="cdt")


class QcomPartitionsWksLayoutTests(OESelftestTestCase):
    """Convert every 512 byte sector layout deployed by qcom-partition-conf."""

    def test_deployed_layouts(self):
        from qcom.partitions_wks import generate_wks, load_partitions_xml

        bitbake("qcom-partition-conf")
        deploy_dir = get_bb_var("DEPLOY_DIR_IMAGE", "qcom-partition-conf")
        layouts = sorted(glob.glob(os.path.join(deploy_dir, "partitions", "*", "*", "partitions.xml")))
        self.assertTrue(layouts, "no partitions.xml deployed in %s" % deploy_dir)

        converted = 0
        for xml in layouts:
            params, physical = load_partitions_xml(xml)
            if params.get("SECTOR_SIZE_IN_BYTES") != "512":
                continue
            with self.subTest(layout=os.path.relpath(xml, deploy_dir)):
                with tempfile.TemporaryDirectory(dir=self.builddir) as file_dir:
                    create_files(file_dir, {p["filename"] for pp in physical for p in pp if p.get("filename")})
                    wks, _ = generate_wks(xml, file_dir)
                labels = list(parse_wks(wks))
                expected = [p["label"] for pp in physical for p in pp if int(p["size_in_kb"]) > 0]
                self.assertCountEqual(labels, expected)
                converted += 1

        shikra = os.path.join(deploy_dir, "partitions", "shikra-evk", "emmc", "partitions.xml")
        self.assertIn(shikra, layouts)
        with tempfile.TemporaryDirectory(dir=self.builddir) as file_dir:
            _, physical = load_partitions_xml(shikra)
            create_files(file_dir, {p["filename"] for pp in physical for p in pp if p.get("filename")})
            wks, _ = generate_wks(shikra, file_dir)
        self.assertEqual(list(parse_wks(wks))[-3:], ["cdt", "efi", "rootfs"])
        self.assertGreater(converted, 0)
