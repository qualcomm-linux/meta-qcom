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
from oe.fitimage import ItsNodeRootKernel, ItsNodeConfiguration, get_compatible_from_dtb

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
                                  compatible_str=None):
        load = None
        dtb_ext = os.path.splitext(dtb_path)[1]

        opt_props = {
            "data": '/incbin/("' + dtb_path + '")',
            "arch": self._arch
        }
        if load:
            opt_props["load"] = f"<{load}>"

        if dtb_id == "qcom-metadata.dtb":
            dtb_type = "qcom_metadata"
        else:
            dtb_type = "flat_dt"

        dtb_node = self.its_add_node_dtb(
            "fdt-" + dtb_id,
            "Flattened Device Tree blob",
            dtb_type,
            "none",
            opt_props,
            compatible_str
        )
        self._dtbs.append(dtb_node)

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

    def fitimage_emit_section_config(self, overlay_groups, base_compats, overlay_compats):
        conf_idx = 1
        for dtb_node in self._dtbs:
            # Skip qcom metadata blob
            if dtb_node.name.endswith("qcom-metadata.dtb"):
                continue

            # Add conf- entries only for dtb
            if dtb_node.name.endswith(".dtb"):
                conf_name = f"{self._conf_prefix}{conf_idx}"
                self._fitimage_emit_one_section_config(conf_name, dtb_node)

                dtb_name = dtb_node.name
                if dtb_name.startswith("fdt-"):
                    dtb_name = dtb_name[len("fdt-"):]
                if dtb_name.endswith(".dtb"):
                    dtb_name = dtb_name[:-len(".dtb")]
                base_dtb = dtb_name

                # base-only conf compatible
                base_compat = base_compats.get(base_dtb, "")
                conf_node = self.configurations.sub_nodes[-1]
                if base_compat:
                    conf_node.add_property('compatible', base_compat)

                # Generate conf entries for DTBO groups along with their base DTB
                for items in overlay_groups.get(base_dtb, []):
                    fdt_base = f"fdt-{base_dtb}.dtb"
                    fdt_ovls = []
                    for it in items:
                        name = it
                        if not name.startswith("fdt-"):
                            name = f"fdt-{name}"
                        if not name.endswith(".dtbo"):
                            name = f"{name}.dtbo"
                        fdt_ovls.append(name)

                    fdtentries = [fdt_base] + fdt_ovls
                    conf_idx += 1
                    conf_name = f"{self._conf_prefix}{conf_idx}"
                    self._fitimage_emit_one_section_config(conf_name, dtb_node)

                    # Lookup overlay compat using '+'-joined key to match BitBake flag
                    composite_key = "+".join([base_dtb] + items)
                    compat_str = overlay_compats.get(composite_key, "")

                    conf_node = self.configurations.sub_nodes[-1]
                    conf_node.add_property('fdt', fdtentries)
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
