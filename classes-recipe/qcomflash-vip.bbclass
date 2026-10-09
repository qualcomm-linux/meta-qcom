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
# Runs as a do_image_qcomflash postfunc ahead of the tarball;
# image_types_qcom.bbclass inherits this class when QCOMFLASH_VIP is "1".

inherit qcom-firmware-sign

# The table is signed inside do_image_qcomflash; nothing to sign in place.
deltask do_qcom_firmware_sign

QCOM_VIP_QDL      ?= "${STAGING_BINDIR_NATIVE}/qdl"
QCOM_VIP_FIREHOSE ?= "prog_firehose_ddr.elf"

# MBN header version used to wrap the digest table.
QCOM_VIP_MBN_VERSION ?= "6"

# Location of the table inside the qcomflash bundle.
QCOMFLASH_VIP_SUBDIR ?= "vip-tables"

do_image_qcomflash[depends] += "${@bb.utils.contains('QCOM_FIRMWARE_SIGN_ENABLE', '1', \
    'qdl-native:do_populate_sysroot sectools-native:do_populate_sysroot security-profiles-native:do_populate_sysroot', '', d)}"

# Ahead of create_qcomflash_tarball, see image_types_qcom.bbclass.
do_image_qcomflash[postfuncs] =+ "create_qcomflash_vip_table"
create_qcomflash_vip_table[dirs] = "${QCOMFLASH_DIR}"

create_qcomflash_vip_table() {
    qcom_firmware_sign_check_prereqs

    if [ ! -f "${QCOM_VIP_FIREHOSE}" ]; then
        bbfatal "qcomflash-vip: firehose programmer ${QCOM_VIP_FIREHOSE} not found in ${QCOMFLASH_DIR}"
    fi

    vipdir="${QCOMFLASH_DIR}/${QCOMFLASH_VIP_SUBDIR}"
    install -d "${vipdir}"

    if ! "${QCOM_VIP_QDL}" --allow-fusing --dry-run \
            --create-digests="${vipdir}" \
            "${QCOM_VIP_FIREHOSE}" \
            rawprogram*.xml patch*.xml; then
        bbfatal "qcomflash-vip: qdl digest generation failed"
    fi

    if [ ! -f "${vipdir}/DigestsToSign.bin" ]; then
        bbfatal "qcomflash-vip: qdl did not produce DigestsToSign.bin in ${vipdir}"
    fi

    if ! "${QCOM_FIRMWARE_SIGN_SECTOOLS}" mbn-tool generate \
            --data "${vipdir}/DigestsToSign.bin" \
            --mbn-version "${QCOM_VIP_MBN_VERSION}" \
            --outfile "${vipdir}/DigestsToSign.bin.mbn"; then
        bbfatal "qcomflash-vip: sectools mbn-tool generate failed"
    fi

    # No verify-root: the profiles mark VIP <oem_vouch_for_disallowed/>,
    # which makes sectools reject an OEM-only signature the boot ROM accepts.
    qcom_sign_only_file "${vipdir}/DigestsToSign.bin.mbn"
}
