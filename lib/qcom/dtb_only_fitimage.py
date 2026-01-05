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
        self._dtbs = []

    def set_extra_opts(self, mkimage_extra_opts):
        self._mkimage_extra_opts = shlex.split(mkimage_extra_opts) if mkimage_extra_opts else []

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
        self._dtbs.append((dtb_node, compatible_str or "", dtb_id))

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
        counter = 0
        for dtb_node, compatible_str in self._dtbs:
            # qcom-metadata don't need any config entry
            if dtb_node.properties.get("type") == "qcom_metadata":
                continue
            # add one config for each compatible string of DTB
            for compatible in compatible_str.split():
                counter += 1
                conf_name = f"{self._conf_prefix}{counter}"
                dtb_node.compatible = compatible
                self._fitimage_emit_one_section_config(conf_name, dtb_node)

    def fitimage_emit_section_qcomconfig(self, overlay_groups, overlay_compats):
        counter = 1
        for (dtb_node, compatible_str, dtb_id) in self._dtbs:
            # qcom-metadata doesn't need any config entry
            if dtb_node.properties.get("type") == "qcom_metadata":
                continue

            # Only create config entries for base dtbs
            if not dtb_id.endswith(".dtb"):
                continue

            # Base-only configs
            base_compats = str(compatible_str or "").split()
            for compatible in base_compats:
                conf_name = f"{self._conf_prefix}{counter}"
                dtb_node.compatible = compatible
                self._fitimage_emit_one_section_config(conf_name, dtb_node)
                counter += 1

            # Overlay configs
            for ovl_list in (overlay_groups or {}).get(dtb_id, []):
                fdtentries = [f"fdt-{dtb_id}"] + [f"fdt-{ovl}" for ovl in ovl_list]

                lookup_key = "+".join([os.path.splitext(dtb_id)[0].replace(',', '_')] + [os.path.splitext(ovl)[0] for ovl in ovl_list])

                ovl_compats = str(((overlay_compats or {}).get(lookup_key, "")) or "").split()
                for compat in ovl_compats:
                    conf_name = f"{self._conf_prefix}{counter}"
                    dtb_node.compatible = compat
                    self._fitimage_emit_one_section_config(conf_name, dtb_node)

                    conf_node = self.configurations.sub_nodes[-1]
                    conf_node.add_property('fdt', fdtentries)
                    counter += 1

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
