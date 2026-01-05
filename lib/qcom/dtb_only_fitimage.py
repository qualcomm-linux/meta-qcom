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
from typing import Tuple, List, Dict
from oe.fitimage import ItsNodeRootKernel, ItsNodeConfiguration

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
        self._dtbs: List[Tuple[object, str]] = []
        self._base_compats: Dict[str, str] = {}
        self._overlay_groups: Dict[str, List[List[str]]] = {}
        self._overlay_compats: Dict[str, str] = {}

    def set_extra_opts(self, mkimage_extra_opts):
        self._mkimage_extra_opts = shlex.split(mkimage_extra_opts) if mkimage_extra_opts else []

    def set_base_compats(self, base_compats):
        self._base_compats = base_compats or {}

    def set_overlay_groups(self, overlay_groups):
        self._overlay_groups = overlay_groups or {}

    def set_overlay_compats(self, overlay_compats):
        self._overlay_compats = overlay_compats or {}

    # Emit the DTB section for the FIT image
    def fitimage_emit_section_dtb(self, dtb_id, dtb_path,
                                  compatible_str=None,
                                  dtb_type=None):
        load = None
        dtb_ext = os.path.splitext(dtb_path)[1]

        opt_props = {
            "data": '/incbin/("' + dtb_path + '")',
            "arch": self._arch
        }
        if load:
            opt_props["load"] = f"<{load}>"

        dtb_node = self.its_add_node_dtb(
            "fdt-" + dtb_id,
            "Flattened Device Tree blob",
            dtb_type,
            "none",
            opt_props,
            compatible_str
        )
        self._dtbs.append((dtb_node, dtb_id))

    def _fitimage_emit_one_section_config(self, conf_node_name, dtb=None):
        """Emit the fitImage ITS configuration section"""
        opt_props = {}
        conf_desc = []

        if dtb:
            conf_desc.append("FDT blob")
            opt_props["fdt"] = dtb.name
            if dtb.compatible:
                opt_props["compatible"] = dtb.compatible

        ItsNodeConfiguration(
            conf_node_name,
            self.configurations,
            description="FDT Blob",
            opt_props=opt_props
        )


    def fitimage_emit_section_config(self):
        conf_idx = 1
        for (dtb_node, dtb_id) in self._dtbs:
            # Skip qcom metadata blob
            if dtb_id == "qcom-metadata.dtb":
                continue

            # Add conf- entries only for dtb
            if dtb_id.endswith(".dtb"):
                conf_name = f"{self._conf_prefix}{conf_idx}"
                self._fitimage_emit_one_section_config(conf_name, dtb_node)

                base_compat = self._base_compats.get(dtb_id, "")
                conf_node = self.configurations.sub_nodes[-1]
                if base_compat:
                    conf_node.add_property('compatible', base_compat)

                # overlay_groups["base.dtb"] = [["ovl.dtbo"], ["ovl1.dtbo","ovl2.dtbo"], ...]
                for items in self._overlay_groups.get(dtb_id, []):
                    fdt_base = f"fdt-{dtb_id}"
                    fdt_ovls = [f"fdt-{ovl}" for ovl in items]
                    fdtentries = [fdt_base] + fdt_ovls

                    conf_idx += 1
                    conf_name = f"{self._conf_prefix}{conf_idx}"
                    self._fitimage_emit_one_section_config(conf_name, dtb_node)

                    # Composite key: "abc.dtb+ovl.dtbo+..."
                    lookup_key = "+".join([dtb_id] + items)
                    compat_str = self._overlay_compats.get(lookup_key, "")

                    conf_node = self.configurations.sub_nodes[-1]
                    conf_node.add_property('fdt', fdtentries)
                    if compat_str:
                        conf_node.add_property('compatible', compat_str)

                conf_idx += 1

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
