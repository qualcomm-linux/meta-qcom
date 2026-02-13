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

    # KERNEL_DEVICETREE contains both .dtb and .dtbo
    files_set = {os.path.basename(x) for x in (d.getVar('KERNEL_DEVICETREE') or "").split()}

    # Collect DTB/DTBO names selected in KERNEL_DEVICETREE to validate declarative FIT_DTB_COMPATIBLE combinations
    base_dtb_list  = {os.path.splitext(f)[0] for f in files_set if f.endswith(".dtb")}
    overlay_dtbo_list = {os.path.splitext(f)[0] for f in files_set if f.endswith(".dtbo")}

    # Parse composite compatible keys :
    # FIT_DTB_COMPATIBLE[base+ovl1+ovl2] = "..."
    overlay_groups  = {}
    overlay_compats = {}

    compat_flags = d.getVarFlags("FIT_DTB_COMPATIBLE") or {}
    for key, compat_val in compat_flags.items():
        if '+' not in key:
            continue

        parts = [os.path.basename(p) for p in key.split('+')]
        if not parts:
            continue

        base_stem = parts[0]
        ovl_stems = parts[1:]

        # Only require base DTB to exist, overlays are optional
        # This allows using base DTB without any overlays
        if base_stem not in base_dtb_list:
            continue

        # Filter overlays to only include those actually present in KERNEL_DEVICETREE
        available_overlays = [ovl for ovl in ovl_stems if ovl in overlay_dtbo_list]
        base = base_stem + ".dtb"
        overlays = [ovl + ".dtbo" for ovl in available_overlays]

        overlay_groups.setdefault(base, []).append(overlays)
        overlay_compats[key] = compat_val

    # Emit DTB/DTBO sections for every entry from KERNEL_DEVICETREE
    for fname in files_set:
        dtb_path = os.path.join(dtb_dir, fname)
        if not os.path.exists(dtb_path):
            bb.fatal(f"Required file '{fname}' not found at '{dtb_path}'.")

        compatible = ""
        if fname.endswith(".dtb"):
            dtb_key = os.path.splitext(fname)[0].replace(',', '_')
            compatible = d.getVarFlag("FIT_DTB_COMPATIBLE", dtb_key) or ""
            if not compatible:
                bb.warn(f"FIT_DTB_COMPATIBLE[{dtb_key}] is not set for base DTB '{fname}'.")
                continue

        root_node.fitimage_emit_section_dtb(fname, dtb_path, compatible_str=compatible, dtb_type="flat_dt")

    # Emit configuration sections
    root_node.fitimage_emit_section_qcomconfig(overlay_groups, overlay_compats)

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
