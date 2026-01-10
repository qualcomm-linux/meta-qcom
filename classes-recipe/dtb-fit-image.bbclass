#
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
#
# SPDX-License-Identifier: BSD-3-Clause-Clear
#

inherit kernel-arch

require conf/image-fitimage.conf

DEPENDS += "\
    u-boot-tools-native \
"

MKIMAGE ?= "${STAGING_BINDIR_NATIVE}/mkimage"

QCOMFIT_DEPLOYDIR = "${WORKDIR}/qcom_fitimage_deploy-${PN}"

do_generate_qcom_fitimage[depends] += "qcom-dtb-metadata:do_deploy"
do_generate_qcom_fitimage[cleandirs] += "${QCOMFIT_DEPLOYDIR}"

python do_generate_qcom_fitimage() {
    import os, shutil
    from qcom.dtb_only_fitimage import QcomItsNodeRoot

    fit_dir = d.getVar('QCOMFIT_DEPLOYDIR')
    itsfile = os.path.join(fit_dir, "qclinux-fit-image.its")
    fitname = os.path.join(fit_dir, "qclinuxfitImage")

    root_node = QcomItsNodeRoot(
        d.getVar("FIT_DESC"),
        d.getVar("FIT_ADDRESS_CELLS"),
        d.getVar("FIT_CONF_PREFIX"),
        d.getVar("MKIMAGE"),
    )
    root_node.set_extra_opts(d.getVar("FIT_DTB_MKIMAGE_EXTRA_OPTS") or "")

    deploy_dir_image = d.getVar('DEPLOY_DIR_IMAGE')
    dtb_dir = os.path.join(d.getVar('B'), "arch", d.getVar('ARCH'), "boot", "dts", "qcom")
    os.makedirs(fit_dir, exist_ok=True)

    # Always include QCOM metadata first
    qcom_meta_src = os.path.join(deploy_dir_image, 'qcom-metadata.dtb')
    qcom_meta_dst = os.path.join(dtb_dir, 'qcom-metadata.dtb')
    shutil.copy(qcom_meta_src, qcom_meta_dst)

    ordered_files = ["qcom-metadata.dtb"]

    # Base DTBs from KERNEL_DEVICETREE
    kernel_devicetree = (d.getVar('KERNEL_DEVICETREE') or "").split()
    kernel_bases = [os.path.splitext(os.path.basename(x))[0] for x in kernel_devicetree]
    for entry in kernel_devicetree:
        ordered_files.append(os.path.basename(entry))

    # Overlays from KERNEL_DEVICETREE_OVERLAYS
    overlays = (d.getVar('KERNEL_DEVICETREE_OVERLAYS') or "").split()
    for ovl in overlays:
        name = os.path.basename(ovl)
        if name.endswith(".dtso"):
            name = name[:-5] + ".dtbo"
        elif not name.endswith(".dtbo"):
            name += ".dtbo"
        if name not in ordered_files:
            ordered_files.append(name)

    # Prepare structures for configs
    base_compats    = {}
    overlay_groups  = {}
    overlay_compats = {}

    # Base compat strings
    for base in kernel_bases:
        base_compats[base] = d.getVarFlag("FIT_DTB_COMPATIBLE", base) or ""

    # Composite keys: base+ovl1+ovl2...
    compat_flags = d.getVarFlags("FIT_DTB_COMPATIBLE") or {}
    for key, compat_val in compat_flags.items():
        if '+' not in key:
            continue
        parts = key.split('+')
        base = parts[0]
        overlays_list = parts[1:]

        normalized_overlays = []
        for ovl in overlays_list:
            name = os.path.basename(ovl)
            if not name.endswith(".dtbo"):
                name += ".dtbo"
            dtbo_path = os.path.join(dtb_dir, name)
            if os.path.exists(dtbo_path):
                normalized_overlays.append(name)
                if name not in ordered_files:
                    ordered_files.append(name)
            else:
                bb.note(f"Skipping overlay '{name}' for composite '{key}': DTBO not found")

        if normalized_overlays:
            overlay_groups.setdefault(base, []).append(normalized_overlays)
            overlay_compats[key] = compat_val


    # Emit DTB/DTBO sections in preserved order
    for dtb_file in ordered_files:
        dtb_path = os.path.join(dtb_dir, dtb_file)
        if not os.path.exists(dtb_path):
            bb.fatal(f"Required file '{dtb_file}' not found at '{dtb_path}'.")

        base_name, ext = os.path.splitext(dtb_file)
        compatible = d.getVarFlag("FIT_DTB_COMPATIBLE", base_name) or ""

        if not compatible and dtb_file != "qcom-metadata.dtb" and ext != ".dtbo":
            bb.fatal(f"FIT_DTB_COMPATIBLE[{base_name}] is not set for base DTB '{dtb_file}'.")

        root_node.fitimage_emit_section_dtb(dtb_file, dtb_path, compatible_str=compatible)

    # Emit configuration sections
    root_node.fitimage_emit_section_config(overlay_groups, base_compats, overlay_compats)

    root_node.write_its_file(itsfile)
    root_node.run_mkimage_assemble(itsfile, fitname)
}

addtask generate_qcom_fitimage after do_populate_sysroot do_packagedata before do_qcom_dtbbin_deploy

# Setup sstate, see deploy.bbclass
SSTATETASKS += "do_generate_qcom_fitimage"
do_generate_qcom_fitimage[sstate-inputdirs] = "${QCOMFIT_DEPLOYDIR}"
do_generate_qcom_fitimage[sstate-outputdirs] = "${DEPLOY_DIR_IMAGE}"

python do_generate_qcom_fitimage_setscene () {
    sstate_setscene(d)
}
addtask do_generate_qcom_fitimage_setscene

do_generate_qcom_fitimage[stamp-extra-info] = "${MACHINE_ARCH}"
