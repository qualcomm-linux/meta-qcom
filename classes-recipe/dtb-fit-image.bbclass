#
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
#
# SPDX-License-Identifier: BSD-3-Clause-Clear
#

inherit kernel-arch

require conf/image-fitimage.conf

# Selects which FIT_DTB_COMPATIBLE include to load.  Defaults to the base
# file; set to fit-dtb-compatible-linux-qcom.inc in linux-qcom* kernel
# recipes to extend with linux-qcom-only overlay entries.
LINUX_QCOM_FIT_DTB_COMPATIBLE ?= "conf/machine/include/fit-dtb-compatible.inc"
require ${LINUX_QCOM_FIT_DTB_COMPATIBLE}

MKIMAGE ?= "${STAGING_BINDIR_NATIVE}/mkimage"

QCOMFIT_DEPLOYDIR = "${WORKDIR}/qcom_fitimage_deploy-${PN}"

do_generate_qcom_fitimage[depends] += "qcom-dtb-metadata:do_deploy u-boot-tools-native:do_populate_sysroot"
do_generate_qcom_fitimage[cleandirs] += "${QCOMFIT_DEPLOYDIR}"
python do_generate_qcom_fitimage() {
    import os
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
    # Consume the DTBs published by do_deploy: unlike ${B}, this location
    # is also populated when do_deploy is restored from sstate.
    dtb_dir = deploy_dir_image
    if d.getVar('KERNEL_DEPLOYSUBDIR'):
        dtb_dir = os.path.join(deploy_dir_image, d.getVar('KERNEL_DEPLOYSUBDIR'))
    os.makedirs(fit_dir, exist_ok=True)

    # Always include QCOM metadata first
    qcom_meta = os.path.join(deploy_dir_image, 'qcom-metadata.dtb')
    root_node.fitimage_emit_section_dtb("qcom-metadata.dtb", qcom_meta, compatible_str=None, dtb_type="qcom_metadata")

    # KERNEL_DEVICETREE contains both .dtb and .dtbo
    files_set = {os.path.basename(x) for x in (d.getVar('KERNEL_DEVICETREE') or "").split()}

    # Collect DTB/DTBO names selected in KERNEL_DEVICETREE to validate declarative FIT_DTB_COMPATIBLE combinations
    dtb_keys_list  = {os.path.splitext(f)[0].replace(',', '_') for f in files_set}

    # Parse FIT_DTB_COMPATIBLE[<encoded-compat>] = "<dtb-stem> [<overlay-stem>...]"
    # Flag keys encode commas as underscores (BitBake syntax constraint); decode
    # with replace('_', ',') to recover the actual compatible string.
    base_compats = {}   # base_dtb_id -> space-separated compat string(s)
    overlay_groups  = {}
    overlay_compats = {}
    seen_combos = set()  # track overlay combos already added to overlay_groups

    compat_flags = d.getVarFlags("FIT_DTB_COMPATIBLE") or {}
    for encoded_key, combo_val in compat_flags.items():
        compat_str = encoded_key.replace('_', ',')

        parts = [os.path.basename(p) for p in combo_val.split()]
        if not parts:
            continue

        # Skip combinations not present in KERNEL_DEVICETREE to avoid generating
        # invalid FIT configs from declarative FIT_DTB_COMPATIBLE metadata.
        if not all(dtb in dtb_keys_list for dtb in parts):
            continue

        base = parts[0] + ".dtb"
        base_dtb_id = base.replace(',', '_')
        overlays = [ovl + ".dtbo" for ovl in parts[1:]]

        if overlays:
            # Encode commas as underscores so combo_key matches the lookup_key
            # built in fitimage_emit_section_qcomconfig (dtb_only_fitimage.py),
            # which applies the same replacement; keep overlay_groups keyed by
            # the encoded base id for the same reason.
            combo_key = " ".join(p.replace(',', '_') for p in parts)
            if combo_key not in seen_combos:
                overlay_groups.setdefault(base_dtb_id, []).append(overlays)
                seen_combos.add(combo_key)
            existing = overlay_compats.get(combo_key, "")
            overlay_compats[combo_key] = (existing + " " + compat_str).strip()
        else:
            existing = base_compats.get(base_dtb_id, "")
            base_compats[base_dtb_id] = (existing + " " + compat_str).strip()

    # Emit DTB/DTBO sections for every entry from KERNEL_DEVICETREE
    for fname in files_set:
        dtb_path = os.path.join(dtb_dir, fname)
        if not os.path.exists(dtb_path):
            bb.fatal(f"Required file '{fname}' not found at '{dtb_path}'.")

        dtb_id = fname.replace(',', '_')
        compatible = ""
        if fname.endswith(".dtb"):
            compatible = base_compats.get(dtb_id, "")
            if not compatible and dtb_id not in overlay_groups:
                bb.note(
                    f"No FIT_DTB_COMPATIBLE entry covers '{fname}' for this "
                    f"kernel variant; it will appear in the FIT image but have "
                    f"no config node."
                )

        root_node.fitimage_emit_section_dtb(dtb_id, dtb_path, compatible_str=compatible, dtb_type="flat_dt")

    # Emit configuration sections
    root_node.fitimage_emit_section_qcomconfig(overlay_groups, overlay_compats)

    root_node.write_its_file(itsfile)

    root_node.run_mkimage_assemble(itsfile, fitname)
}
addtask generate_qcom_fitimage after do_deploy before do_qcom_dtbbin_deploy

# Setup sstate, see deploy.bbclass
SSTATETASKS += "do_generate_qcom_fitimage"
do_generate_qcom_fitimage[sstate-inputdirs] = "${QCOMFIT_DEPLOYDIR}"
do_generate_qcom_fitimage[sstate-outputdirs] = "${DEPLOY_DIR_IMAGE}"

python do_generate_qcom_fitimage_setscene () {
    sstate_setscene(d)
}
addtask do_generate_qcom_fitimage_setscene

do_generate_qcom_fitimage[stamp-extra-info] = "${MACHINE_ARCH}"
do_generate_qcom_fitimage[vardeps] += "FIT_DTB_COMPATIBLE"
