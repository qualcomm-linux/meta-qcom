#
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
#
# SPDX-License-Identifier: BSD-3-Clause-Clear
#
# Test cases for the QCOM DTB-only FIT image generation.
#
# These tests validate that QcomItsNodeRoot (used by dtb-fit-image.bbclass)
# produces correct ITS files so the UEFI firmware can parse them and select
# the right device tree at runtime.
#

import os
import re
import shutil
import logging

from oeqa.selftest.case import OESelftestTestCase
from oeqa.utils.commands import runCmd, bitbake, get_bb_vars

class QcomFitImageTests(OESelftestTestCase):
    """Unit tests for the QCOM DTB-only FIT image generator.

    Each test instantiates QcomItsNodeRoot directly, replicates the
    overlay-group processing from dtb-fit-image.bbclass, writes an ITS
    file, parses it back and asserts structural invariants that the UEFI
    firmware relies on.
    """

    # Valid metadata suffixes extracted from qcom-metadata.dts.
    # Used by test_compatible_string_format to cross-check compatible strings.
    METADATA_SUFFIXES = {
        # SoC
        "glymur", "hamoa", "purwa", "qcm6490", "qcs615", "qcs5430",
        "qcs6490", "qcs8275", "qcs8300", "qcs9075", "qcs9100", "sa8775p",
        # Board
        "adp", "atp", "cdp", "crd", "evk", "idp", "iot", "mtp", "qam", "qrd",
        # SoC version
        "socv1.0", "socv1.1", "socv2.0", "socv2.1",
        # Board revision
        "r1.0", "r1.1", "r2.0", "r2.1",
        # Peripheral subtype
        "subtype0", "subtype1", "subtype2", "subtype3", "subtype4",
        "subtype5", "subtype6", "subtype7", "subtype8", "subtype9",
        "subtype10",
        # Storage / memory / sku
        "emmc", "nand", "sdcard", "ufs",
        "256MB", "512MB", "1GB", "2GB", "3GB", "4GB",
        "sku0", "softsku0", "softsku1",
    }

    # Suffixes allowed by the metadata-check script's blacklist.
    # el2kvm/el2gh select the hypervisor (KVM or Gunyah) and, like camx and
    # staging, are not board metadata so they have no qcom-metadata node.
    COMPAT_EXTENSIONS = {"camx", "el2gh", "el2kvm", "staging"}

    # ------------------------------------------------------------------
    # Helpers
    # ------------------------------------------------------------------

    @staticmethod
    def _create_dummy_file(path, size=128):
        """Create a small random binary file (enough to satisfy mkimage)."""
        os.makedirs(os.path.dirname(path), exist_ok=True)
        with open(path, 'wb') as f:
            f.write(os.urandom(size))

    def _get_test_dir(self):
        topdir = os.environ['BUILDDIR']
        d = os.path.join(topdir, 'qcom-fitimage-test', self._testMethodName)
        if os.path.exists(d):
            shutil.rmtree(d)
        os.makedirs(d, exist_ok=True)
        return d

    def _build_qcom_fitimage(self, kernel_devicetree, fit_dtb_compatible):
        """Replicate dtb-fit-image.bbclass logic and produce an ITS file.

        Args:
            kernel_devicetree: Space-separated DTB/DTBO filenames
                (the value of KERNEL_DEVICETREE).
            fit_dtb_compatible: Dict mapping encoded compatible strings
                (commas replaced with underscores, e.g. ``"qcom_board-iot"``)
                to DTB+overlay combo strings (e.g. ``"board"`` or
                ``"board overlay"``). (the FIT_DTB_COMPATIBLE flags)

        Returns:
            (its_path, parsed) where *parsed* is the dict returned by
            ``_parse_its_file``.
        """
        # Lazy import: layer lib paths are only on sys.path after
        # _add_layer_libs() which runs *after* test module discovery.
        from qcom.dtb_only_fitimage import QcomItsNodeRoot

        test_dir = self._get_test_dir()
        dtb_dir = os.path.join(test_dir, 'dtbs')
        its_path = os.path.join(test_dir, 'qclinux-fit-image.its')

        root_node = QcomItsNodeRoot(
            "QCOM DTB-only FIT image for testing",
            "1",
            "conf-",
        )

        # ---- metadata DTB (always first) ----
        meta_path = os.path.join(dtb_dir, 'qcom-metadata.dtb')
        self._create_dummy_file(meta_path)
        root_node.fitimage_emit_section_dtb(
            "qcom-metadata.dtb", meta_path,
            compatible_str=None, dtb_type="qcom_metadata")

        # ---- replicate bbclass overlay-group parsing ----
        files_set = {os.path.basename(x)
                     for x in kernel_devicetree.split()}
        dtb_keys_list = {os.path.splitext(f)[0].replace(',', '_')
                         for f in files_set}

        base_compats = {}   # base_dtb_id -> space-separated compat string(s)
        overlay_groups = {}
        overlay_compats = {}
        seen_combos = set()  # track overlay combos already added to overlay_groups
        for encoded_key, combo_val in fit_dtb_compatible.items():
            compat_str = encoded_key.replace('_', ',')
            parts = [os.path.basename(p) for p in combo_val.split()]
            if not parts:
                continue
            if not all(dtb in dtb_keys_list for dtb in parts):
                continue
            base = parts[0] + ".dtb"
            base_dtb_id = base.replace(',', '_')
            overlays = [ovl + ".dtbo" for ovl in parts[1:]]
            if overlays:
                # Mirror the bbclass: encode commas so combo_key/overlay_groups
                # keys match the lookup_key in fitimage_emit_section_qcomconfig.
                combo_key = " ".join(p.replace(',', '_') for p in parts)
                if combo_key not in seen_combos:
                    overlay_groups.setdefault(base_dtb_id, []).append(overlays)
                    seen_combos.add(combo_key)
                existing = overlay_compats.get(combo_key, "")
                overlay_compats[combo_key] = (existing + " " + compat_str).strip()
            else:
                existing = base_compats.get(base_dtb_id, "")
                base_compats[base_dtb_id] = (existing + " " + compat_str).strip()

        # ---- emit image nodes (sorted for deterministic output) ----
        for fname in sorted(files_set):
            fpath = os.path.join(dtb_dir, fname)
            self._create_dummy_file(fpath)
            dtb_id = fname.replace(',', '_')
            compatible = ""
            if fname.endswith(".dtb"):
                compatible = base_compats.get(dtb_id, "")
            root_node.fitimage_emit_section_dtb(
                dtb_id, fpath,
                compatible_str=compatible, dtb_type="flat_dt")

        # ---- emit configuration nodes ----
        root_node.fitimage_emit_section_qcomconfig(
            overlay_groups, overlay_compats)

        root_node.write_its_file(its_path)
        parsed = self._parse_its_file(its_path)
        return its_path, parsed

    # ------------------------------------------------------------------
    # ITS parser
    # ------------------------------------------------------------------

    @staticmethod
    def _parse_its_file(its_path):
        """Parse an ITS file into ``{images: {…}, configurations: {…}}``.

        Only properties of depth-3 nodes (direct children of ``images``
        or ``configurations``) are captured.
        """
        images = {}
        configs = {}
        path = []
        props = {}

        with open(its_path) as f:
            for line in f:
                s = line.strip()
                if not s or s == '/dts-v1/;':
                    continue
                if s.endswith('{'):
                    name = s[:-1].strip()
                    path.append(name)
                    if len(path) >= 3:
                        props = {}
                elif s == '};':
                    if len(path) == 3:
                        parent, node = path[1], path[2]
                        if parent == 'images':
                            images[node] = dict(props)
                        elif parent == 'configurations':
                            configs[node] = dict(props)
                    if path:
                        path.pop()
                elif '=' in s and s.endswith(';') and len(path) >= 3:
                    key, _, rest = s.partition('=')
                    key = key.strip()
                    val = rest.strip().rstrip(';').strip()
                    if val.startswith('/incbin/'):
                        props[key] = val
                    elif val.startswith('<') and val.endswith('>'):
                        props[key] = val
                    elif '", "' in val:
                        props[key] = re.findall(r'"([^"]*)"', val)
                    elif val.startswith('"') and val.endswith('"'):
                        props[key] = val[1:-1]
                    else:
                        props[key] = val

        return {'images': images, 'configurations': configs}

    # ------------------------------------------------------------------
    # Assertion helpers
    # ------------------------------------------------------------------

    def _get_config_compats(self, parsed):
        """Return the list of compatible strings across all configs."""
        return [p['compatible']
                for p in parsed['configurations'].values()
                if 'compatible' in p]

    def _assert_fdt_linkage(self, parsed):
        """Every ``fdt`` ref in every config must name an existing image."""
        img_names = set(parsed['images'].keys())
        for cname, cprops in parsed['configurations'].items():
            fdt = cprops.get('fdt')
            self.assertIsNotNone(fdt,
                f"Config {cname} has no 'fdt' property")
            refs = fdt if isinstance(fdt, list) else [fdt]
            for ref in refs:
                self.assertIn(ref, img_names,
                    f"Config {cname}: fdt '{ref}' not found in images")

    def _assert_metadata_excluded_from_configs(self, parsed):
        """qcom-metadata must never appear in any configuration node."""
        for cname, cprops in parsed['configurations'].items():
            fdt = cprops.get('fdt', '')
            refs = fdt if isinstance(fdt, list) else [fdt]
            for ref in refs:
                self.assertNotEqual(ref, 'fdt-qcom-metadata.dtb',
                    f"Config {cname} references metadata DTB")

    # ==================================================================
    # Test cases
    # ==================================================================

    def test_single_dtb_single_compat(self):
        """Single DTB with one compatible string."""
        _, p = self._build_qcom_fitimage(
            "myboard.dtb",
            {"qcom_myboard-idp": "myboard"})

        # Images
        self.assertIn('fdt-qcom-metadata.dtb', p['images'])
        self.assertEqual(
            p['images']['fdt-qcom-metadata.dtb']['type'], 'qcom_metadata')
        self.assertIn('fdt-myboard.dtb', p['images'])
        self.assertEqual(
            p['images']['fdt-myboard.dtb']['type'], 'flat_dt')

        # Exactly one config
        self.assertEqual(len(p['configurations']), 1)
        conf = p['configurations']['conf-1']
        self.assertEqual(conf['compatible'], 'qcom,myboard-idp')
        self.assertEqual(conf['fdt'], 'fdt-myboard.dtb')

        self._assert_fdt_linkage(p)
        self._assert_metadata_excluded_from_configs(p)

    def test_single_dtb_multi_compat(self):
        """Single DTB with multiple compatibles -> one config per compat."""
        _, p = self._build_qcom_fitimage(
            "qcs6490-rb3gen2.dtb",
            {
                "qcom_qcs5430-iot": "qcs6490-rb3gen2",
                "qcom_qcs6490-iot": "qcs6490-rb3gen2",
            })

        self.assertEqual(len(p['images']), 2)   # metadata + 1 DTB
        self.assertEqual(len(p['configurations']), 2)

        compats = self._get_config_compats(p)
        self.assertIn('qcom,qcs5430-iot', compats)
        self.assertIn('qcom,qcs6490-iot', compats)

        # Both configs reference the same DTB
        for conf in p['configurations'].values():
            self.assertEqual(conf['fdt'], 'fdt-qcs6490-rb3gen2.dtb')

        self._assert_fdt_linkage(p)
        self._assert_metadata_excluded_from_configs(p)

    def test_dtb_with_single_overlay(self):
        """Base DTB + overlay -> base config + overlay config with fdt list."""
        _, p = self._build_qcom_fitimage(
            "qcs6490-rb3gen2.dtb qcs6490-rb3gen2-vision-mezzanine.dtbo",
            {
                "qcom_qcs6490-iot": "qcs6490-rb3gen2",
                "qcom_qcs6490-iot-subtype2": "qcs6490-rb3gen2 qcs6490-rb3gen2-vision-mezzanine",
            })

        self.assertEqual(len(p['images']), 3)
        self.assertIn('fdt-qcs6490-rb3gen2-vision-mezzanine.dtbo', p['images'])

        self.assertEqual(len(p['configurations']), 2)

        base_found = ovl_found = False
        for conf in p['configurations'].values():
            if conf['compatible'] == 'qcom,qcs6490-iot':
                self.assertEqual(conf['fdt'], 'fdt-qcs6490-rb3gen2.dtb')
                base_found = True
            elif conf['compatible'] == 'qcom,qcs6490-iot-subtype2':
                self.assertIsInstance(conf['fdt'], list)
                self.assertEqual(
                    conf['fdt'],
                    ['fdt-qcs6490-rb3gen2.dtb', 'fdt-qcs6490-rb3gen2-vision-mezzanine.dtbo'])
                ovl_found = True
        self.assertTrue(base_found, "Missing base config")
        self.assertTrue(ovl_found, "Missing overlay config")

        self._assert_fdt_linkage(p)
        self._assert_metadata_excluded_from_configs(p)

    def test_dtb_with_multiple_overlays(self):
        """Base DTB + multiple stacked overlays."""
        _, p = self._build_qcom_fitimage(
            "lemans-evk.dtb lemans-evk-camx.dtbo "
            "lemans-el2.dtbo lemans-camx-el2.dtbo",
            {
                "qcom_qcs9075-iot": "lemans-evk",
                "qcom_qcs9075-iot-camx-el2kvm":
                    "lemans-evk lemans-evk-camx lemans-el2 lemans-camx-el2",
                "qcom_qcs9075-socv2.0-iot-camx-el2kvm":
                    "lemans-evk lemans-evk-camx lemans-el2 lemans-camx-el2",
            })

        self.assertEqual(len(p['images']), 5)
        # 1 base + 2 overlay (one config per compatible string)
        self.assertEqual(len(p['configurations']), 3)

        expected_fdt_list = [
            'fdt-lemans-evk.dtb',
            'fdt-lemans-evk-camx.dtbo',
            'fdt-lemans-el2.dtbo',
            'fdt-lemans-camx-el2.dtbo',
        ]

        ovl_compats = []
        for conf in p['configurations'].values():
            compat = conf['compatible']
            if compat == 'qcom,qcs9075-iot':
                self.assertEqual(conf['fdt'], 'fdt-lemans-evk.dtb')
            else:
                ovl_compats.append(compat)
                self.assertIsInstance(conf['fdt'], list)
                self.assertEqual(conf['fdt'], expected_fdt_list)

        self.assertIn('qcom,qcs9075-iot-camx-el2kvm', ovl_compats)
        self.assertIn('qcom,qcs9075-socv2.0-iot-camx-el2kvm', ovl_compats)

        self._assert_fdt_linkage(p)
        self._assert_metadata_excluded_from_configs(p)

    def test_metadata_node_excluded_from_configs(self):
        """Metadata DTB appears as image (type=qcom_metadata) but never in configs."""
        _, p = self._build_qcom_fitimage(
            "simple.dtb",
            {"qcom_simple-evk": "simple"})

        meta = p['images'].get('fdt-qcom-metadata.dtb')
        self.assertIsNotNone(meta, "Metadata image node missing")
        self.assertEqual(meta['type'], 'qcom_metadata')

        self._assert_metadata_excluded_from_configs(p)

        for conf in p['configurations'].values():
            self.assertIn('compatible', conf)
            self.assertTrue(len(conf['compatible']) > 0)

    def test_overlay_filtering_by_kernel_devicetree(self):
        """Overlay combos whose DTBOs are absent from KERNEL_DEVICETREE are skipped."""
        _, p = self._build_qcom_fitimage(
            "base.dtb",            # overlay DTBO not listed
            {
                "qcom_base-iot": "base",
                "qcom_base-iot-subtype2": "base missing-overlay",
            })

        # Only 1 config (base); overlay combo silently dropped
        self.assertEqual(len(p['configurations']), 1)
        conf = list(p['configurations'].values())[0]
        self.assertEqual(conf['compatible'], 'qcom,base-iot')
        self.assertEqual(conf['fdt'], 'fdt-base.dtb')
        self.assertEqual(len(p['images']), 2)   # metadata + base

    def test_fdt_linkage_validity(self):
        """Every fdt reference in every config matches an existing image."""
        _, p = self._build_qcom_fitimage(
            "board.dtb camx.dtbo el2.dtbo",
            {
                "qcom_board-iot": "board",
                "qcom_board-iot-subtype2": "board camx",
                "qcom_board-iot-el2kvm": "board camx el2",
            })

        self._assert_fdt_linkage(p)

        # Ensure all DTBs are present as images
        self.assertIn('fdt-board.dtb', p['images'])
        self.assertIn('fdt-camx.dtbo', p['images'])
        self.assertIn('fdt-el2.dtbo', p['images'])

    def test_compatible_string_format(self):
        """Compatible strings use valid metadata suffixes (qcom,<soc>-<board>[-…])."""
        _, p = self._build_qcom_fitimage(
            "qcs6490-rb3gen2.dtb qcs6490-rb3gen2-vision-mezzanine.dtbo",
            {
                "qcom_qcs6490-iot": "qcs6490-rb3gen2",
                "qcom_qcs5430-iot": "qcs6490-rb3gen2",
                "qcom_qcs6490-iot-subtype2":
                    "qcs6490-rb3gen2 qcs6490-rb3gen2-vision-mezzanine",
                "qcom_qcs5430-iot-subtype2":
                    "qcs6490-rb3gen2 qcs6490-rb3gen2-vision-mezzanine",
            })

        all_valid = self.METADATA_SUFFIXES | self.COMPAT_EXTENSIONS
        for cname, cprops in p['configurations'].items():
            compat = cprops.get('compatible', '')
            self.assertTrue(compat.startswith("qcom,"),
                f"Config {cname}: '{compat}' must start with 'qcom,'")
            for part in compat[len("qcom,"):].split('-'):
                if not part:
                    continue
                self.assertIn(part, all_valid,
                    f"Config {cname}: unknown suffix '{part}' in '{compat}'")

    def test_dtbo_no_standalone_config(self):
        """Overlay .dtbo files must never be the sole fdt in a config."""
        _, p = self._build_qcom_fitimage(
            "base.dtb overlay1.dtbo overlay2.dtbo",
            {
                "qcom_base-iot": "base",
                "qcom_base-iot-subtype1": "base overlay1",
                "qcom_base-iot-subtype2": "base overlay2",
            })

        for cname, cprops in p['configurations'].items():
            fdt = cprops.get('fdt', '')
            if isinstance(fdt, str):
                self.assertFalse(fdt.endswith('.dtbo'),
                    f"Config {cname}: standalone DTBO '{fdt}'")
            elif isinstance(fdt, list):
                self.assertTrue(fdt[0].endswith('.dtb'),
                    f"Config {cname}: first fdt '{fdt[0]}' must be a .dtb")

    def test_multiple_base_dtbs_with_overlays(self):
        """Multiple base DTBs each with their own overlay sets."""
        _, p = self._build_qcom_fitimage(
            "boardA.dtb boardA-cam.dtbo boardB.dtb boardB-cam.dtbo",
            {
                "qcom_boardA-iot": "boardA",
                "qcom_boardA-iot-subtype2": "boardA boardA-cam",
                "qcom_boardB-idp": "boardB",
                "qcom_boardB-idp-subtype2": "boardB boardB-cam",
            })

        self.assertEqual(len(p['images']), 5)  # metadata + 2×(base+ovl)
        self.assertEqual(len(p['configurations']), 4)

        compats = self._get_config_compats(p)
        for expected in ('qcom,boardA-iot', 'qcom,boardA-iot-subtype2',
                         'qcom,boardB-idp', 'qcom,boardB-idp-subtype2'):
            self.assertIn(expected, compats)

        # Overlay configs must not mix boards
        for conf in p['configurations'].values():
            compat, fdt = conf['compatible'], conf['fdt']
            if compat == 'qcom,boardA-iot-subtype2':
                self.assertIsInstance(fdt, list)
                self.assertIn('fdt-boardA.dtb', fdt)
                self.assertIn('fdt-boardA-cam.dtbo', fdt)
                self.assertNotIn('fdt-boardB.dtb', fdt)
            elif compat == 'qcom,boardB-idp-subtype2':
                self.assertIsInstance(fdt, list)
                self.assertIn('fdt-boardB.dtb', fdt)
                self.assertIn('fdt-boardB-cam.dtbo', fdt)
                self.assertNotIn('fdt-boardA.dtb', fdt)

        self._assert_fdt_linkage(p)
        self._assert_metadata_excluded_from_configs(p)

    def test_base_dtb_only_in_overlay(self):
        """Base DTB with no standalone compatible, used only via overlays."""
        _, p = self._build_qcom_fitimage(
            "base.dtb overlay.dtbo",
            {
                # No standalone base entry – the DTB is only referenced via overlays
                "qcom_base-iot-subtype2": "base overlay",
            })

        # Only 1 config (the overlay combo), no base-only config
        self.assertEqual(len(p['configurations']), 1)
        conf = list(p['configurations'].values())[0]
        self.assertEqual(conf['compatible'], 'qcom,base-iot-subtype2')
        self.assertIsInstance(conf['fdt'], list)
        self.assertEqual(conf['fdt'],
                         ['fdt-base.dtb', 'fdt-overlay.dtbo'])

        self._assert_fdt_linkage(p)
        self._assert_metadata_excluded_from_configs(p)

    def test_mkimage_compile(self):
        """Compile the ITS with mkimage and verify with dumpimage."""
        its_path, p = self._build_qcom_fitimage(
            "testboard.dtb testboard-cam.dtbo",
            {
                "qcom_testboard-iot": "testboard",
                "qcom_testboard-iot-subtype2": "testboard testboard-cam",
            })

        # Build u-boot-tools-native (mkimage/dumpimage) and dtc-native
        # (mkimage shells out to dtc to compile the ITS)
        bitbake("u-boot-tools-native dtc-native -c addto_recipe_sysroot")
        uboot_vars = get_bb_vars(
            ['RECIPE_SYSROOT_NATIVE', 'bindir'], 'u-boot-tools-native')
        uboot_bindir = os.path.join(
            uboot_vars['RECIPE_SYSROOT_NATIVE'], uboot_vars['bindir'])
        mkimage = os.path.join(uboot_bindir, 'mkimage')
        dumpimage = os.path.join(uboot_bindir, 'dumpimage')

        dtc_vars = get_bb_vars(
            ['RECIPE_SYSROOT_NATIVE', 'bindir'], 'dtc-native')
        dtc_bindir = os.path.join(
            dtc_vars['RECIPE_SYSROOT_NATIVE'], dtc_vars['bindir'])

        fit_path = its_path.replace('.its', '.bin')

        # Compile with external-data + 8-byte alignment (QCOM default)
        # dtc must be on PATH for mkimage to find it
        runCmd(f"{mkimage} -E -B 8 -f {its_path} {fit_path}",
               native_sysroot=dtc_vars['RECIPE_SYSROOT_NATIVE'])
        self.assertExists(fit_path, "mkimage did not produce a FIT image")

        # Verify structure with dumpimage
        result = runCmd(f"{dumpimage} -l {fit_path}")
        out = result.output
        self.assertIn("QCOM DTB-only FIT image for testing", out)

        # Check that image & configuration sections appear
        self.assertIn("fdt-testboard.dtb", out)
        self.assertIn("fdt-testboard-cam.dtbo", out)
        self.assertIn("fdt-qcom-metadata.dtb", out)

        # Verify at least the expected number of configurations
        conf_count = out.count("Configuration")
        self.assertGreaterEqual(conf_count, 2,
            "Expected at least 2 configuration entries in dumpimage output")


