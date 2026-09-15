#
# Copyright (c) 2026 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear
#
# qcomflash-vip image type: produce a VIP (Validated Image Programming)
# digest table for a signed qcomflash build, wrap it as an MBN and sign
# it with the OEM secure-boot keys.  The resulting `.qcomflash-vip.tar`
# bundle deploys the signed VIP table next to the qcomflash artifacts
# and is intended to be flashed alongside it via qdl --vip-table-path
# (see qcom-sec-tools/secure.sh "make sign-build" for the reference
# pipeline this class is derived from).
#
# Usage:
#     IMAGE_FSTYPES += "qcomflash-vip"
#
# Requires the qcom-firmware-sign signing machinery to be active
# (QCOM_FIRMWARE_SIGN_ENABLE = "1"); without it the VIP step is a
# no-op because there is nothing to bind the digests to.

inherit image_types_qcom qcom-firmware-sign

IMAGE_TYPES                += "qcomflash-vip"
IMAGE_TYPEDEP:qcomflash-vip += "qcomflash"
IMAGE_CMD:qcomflash-vip     = "create_qcomflash_vip_pkg"

# Tooling staged into the native sysroot by the secure-boot recipes.
QCOM_VIP_QDL      ?= "${STAGING_BINDIR_NATIVE}/qdl"
QCOM_VIP_SECTOOLS ?= "${QCOM_FIRMWARE_SIGN_SECTOOLS}"

QCOM_VIP_FIREHOSE ?= "prog_firehose_ddr.elf"

# MBN container version used to wrap the digest table
QCOM_VIP_MBN_VERSION ?= "6"

QCOMFLASH_VIP_DIR = "${IMGDEPLOYDIR}/${IMAGE_NAME}.qcomflash-vip"

do_image_qcomflash_vip[dirs]      = "${QCOMFLASH_VIP_DIR}"
do_image_qcomflash_vip[cleandirs] = "${QCOMFLASH_VIP_DIR}"
do_image_qcomflash_vip[depends]  += "qdl-native:do_populate_sysroot \
                                     sectools-native:do_populate_sysroot \
                                     security-profiles-native:do_populate_sysroot"

create_qcomflash_vip_pkg() {
    if ! qcom_check_signing_enabled ; then
        bbwarn "qcomflash-vip requested but QCOM_FIRMWARE_SIGN_ENABLE is not '1'; skipping VIP generation"
        return 0
    fi

    if [ ! -d "${QCOMFLASH_DIR}" ]; then
        bbfatal "qcomflash-vip: expected qcomflash output at ${QCOMFLASH_DIR}, but it does not exist"
    fi

    if [ ! -f "${QCOMFLASH_DIR}/${QCOM_VIP_FIREHOSE}" ]; then
        bbfatal "qcomflash-vip: firehose programmer ${QCOM_VIP_FIREHOSE} not found in ${QCOMFLASH_DIR}"
    fi

    # 1. Mirror the qcomflash content into our output directory.  The
    # resulting tarball is a superset of the regular qcomflash tarball
    bbnote "qcomflash-vip: mirroring qcomflash artefacts into ${QCOMFLASH_VIP_DIR}"
    cp -al "${QCOMFLASH_DIR}"/. "${QCOMFLASH_VIP_DIR}/"

    vipdir="${QCOMFLASH_VIP_DIR}/vip-tables"
    install -d "${vipdir}"

    # 2. Generate the digest table.  qdl in --dry-run + --create-digests
    # mode walks the rawprogram/patch XMLs the same way it would for a
    # real flash, but instead of transferring data computes a digest
    # over each programmed segment and writes them to
    # DigestsToSign.bin under the requested directory.
    (
        cd "${QCOMFLASH_VIP_DIR}"
        "${QCOM_VIP_QDL}" --allow-fusing --dry-run \
            --create-digests="${vipdir}" \
            "${QCOM_VIP_FIREHOSE}" \
            rawprogram*.xml patch*.xml
    ) || bbfatal "qcomflash-vip: qdl digest generation failed"

    if [ ! -f "${vipdir}/DigestsToSign.bin" ]; then
        bbfatal "qcomflash-vip: qdl did not produce DigestsToSign.bin in ${vipdir}"
    fi

    # 3. Wrap the digest table as an MBN so it has a proper Qualcomm
    # firmware header, otherwise the on-target VIP verifier rejects
    # it. mbn-tool generate writes the MBN beside the source bin.
    "${QCOM_VIP_SECTOOLS}" mbn-tool generate \
        --data "${vipdir}/DigestsToSign.bin" \
        --mbn-version "${QCOM_VIP_MBN_VERSION}" \
        --outfile "${vipdir}/DigestsToSign.bin.mbn" \
        || bbfatal "qcomflash-vip: sectools mbn-tool generate failed"

    # 4. Sign the MBN as image-id VIP using the same keying material as
    # the rest of the firmware.  qcom_sign_only_file looks up VIP in
    # QCOM_FIRMWARE_SIGN_IMAGE_ID_MAP, validates it against the active
    # security profile and runs sectools secure-image --sign in-place.
    #
    # We deliberately skip the post-sign verify-root step (use
    # qcom_sign_only_file, not qcom_sign_verify_file): the upstream
    # github security-profiles repo marks VIP with
    # <oem_vouch_for_disallowed/>, which makes sectools' verify-root
    # reject OEM-only signatures even though the boot ROM accepts
    qcom_sign_only_file "${vipdir}/DigestsToSign.bin.mbn"

    # 5. Bundle the mirrored qcomflash directory plus the signed VIP
    # table into a single tarball.  The tar layout mirrors the
    # qcomflash convention: everything lives under <image>-<machine>/
    # so consumers that drive flashing from the extracted root can
    # use exactly the same paths as they do for the plain qcomflash
    # artefact, with the additional vip-tables/ subdir.
    ${IMAGE_CMD_TAR} --sparse --numeric-owner \
        --transform="s,^\./,${IMAGE_BASENAME}-${MACHINE}/," \
        -cf- -C "${QCOMFLASH_VIP_DIR}" . \
        | pigz -p ${BB_NUMBER_THREADS} -9 -n --rsyncable \
        > "${IMGDEPLOYDIR}/${IMAGE_NAME}.qcomflash-vip.tar.gz"

    ln -sf "${IMAGE_NAME}.qcomflash-vip.tar.gz" \
        "${IMGDEPLOYDIR}/${IMAGE_LINK_NAME}.qcomflash-vip.tar.gz"
}

create_qcomflash_vip_pkg[vardepsexclude] += "BB_NUMBER_THREADS DATETIME"