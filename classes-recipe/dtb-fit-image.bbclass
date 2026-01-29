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
    root_node.fitimage_emit_section_dtb("qcom-metadata.dtb", qcom_meta_dst, compatible_str=None, dtb_type="qcom_metadata")

    ordered_files = []
    dt_entries = set()

    # Base DTBs from KERNEL_DEVICETREE
    kernel_devicetree = (d.getVar('KERNEL_DEVICETREE') or "").split()
    kernel_bases = []
    for entry in kernel_devicetree:
        fname = os.path.basename(entry)
        kernel_bases.append(fname)
        if fname not in dt_entries:
            ordered_files.append(fname)
            dt_entries.add(fname)

    # Overlays from KERNEL_DEVICETREE_OVERLAYS
    overlays = (d.getVar('KERNEL_DEVICETREE_OVERLAYS') or "").split()
    overlay_bases = set()
    for ovl in overlays:
        fname = os.path.basename(ovl)
        overlay_bases.add(fname)
        if fname not in dt_entries:
            ordered_files.append(fname)
            dt_entries.add(fname)

    # Prepare structures for configs
    base_compats    = {}
    overlay_groups  = {}
    overlay_compats = {}

    # Base compatible strings
    for dtb_file in kernel_bases:
        base_compats[dtb_file] = d.getVarFlag("FIT_DTB_COMPATIBLE", dtb_file) or ""

    # Composite keys: base.dtb+ovl1.dtbo+ovl2.dtbo...
    compat_flags = d.getVarFlags("FIT_DTB_COMPATIBLE") or {}
    for key, compat_val in compat_flags.items():
        if '+' not in key:
            continue

        parts = [os.path.basename(entry.strip()) for entry in key.split('+') if entry.strip()]
        if not parts:
            continue

        base_part = parts[0]

        ovl_list = []
        for entry in parts[1:]:
            ov = os.path.basename(entry)
            if ov in overlay_bases:
                ovl_list.append(ov)

        if not ovl_list:
            continue

        overlay_groups.setdefault(base_part, []).append(ovl_list)
        norm_key = "+".join([base_part] + ovl_list)
        overlay_compats[norm_key] = compat_val

    # Emit DTB/DTBO sections
    for dtb_file in ordered_files:
        dtb_path = os.path.join(dtb_dir, dtb_file)
        if not os.path.exists(dtb_path):
            bb.fatal(f"Required file '{dtb_file}' not found at '{dtb_path}'.")

        _, ext = os.path.splitext(dtb_file)

        compatible = base_compats.get(dtb_file, "")
        if not compatible and ext != ".dtbo":
            bb.fatal(f"FIT_DTB_COMPATIBLE[{dtb_file}] is not set for base DTB '{dtb_file}'.")

        root_node.fitimage_emit_section_dtb(dtb_file, dtb_path, compatible_str=compatible, dtb_type="flat_dt")

    # Provide configuration data via setters
    root_node.set_base_compats(base_compats)
    root_node.set_overlay_groups(overlay_groups)
    root_node.set_overlay_compats(overlay_compats)

    # Emit configuration sections
    root_node.fitimage_emit_section_config()

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
