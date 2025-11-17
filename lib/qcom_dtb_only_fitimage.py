# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: GPL-2.0-only
#
# This file contains functions for Qualcomm-specific DTB-only FIT image generation,
# which imports classes from OE-Core fitimage.py and enhances to meet Qualcomm FIT
# specifications.
#
# For details on Qualcomm DTB metadata and FIT requirements, see:
# https://github.com/qualcomm-linux/qcom-dtb-metadata/blob/main/Documentation.md

import os
import shlex
import subprocess
import bb
from typing import Tuple
from oe.fitimage import ItsNodeRootKernel, get_compatible_from_dtb

# Custom extension of ItsNodeRootKernel to inject compatible strings
class QcomItsNodeRoot(ItsNodeRootKernel):

    def __init__(self, description, address_cells, conf_prefix, mkimage=None):
        # We only pass the essential parameters needed for QCOM DTB-only FIT image generation
        # because FIT features like signing, hashing, and padding are not required here.
        # Original full signature for reference:
        # super().__init__(description, address_cells, host_prefix, arch, conf_prefix,
        #                  sign_enable, sign_keydir, mkimage, mkimage_dtcopts,
        #                  mkimage_sign, mkimage_sign_args, hash_algo, sign_algo,
        #                  pad_algo, sign_keyname_conf, sign_individual, sign_keyname_img
        super().__init__(description, address_cells, None, "arm64", conf_prefix,
                         False, None, mkimage, None,
                         None, None, None, None,
                         None, None, False, None)

        self._mkimage_extra_opts = []
        self._dtbs = []

    def set_extra_opts(self, mkimage_extra_opts):
        self._mkimage_extra_opts = shlex.split(mkimage_extra_opts) if mkimage_extra_opts else []

    # Emit the DTB section for the FIT image
    def fitimage_emit_section_dtb(self, dtb_id, dtb_path, dtb_loadaddress=None,
                                  dtbo_loadaddress=None, add_compatible=False,
                                  compatible_str=None, dtb_abspath=None):
        load = None
        dtb_ext = os.path.splitext(dtb_path)[1]
        if dtb_ext == ".dtbo":
            if dtbo_loadaddress:
                load = dtbo_loadaddress
        elif dtb_loadaddress:
            load = dtb_loadaddress

        opt_props = {
            "data": '/incbin/("' + dtb_path + '")',
            "arch": self._arch
        }
        if load:
            opt_props["load"] = f"<{load}>"

        compatibles = None
        if add_compatible and compatible_str:
            compatibles = str(compatible_str).split()

        dtb_node = self.its_add_node_dtb(
            "fdt-" + dtb_id,
            "Flattened Device Tree blob",
            "flat_dt",
            "none",
            opt_props,
            compatibles
        )
        self._dtbs.append((dtb_node, compatibles or []))

    def fitimage_emit_section_config(self, default_dtb_image=None):
        counter = 0
        for counter, (dtb_node, compatibles) in enumerate(self._dtbs):
            if compatibles:
                for comp in compatibles:
                    conf_name = f"{self._conf_prefix}{counter}"
                    self._fitimage_emit_one_section_config(conf_name, dtb_node)
                    conf_node = self.configurations.sub_nodes[-1]
                    conf_node.add_property('compatible', comp)
            else:
                conf_name = f"{self._conf_prefix}{counter}"
                self._fitimage_emit_one_section_config(conf_name, dtb_node)

        if default_dtb_image and self._dtbs:
            default_conf = self._conf_prefix + default_dtb_image
        self.configurations.add_property('default', default_conf)

    # Override mkimage assemble to inject extra opts
    def run_mkimage_assemble(self, itsfile, fitfile):
        cmd = [self._mkimage, *self._mkimage_extra_opts, '-f', itsfile, fitfile]
        if self._mkimage_dtcopts:
            cmd.insert(1, '-D')
            cmd.insert(2, self._mkimage_dtcopts)

        bb.note(f"Running mkimage with extra opts: {' '.join(cmd)}")

        try:
            subprocess.run(cmd, check=True, capture_output=True)
        except subprocess.CalledProcessError as e:
            bb.fatal(
                f"Command '{' '.join(cmd)}' failed with return code {e.returncode}\n"
                f"stdout: {e.stdout.decode()}\n"
                f"stderr: {e.stderr.decode()}\n"
                f"itsfile: {os.path.abspath(itsfile)}"
            )