class QcomFitImageIntegrationTests(OESelftestTestCase):
    """Integration tests that build a real FIT image from the kernel recipe.

    These tests validate that dtb-fit-image.bbclass, fit-dtb-compatible.inc
    and QcomItsNodeRoot work together end-to-end to produce a FIT image that
    the UEFI firmware will be able to parse at boot.

    A real kernel build is triggered so that DTB files, the metadata blob and
    the FIT binary are all produced by the same tooling as in production.
    """

    # Cache build vars across helper calls within the same test run.
    _cached_bb_vars = None

    # Metadata DTS node names we extract once (class-level cache).
    _meta_nodes = None

    # Suffixes allowed by the metadata-check blacklist
    COMPAT_SKIP_PATTERNS = {"camx", "el2gh", "el2kvm", "staging"}

    # ------------------------------------------------------------------
    # Helpers
    # ------------------------------------------------------------------

    def _get_bb_vars(self):
        """Retrieve bitbake variables needed by integration tests."""
        if self.__class__._cached_bb_vars is None:
            self.__class__._cached_bb_vars = get_bb_vars([
                'DEPLOY_DIR_IMAGE',
                'KERNEL_DEVICETREE',
                'MACHINE',
                'QCOM_DTB_DEFAULT',
                'FIT_CONF_PREFIX',
            ], 'virtual/kernel')
        return self.__class__._cached_bb_vars

    def _skip_unless_multi_dtb(self):
        """Skip the test unless the current MACHINE uses multi-dtb mode."""
        bb_vars = self._get_bb_vars()
        if bb_vars.get('QCOM_DTB_DEFAULT', '') != 'multi-dtb':
            self.skipTest(
                "MACHINE %s does not use multi-dtb FIT "
                "(QCOM_DTB_DEFAULT=%s)" %
                (bb_vars.get('MACHINE', '?'),
                 bb_vars.get('QCOM_DTB_DEFAULT', '?')))

    def _build_and_locate_fit(self):
        """Build virtual/kernel and return (its_path, fit_path, bb_vars).

        The build is triggered once; subsequent calls within the same
        oe-selftest invocation are essentially no-ops (sstate hit).
        """
        bb_vars = self._get_bb_vars()
        deploy_dir = bb_vars['DEPLOY_DIR_IMAGE']

        bitbake('virtual/kernel')

        its_path = os.path.join(deploy_dir, 'qclinux-fit-image.its')
        fit_path = os.path.join(deploy_dir, 'qclinuxfitImage')

        return its_path, fit_path, bb_vars

    @staticmethod
    def _parse_its_file(its_path):
        """Re-use the ITS parser from the unit-test class."""
        return QcomFitImageTests._parse_its_file(its_path)

    def _get_metadata_nodes(self, deploy_dir):
        """Extract valid node names from qcom-metadata.

        Decompiles the deployed qcom-metadata.dtb back to DTS using
        dtc-native, then parses node names.  This mirrors what
        check-fitimage-metadata.sh does.
        """
        if self.__class__._meta_nodes is not None:
            return self.__class__._meta_nodes

        meta_dtb = os.path.join(deploy_dir, 'qcom-metadata.dtb')
        if not os.path.exists(meta_dtb):
            return set()

        # Use dtc-native to decompile the .dtb to DTS text
        bitbake('dtc-native -c addto_recipe_sysroot')
        dtc_vars = get_bb_vars(
            ['RECIPE_SYSROOT_NATIVE', 'bindir'], 'dtc-native')
        dtc = os.path.join(
            dtc_vars['RECIPE_SYSROOT_NATIVE'], dtc_vars['bindir'], 'dtc')

        result = runCmd(f"{dtc} -I dtb -O dts {meta_dtb}")

        nodes = set()
        for line in result.output.splitlines():
            line = line.strip()
            if not line.endswith('{'):
                continue
            if line.startswith('&'):
                continue
            name = line.split()[0].rstrip(':').rstrip('{').strip()
            if name and name != '/' and name != 'description':
                nodes.add(name)

        self.__class__._meta_nodes = nodes
        return nodes

    def _setup_uboot_tools(self):
        """Build u-boot-tools-native and return the bindir."""
        bitbake('u-boot-tools-native -c addto_recipe_sysroot')
        uboot_vars = get_bb_vars(
            ['RECIPE_SYSROOT_NATIVE', 'bindir'], 'u-boot-tools-native')
        return os.path.join(
            uboot_vars['RECIPE_SYSROOT_NATIVE'], uboot_vars['bindir'])

    # ==================================================================
    # Integration tests
    # ==================================================================

    def test_fitimage_its_structure(self):
        """Build virtual/kernel and validate the generated ITS structure.

        Checks:
          - ITS and FIT files exist in DEPLOY_DIR_IMAGE
          - Every DTB from KERNEL_DEVICETREE has a corresponding image node
          - qcom-metadata.dtb image node exists with type=qcom_metadata
          - Every config has an fdt reference that exists in images
          - qcom-metadata never appears in any configuration
          - At least one configuration exists
        """
        self._skip_unless_multi_dtb()
        its_path, fit_path, bb_vars = self._build_and_locate_fit()

        self.assertExists(its_path,
            "ITS file not found in DEPLOY_DIR_IMAGE")
        self.assertExists(fit_path,
            "FIT binary not found in DEPLOY_DIR_IMAGE")

        parsed = self._parse_its_file(its_path)
        images = parsed['images']
        configs = parsed['configurations']

        # Metadata image node must exist and have correct type
        self.assertIn('fdt-qcom-metadata.dtb', images,
            "Missing qcom-metadata.dtb image node")
        self.assertEqual(images['fdt-qcom-metadata.dtb'].get('type'),
            'qcom_metadata',
            "Metadata image node has wrong type")

        # Every DTB from KERNEL_DEVICETREE must have an image node
        for dtb_path in bb_vars['KERNEL_DEVICETREE'].split():
            fname = os.path.basename(dtb_path).replace(',', '_')
            self.assertIn(f'fdt-{fname}', images,
                f"DTB '{fname}' from KERNEL_DEVICETREE missing in images")

        # Must have at least one configuration
        self.assertGreater(len(configs), 0,
            "No configuration nodes found in ITS")

        # FDT linkage: every fdt ref in configs must name an existing image
        for cname, cprops in configs.items():
            fdt = cprops.get('fdt')
            self.assertIsNotNone(fdt,
                f"Config {cname} has no 'fdt' property")
            refs = fdt if isinstance(fdt, list) else [fdt]
            for ref in refs:
                self.assertIn(ref, images,
                    f"Config {cname}: fdt '{ref}' not in images")

        # Metadata must never appear in any configuration
        for cname, cprops in configs.items():
            fdt = cprops.get('fdt', '')
            refs = fdt if isinstance(fdt, list) else [fdt]
            for ref in refs:
                self.assertNotEqual(ref, 'fdt-qcom-metadata.dtb',
                    f"Config {cname} references metadata DTB")

    def test_fitimage_dumpimage(self):
        """Verify the compiled FIT binary with dumpimage.

        Checks:
          - dumpimage can parse the FIT without errors
          - All DTBs from KERNEL_DEVICETREE appear in the dump
          - The metadata image node is listed
          - Configuration sections are present
        """
        self._skip_unless_multi_dtb()
        its_path, fit_path, bb_vars = self._build_and_locate_fit()
        self.assertExists(fit_path)

        bindir = self._setup_uboot_tools()
        dumpimage = os.path.join(bindir, 'dumpimage')

        result = runCmd(f"{dumpimage} -l {fit_path}")
        out = result.output

        # Metadata must appear
        self.assertIn('fdt-qcom-metadata.dtb', out,
            "Metadata node missing from dumpimage output")

        # All KERNEL_DEVICETREE entries must appear
        for dtb_path in bb_vars['KERNEL_DEVICETREE'].split():
            fname = os.path.basename(dtb_path).replace(',', '_')
            self.assertIn(f'fdt-{fname}', out,
                f"DTB '{fname}' missing from dumpimage output")

        # At least one configuration section must exist
        self.assertGreater(out.count('Configuration'), 0,
            "No configuration sections in dumpimage output")

    def test_fitimage_compatible_metadata_validation(self):
        """Cross-check compatible strings against qcom-metadata.dts.

        Every dash-separated suffix in each compatible string must
        either be a node name in the metadata DTS or be listed in
        the skip patterns (camx, el2gh, el2kvm, staging).

        This replicates the core check from check-fitimage-metadata.sh
        without requiring dtc.
        """
        self._skip_unless_multi_dtb()
        its_path, _, bb_vars = self._build_and_locate_fit()
        self.assertExists(its_path)

        parsed = self._parse_its_file(its_path)
        meta_nodes = self._get_metadata_nodes(bb_vars['DEPLOY_DIR_IMAGE'])

        # If we failed to load metadata nodes, fail loudly
        self.assertGreater(len(meta_nodes), 0,
            "Could not load any metadata nodes from qcom-metadata.dts")

        for cname, cprops in parsed['configurations'].items():
            compat = cprops.get('compatible', '')
            if not compat:
                continue

            self.assertTrue(compat.startswith('qcom,'),
                f"Config {cname}: compatible '{compat}' "
                f"must start with 'qcom,'")

            suffix_part = compat[len('qcom,'):]
            for part in suffix_part.split('-'):
                if not part:
                    continue
                if part in self.COMPAT_SKIP_PATTERNS:
                    continue
                self.assertIn(part, meta_nodes,
                    f"Config {cname}: suffix '{part}' from "
                    f"'{compat}' not found in metadata nodes "
                    f"(and not in skip list)")

    def test_fitimage_overlay_configs_fdt_list(self):
        """Overlay configurations must have an fdt list, not a single fdt.

        For any config whose fdt is a list, the first entry must be a
        base .dtb and the remaining entries must be .dtbo overlays.
        """
        self._skip_unless_multi_dtb()
        its_path, _, bb_vars = self._build_and_locate_fit()
        self.assertExists(its_path)

        parsed = self._parse_its_file(its_path)

        for cname, cprops in parsed['configurations'].items():
            fdt = cprops.get('fdt')
            if isinstance(fdt, list):
                self.assertTrue(fdt[0].endswith('.dtb'),
                    f"Config {cname}: first fdt '{fdt[0]}' in overlay "
                    f"list must be a .dtb")
                for ovl in fdt[1:]:
                    self.assertTrue(ovl.endswith('.dtbo'),
                        f"Config {cname}: overlay fdt '{ovl}' must "
                        f"be a .dtbo")
            elif isinstance(fdt, str):
                # Single-fdt configs must reference a .dtb, never a .dtbo
                self.assertTrue(fdt.endswith('.dtb'),
                    f"Config {cname}: standalone fdt '{fdt}' must "
                    f"be a .dtb, not a .dtbo")


