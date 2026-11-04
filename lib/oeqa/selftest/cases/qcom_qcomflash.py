#
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
#
# SPDX-License-Identifier: BSD-3-Clause-Clear
#
# Test cases for the board variants of the qcomflash bundle
# (QCOM_FLASH_VARIANTS in image_types_qcom.bbclass).
#

import filecmp
import glob
import os
import re
import tarfile

from oeqa.selftest.case import OESelftestTestCase
from oeqa.utils.commands import runCmd, bitbake, get_bb_vars


def variant_xml(xml, cdt):
    """Return the bundle XML text as a variant directory should hold it."""
    def rewrite(match):
        filename = match.group(1)
        if filename == 'DISK':
            return match.group(0)
        if filename == 'cdt.bin':
            filename = cdt + '.bin'
        return 'filename="../../%s"' % filename

    return re.sub(r'filename="([^"]+)"', rewrite, xml)


class QcomFlashVariantsTests(OESelftestTestCase):
    """Build a qcomflash bundle and check its variants/<name> directories.

    Each variant must flash from its own directory with the usual qdl
    command, program its own CDT, and reach the shared images through
    paths rather than links, so the tarball extracts on any host.
    """

    IMAGE = 'core-image-minimal'

    def test_cdt_variants(self):
        bb_vars = get_bb_vars(['DEPLOY_DIR_IMAGE', 'IMAGE_LINK_NAME',
                               'MACHINE', 'QCOM_CDT_FILE',
                               'QCOM_FLASH_ROOT_PROGRAMMER',
                               'QCOM_FLASH_VARIANT_DEFAULT',
                               'QCOM_FLASH_VARIANT_LIST'], self.IMAGE)
        # "<name>:<cdt>:<programmer>" entries, resolved by the class
        variants = [v.split(':') for v in
                    (bb_vars['QCOM_FLASH_VARIANT_LIST'] or '').split()]
        if not variants:
            self.skipTest("MACHINE %s sets no QCOM_FLASH_VARIANTS" %
                          bb_vars['MACHINE'])

        default = bb_vars['QCOM_FLASH_VARIANT_DEFAULT'] or variants[0][0]
        default_cdt, default_programmer = \
            {n: (c, p) for n, c, p in variants}[default]
        self.assertEqual(bb_vars['QCOM_CDT_FILE'], default_cdt,
                         "QCOM_CDT_FILE is not the default variant's CDT")
        root_programmer = bb_vars['QCOM_FLASH_ROOT_PROGRAMMER']
        self.assertEqual(root_programmer, default_programmer,
                         "the bundle root does not use the default "
                         "variant's programmer")

        bitbake(self.IMAGE)
        bitbake('qdl-native -c addto_recipe_sysroot')

        qdl_vars = get_bb_vars(['RECIPE_SYSROOT_NATIVE', 'bindir'],
                               'qdl-native')
        qdl = os.path.join(qdl_vars['RECIPE_SYSROOT_NATIVE'],
                           qdl_vars['bindir'], 'qdl')

        tarball = os.path.realpath(os.path.join(
            bb_vars['DEPLOY_DIR_IMAGE'],
            bb_vars['IMAGE_LINK_NAME'] + '.qcomflash.tar.gz'))
        bundle = tarball[:-len('.tar.gz')]
        self.assertTrue(os.path.isdir(bundle),
                        "qcomflash bundle %s not found" % bundle)

        with tarfile.open(tarball) as tar:
            members = tar.getmembers()
        links = [m.name for m in members if m.islnk() or m.issym()]
        self.assertEqual(links, [],
                         "qcomflash tarball must not contain links")
        # Member paths below the bundle's top-level directory
        archived = {m.name.split('/', 1)[1] for m in members if '/' in m.name}

        xmls = sorted(os.path.basename(x) for x in
                      glob.glob(os.path.join(bundle, 'rawprogram[0-9].xml')) +
                      glob.glob(os.path.join(bundle, 'patch*.xml')))
        self.assertTrue(xmls, "no rawprogram/patch XMLs in %s" % bundle)

        self.assertTrue(filecmp.cmp(
            os.path.join(bundle, 'cdt.bin'),
            os.path.join(bundle, bb_vars['QCOM_CDT_FILE'] + '.bin'),
            shallow=False), "cdt.bin is not the default variant's CDT")
        runCmd('%s --allow-fusing --dry-run %s rawprogram*.xml patch*.xml' %
               (qdl, root_programmer), cwd=bundle)

        for name, cdt, programmer in variants:
            with self.subTest(variant=name):
                vdir = os.path.join(bundle, 'variants', name)
                for f in [programmer] + xmls:
                    self.assertIn('variants/%s/%s' % (name, f), archived,
                                  "%s of variant %s not in the tarball" %
                                  (f, name))

                programs_cdt = False
                for xml in xmls:
                    with open(os.path.join(bundle, xml)) as f:
                        expected = variant_xml(f.read(), cdt)
                    with open(os.path.join(vdir, xml)) as f:
                        content = f.read()
                    self.assertEqual(content, expected,
                                     "%s of variant %s" % (xml, name))
                    programs_cdt |= 'filename="../../%s.bin"' % cdt in content
                self.assertTrue(programs_cdt,
                                "variant %s does not program %s.bin" %
                                (name, cdt))

                runCmd('%s --allow-fusing --dry-run %s rawprogram*.xml '
                       'patch*.xml' % (qdl, programmer), cwd=vdir)

    def test_variant_without_cdt(self):
        self.write_config('QCOM_FLASH_VARIANTS:append = " no-cdt"')
        result = bitbake('-e %s' % self.IMAGE, ignore_status=True)
        self.assertNotEqual(result.status, 0,
                            "a variant without a CDT must fail to parse")
        self.assertIn('variant no-cdt has no QCOM_FLASH_VARIANT_CDT[no-cdt]',
                      result.output)
