#
# Copyright (c) 2026 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear
#
# Add a signed VIP (Validated Image Programming) digest table to the
# qcomflash bundle as vip-tables/DigestsToSign.bin.mbn, for
# `qdl --vip-table-path`:
#   1. qdl --dry-run --create-digests computes the table over the
#      rawprogram/patch XMLs (DigestsToSign.bin),
#   2. sectools mbn-tool generate wraps it in an MBN header,
#   3. it is signed as image id VIP with the OEM keys.
# Runs as a QCOMFLASH_PRE_TAR_HOOKS hook of image_types_qcom.bbclass, which
# inherits this class when QCOMFLASH_VIP is "1".

inherit qcom-firmware-sign

# The table is signed inside do_image_qcomflash; nothing to sign in place.
# qcom-firmware-sign adds its task from anonymous python, so drop it the
# same way, after that function has run.
python () {
    bb.build.deltask('do_qcom_firmware_sign', d)
}

QCOM_VIP_QDL      ?= "${STAGING_BINDIR_NATIVE}/qdl"
QCOM_VIP_FIREHOSE ?= "prog_firehose_ddr.elf"

# MBN header version used to wrap the digest table.
QCOM_VIP_MBN_VERSION ?= "6"

# Location of the table inside the qcomflash bundle.
QCOMFLASH_VIP_SUBDIR ?= "vip-tables"

do_image_qcomflash[depends] += "${@bb.utils.contains('QCOM_FIRMWARE_SIGN_ENABLE', '1', \
    'qdl-native:do_populate_sysroot sectools-native:do_populate_sysroot security-profiles-native:do_populate_sysroot', '', d)}"

QCOMFLASH_PRE_TAR_HOOKS += "create_qcomflash_vip_table"

# Runs from ${QCOMFLASH_DIR}, see image_types_qcom.bbclass.
create_qcomflash_vip_table() {
    if ! qcom_check_signing_enabled ; then
        bbnote "qcomflash-vip: QCOM_FIRMWARE_SIGN_ENABLE is not '1', skipping the VIP table"
        return 0
    fi

    if [ ! -f "${QCOM_VIP_FIREHOSE}" ]; then
        bbfatal "qcomflash-vip: firehose programmer ${QCOM_VIP_FIREHOSE} not found in ${QCOMFLASH_DIR}"
    fi

    vipdir="${QCOMFLASH_DIR}/${QCOMFLASH_VIP_SUBDIR}"
    install -d "${vipdir}"

    "${QCOM_VIP_QDL}" --allow-fusing --dry-run \
        --create-digests="${vipdir}" \
        "${QCOM_VIP_FIREHOSE}" \
        rawprogram*.xml patch*.xml \
        || bbfatal "qcomflash-vip: qdl digest generation failed"

    if [ ! -f "${vipdir}/DigestsToSign.bin" ]; then
        bbfatal "qcomflash-vip: qdl did not produce DigestsToSign.bin in ${vipdir}"
    fi

    "${QCOM_FIRMWARE_SIGN_SECTOOLS}" mbn-tool generate \
        --data "${vipdir}/DigestsToSign.bin" \
        --mbn-version "${QCOM_VIP_MBN_VERSION}" \
        --outfile "${vipdir}/DigestsToSign.bin.mbn" \
        || bbfatal "qcomflash-vip: sectools mbn-tool generate failed"

    # No verify-root: the profiles mark VIP <oem_vouch_for_disallowed/>,
    # which makes sectools reject an OEM-only signature the boot ROM accepts.
    qcom_sign_only_file "${vipdir}/DigestsToSign.bin.mbn"
}