class QcomFitImageMatrixTests(OESelftestTestCase):
    """Matrix tests validating DTB/FIT coverage across machines/providers.

    These tests avoid full image builds by using bitbake metadata expansion
    (bitbake -e via get_bb_vars) plus kernel source unpack. This validates:
      - KERNEL_DEVICETREE values resolve for each MACHINE/provider pair
      - DTBs/DTBOs declared by machine metadata exist in kernel source trees
      - LINUX_QCOM_KERNEL_DEVICETREE entries are present in qcom kernels
      - Every FIT_DTB_COMPATIBLE key matches at least one MACHINE/provider DTB set
    """

    KERNEL_PROVIDER_YOCTO = "linux-yocto"
    KERNEL_PROVIDERS_QCOM = ("linux-qcom-next", "linux-qcom")

    _provider_outputs_cache = {}
    _provider_machine_cache = {}
    _available_providers_cache = None
    _matrix_cache = None
    _layer_dir_cache = None

    @classmethod
    def _layer_dir(cls):
        """Return the meta-qcom layer directory using LAYERDIR_qcom from bitbake."""
        if cls._layer_dir_cache is not None:
            return cls._layer_dir_cache
        bb_vars = get_bb_vars(["LAYERDIR_qcom"], "virtual/kernel")
        layerdir = bb_vars.get("LAYERDIR_qcom")
        if not layerdir:
            raise AssertionError("LAYERDIR_qcom is not defined")
        cls._layer_dir_cache = layerdir
        return cls._layer_dir_cache

    @staticmethod
    def _dt_files(var_value):
        return {os.path.basename(x) for x in (var_value or "").split() if x}

    @staticmethod
    def _dt_keys_from_files(files):
        return {os.path.splitext(f)[0].replace(',', '_') for f in files}

    def _machine_list(self):
        machine_dir = os.path.join(self._layer_dir(), "conf", "machine")
        machines = []
        for name in sorted(os.listdir(machine_dir)):
            if name.endswith(".conf"):
                machines.append(name[:-5])  # strip ".conf"
        return machines

    def _fit_compatible_inc(self):
        return os.path.join(
            self._layer_dir(), "conf", "machine", "include",
            "fit-dtb-compatible.inc")

    def _linux_qcom_fit_compat_inc(self):
        return os.path.join(
            self._layer_dir(), "conf", "machine", "include",
            "fit-dtb-compatible-linux-qcom.inc")

    @staticmethod
    def _parse_fit_compatible_map(inc_path):
        """Parse a fit-dtb-compatible*.inc file into {encoded_compat: dtb_combo}.

        Each key is an encoded compatible string (commas replaced with
        underscores, e.g. ``"qcom_board-iot"``) and each value is the
        corresponding DTB+overlay combo string (e.g. ``"board"`` or
        ``"board overlay"``).  Handles both single-line and
        backslash-continued multi-line values.  Comment lines are skipped.
        """
        with open(inc_path) as f:
            content = f.read()
        content = content.replace('\\\n', ' ')
        content = '\n'.join(
            l for l in content.split('\n') if not l.lstrip().startswith('#'))
        result = {}
        for m in re.finditer(
                r'FIT_DTB_COMPATIBLE\[([^\]]+)\]\s*=\s*"([^"]*)"', content):
            key = m.group(1).strip()
            result[key] = m.group(2).strip()
        return result

    def _fit_compatible_map(self):
        """Return the base FIT_DTB_COMPATIBLE map (fit-dtb-compatible.inc only).

        Returns {encoded_compat: dtb_combo_str}.
        """
        return self._parse_fit_compatible_map(self._fit_compatible_inc())

    @staticmethod
    def _name_variants(name):
        return {
            name,
            name.replace('_', ','),
            name.replace(',', '_'),
        }

    def _has_dt_output(self, output_files, part_name, exts):
        for variant in self._name_variants(part_name):
            for ext in exts:
                if f"{variant}{ext}" in output_files:
                    return True
        return False

    def _provider_output_files(self, provider):
        """Return set of DTB/DTBO output filenames available in provider source."""
        if provider in self.__class__._provider_outputs_cache:
            return self.__class__._provider_outputs_cache[provider]

        machine = self._provider_machine(provider)
        self.assertIsNotNone(machine,
            f"Could not find a MACHINE compatible with provider {provider}")
        postconfig = '\n'.join([
            f'MACHINE = "{machine}"',
            f'PREFERRED_PROVIDER_virtual/kernel = "{provider}"',
        ])

        # Lightweight: unpack kernel source once (no compile/image build).
        bitbake("virtual/kernel -c unpack", postconfig=postconfig)
        bb_vars = get_bb_vars(["S"], "virtual/kernel", postconfig=postconfig)
        src_dir = bb_vars.get("S") or ""
        self.assertTrue(src_dir and os.path.isdir(src_dir),
            f"Could not resolve source directory (S) for {provider}")

        # Search in architecture-specific directories for DTS/DTSO files.
        arch_paths = (
            os.path.join(src_dir, "arch", "arm64", "boot", "dts"),
            os.path.join(src_dir, "arch", "arm", "boot", "dts"),
        )

        outputs = set()
        for dts_dir in arch_paths:
            if not os.path.isdir(dts_dir):
                continue
            for _, _, files in os.walk(dts_dir):
                for fname in files:
                    if fname.endswith(".dts"):
                        outputs.add(os.path.splitext(fname)[0] + ".dtb")
                    elif fname.endswith(".dtso"):
                        outputs.add(os.path.splitext(fname)[0] + ".dtbo")

        self.__class__._provider_outputs_cache[provider] = outputs
        return outputs

    def _provider_machine(self, provider):
        if provider in self.__class__._provider_machine_cache:
            return self.__class__._provider_machine_cache[provider]

        for machine in self._machine_list():
            try:
                self._resolve_machine_provider(machine, provider)
                self.__class__._provider_machine_cache[provider] = machine
                return machine
            except AssertionError:
                continue

        self.__class__._provider_machine_cache[provider] = None
        return None

    def _available_providers(self):
        if self.__class__._available_providers_cache is not None:
            return self.__class__._available_providers_cache

        providers = []
        for provider in (self.KERNEL_PROVIDER_YOCTO,) + self.KERNEL_PROVIDERS_QCOM:
            result = runCmd(
                f"bitbake-layers show-recipes {provider}",
                ignore_status=True,
                assert_error=False,
            )
            if f"{provider}:" in result.output:
                providers.append(provider)

        self.__class__._available_providers_cache = providers
        return providers

    def _resolve_machine_provider(self, machine, provider):
        postconfig = '\n'.join([
            f'MACHINE = "{machine}"',
            f'PREFERRED_PROVIDER_virtual/kernel = "{provider}"',
        ])
        bb_vars = get_bb_vars(
            ["KERNEL_DEVICETREE", "LINUX_QCOM_KERNEL_DEVICETREE"],
            "virtual/kernel",
            postconfig=postconfig,
        )
        dt_files = self._dt_files(bb_vars.get("KERNEL_DEVICETREE"))
        extra_files = self._dt_files(bb_vars.get("LINUX_QCOM_KERNEL_DEVICETREE"))
        return {
            "machine": machine,
            "provider": provider,
            "dt_files": dt_files,
            "dt_keys": self._dt_keys_from_files(dt_files),
            "extra_files": extra_files,
        }

    def _resolve_qcom_provider(self, machine):
        last_error = None
        available = set(self._available_providers())
        for provider in self.KERNEL_PROVIDERS_QCOM:
            if provider not in available:
                continue
            try:
                return self._resolve_machine_provider(machine, provider)
            except AssertionError as exc:
                last_error = exc
        raise AssertionError(
            f"Could not resolve qcom kernel provider for {machine}: {last_error}")

    def _matrix(self):
        if self.__class__._matrix_cache is not None:
            return self.__class__._matrix_cache

        matrix = []
        for machine in self._machine_list():
            try:
                yocto = self._resolve_machine_provider(
                    machine, self.KERNEL_PROVIDER_YOCTO)
                matrix.append(yocto)
            except AssertionError:
                # Some machines are intentionally not compatible with
                # linux-yocto; validate them through qcom kernels only.
                pass
            qcom = self._resolve_qcom_provider(machine)
            matrix.append(qcom)
        self.__class__._matrix_cache = matrix
        return matrix

    def test_machine_dtb_entries_exist_for_kernel_providers(self):
        """Validate machine DTB metadata against linux-yocto and qcom kernels."""
        available = set(self._available_providers())
        qcom_available = [p for p in self.KERNEL_PROVIDERS_QCOM if p in available]
        self.assertGreater(len(qcom_available), 0,
            "No qcom kernel provider available (linux-qcom-next/linux-qcom)")

        yocto_outputs = self._provider_output_files(self.KERNEL_PROVIDER_YOCTO)

        qcom_outputs = {p: self._provider_output_files(p) for p in qcom_available}

        errors = []
        yocto_warnings = []
        for machine in self._machine_list():
            yocto = None
            try:
                yocto = self._resolve_machine_provider(
                    machine, self.KERNEL_PROVIDER_YOCTO)
            except AssertionError:
                # Some machines are intentionally not compatible with
                # linux-yocto; validate them through qcom kernels only.
                yocto = None
            qcom = self._resolve_qcom_provider(machine)

            if yocto is not None:
                yocto_base_files = yocto["dt_files"] - yocto["extra_files"]
                missing_yocto = sorted(yocto_base_files - yocto_outputs)
                if missing_yocto:
                    yocto_warnings.append(
                        f"{machine}/{self.KERNEL_PROVIDER_YOCTO}: missing DT files "
                        f"in kernel source: {', '.join(missing_yocto)}")

            missing_qcom = sorted(qcom["dt_files"] - qcom_outputs[qcom["provider"]])
            if missing_qcom:
                errors.append(
                    f"{machine}/{qcom['provider']}: missing DT files in kernel "
                    f"source: {', '.join(missing_qcom)}")

            missing_extra = sorted(qcom["extra_files"] - qcom["dt_files"])
            if missing_extra:
                errors.append(
                    f"{machine}/{qcom['provider']}: LINUX_QCOM_KERNEL_DEVICETREE "
                    f"not present in KERNEL_DEVICETREE: {', '.join(missing_extra)}")

            if yocto is not None:
                yocto_base_files = yocto["dt_files"] - yocto["extra_files"]
                yocto_present_base = yocto_base_files & yocto_outputs
                missing_base = sorted(yocto_present_base - qcom["dt_files"])
                if missing_base:
                    errors.append(
                        f"{machine}/{qcom['provider']}: missing linux-yocto base DT files: "
                        f"{', '.join(missing_base)}")

        for warning in yocto_warnings:
            logging.warning(warning)

        if errors:
            self.fail("\n".join(errors))

    def test_fit_dtb_compatible_combos_exist_in_kernel_sources(self):
        """Every FIT_DTB_COMPATIBLE value must name DT files present in kernel sources.

        Base file entries are checked against the union of linux-yocto and qcom
        kernel sources.  Entries from fit-dtb-compatible-linux-qcom.inc are
        checked against qcom kernel sources only (they reference
        LINUX_QCOM_KERNEL_DEVICETREE overlays not available in linux-yocto).
        """
        available = set(self._available_providers())

        qcom_available = [p for p in self.KERNEL_PROVIDERS_QCOM if p in available]
        self.assertGreater(
            len(qcom_available), 0,
            "No qcom kernel provider available (linux-qcom-next/linux-qcom)")

        all_outputs = set()
        for provider in [self.KERNEL_PROVIDER_YOCTO] + qcom_available:
            all_outputs |= self._provider_output_files(provider)

        qcom_outputs = set()
        for provider in qcom_available:
            qcom_outputs |= self._provider_output_files(provider)

        def _check_combos(compat_map, output_files, label):
            missing = []
            for encoded_key, combo_val in compat_map.items():
                parts = combo_val.split()
                base = parts[0]
                if len(parts) == 1:
                    if not self._has_dt_output(output_files, base, (".dtb", ".dtbo")):
                        missing.append(f"{encoded_key} -> {combo_val}  [{label}]")
                    continue
                if not self._has_dt_output(output_files, base, (".dtb",)):
                    missing.append(f"{encoded_key} -> {combo_val}  [{label}]")
                    continue
                if any(not self._has_dt_output(output_files, ovl, (".dtbo",))
                       for ovl in parts[1:]):
                    missing.append(f"{encoded_key} -> {combo_val}  [{label}]")
            return missing

        missing = _check_combos(self._fit_compatible_map(), all_outputs, "base")

        qcom_inc = self._linux_qcom_fit_compat_inc()
        if os.path.exists(qcom_inc):
            qcom_map = self._parse_fit_compatible_map(qcom_inc)
            missing += _check_combos(qcom_map, qcom_outputs, "linux-qcom")

        if missing:
            self.fail(
                "FIT_DTB_COMPATIBLE entries missing DTB/DTBO files in kernel sources:\n"
                + "\n".join(sorted(missing)))

    def test_fit_dtb_compatible_no_duplicate_keys(self):
        """Each FIT_DTB_COMPATIBLE key must be defined exactly once per file.

        Bitbake silently overwrites a flag variable when the same key is
        assigned twice, dropping the first set of compatible strings without
        any warning.
        """
        errors = []
        for inc_path in (self._fit_compatible_inc(), self._linux_qcom_fit_compat_inc()):
            if not os.path.exists(inc_path):
                continue
            key_re = re.compile(r'^\s*FIT_DTB_COMPATIBLE\[([^\]]+)\]\s*=')
            seen = {}
            with open(inc_path) as f:
                for lineno, line in enumerate(f, 1):
                    m = key_re.match(line)
                    if m:
                        key = m.group(1).strip()
                        seen.setdefault(key, []).append(lineno)

            fname = os.path.basename(inc_path)
            for k, lines in sorted(seen.items()):
                if len(lines) > 1:
                    errors.append(
                        f"  '{k}' in {fname} defined at lines: "
                        f"{', '.join(str(l) for l in lines)}")

        if errors:
            self.fail(
                "Duplicate FIT_DTB_COMPATIBLE keys:\n" + "\n".join(errors))

    def test_fit_dtb_compatible_compat_key_format(self):
        """Each FIT_DTB_COMPATIBLE key must be an encoded compatible string.

        Keys encode the Device Tree compatible string with commas replaced by
        underscores (BitBake flag names cannot contain commas).  Every key
        must therefore start with the 'qcom_' prefix, ensuring the bbclass
        can decode it back to a valid 'qcom,…' compatible string.
        """
        errors = []
        for inc_path in (self._fit_compatible_inc(), self._linux_qcom_fit_compat_inc()):
            if not os.path.exists(inc_path):
                continue
            fname = os.path.basename(inc_path)
            for encoded_key in self._parse_fit_compatible_map(inc_path):
                if not encoded_key.startswith("qcom_"):
                    errors.append(
                        f"  '{encoded_key}' in {fname}: must start with 'qcom_'")

        if errors:
            self.fail(
                "FIT_DTB_COMPATIBLE keys must be encoded compatible strings "
                "(qcom_<compat>):\n" + "\n".join(errors))
