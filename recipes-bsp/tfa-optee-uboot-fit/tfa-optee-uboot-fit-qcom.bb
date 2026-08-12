#
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
#
# SPDX-License-Identifier: BSD-3-Clause-Clear
#

SUMMARY = "Qualcomm FIT image (TFA + OP-TEE + U-Boot)"
DESCRIPTION = "Assembles a Flattened Image Tree (ITB) from a manual ITS file that bundles \
BL31 (TF-A), BL32 (OP-TEE) and BL33 (U-Boot) into a single FIT image."
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

SRC_URI = "file://tfa-optee-uboot-spl.its"

inherit deploy nopackages

INHIBIT_DEFAULT_DEPS = "1"
DEPENDS = "u-boot-tools-native"

S = "${UNPACKDIR}"

do_compile[depends] += " \
    trusted-firmware-a-qcom:do_deploy \
    optee-os-qcom:do_deploy \
    u-boot-qcom:do_deploy \
"

MKIMAGE ?= "${STAGING_BINDIR_NATIVE}/mkimage"

FITIMAGE_STAGING_DIR = "${WORKDIR}/fitimage-staging"

do_compile[cleandirs] = "${FITIMAGE_STAGING_DIR}"

do_compile() {
    install -m 0644 "${DEPLOY_DIR_IMAGE}/trusted-firmware-a-qcom/bl31.bin" \
        "${FITIMAGE_STAGING_DIR}/bl31.bin"
    install -m 0644 "${DEPLOY_DIR_IMAGE}/optee/tee-raw.bin" \
        "${FITIMAGE_STAGING_DIR}/tee-raw.bin"
    install -m 0644 "${DEPLOY_DIR_IMAGE}/u-boot.bin" \
        "${FITIMAGE_STAGING_DIR}/u-boot.bin"
    install -m 0644 "${S}/tfa-optee-uboot-spl.its" \
        "${FITIMAGE_STAGING_DIR}/tfa-optee-uboot-spl.its"

    cd "${FITIMAGE_STAGING_DIR}"
    ${MKIMAGE} -f tfa-optee-uboot-spl.its tfa-optee-uboot-spl.itb
}

do_install[noexec] = "1"

do_deploy() {
    install -d "${DEPLOYDIR}"
    install -m 0644 "${FITIMAGE_STAGING_DIR}/tfa-optee-uboot-spl.itb" \
        "${DEPLOYDIR}/tfa-optee-uboot-spl.itb"
    install -m 0644 "${FITIMAGE_STAGING_DIR}/tfa-optee-uboot-spl.its" \
        "${DEPLOYDIR}/tfa-optee-uboot-spl.its"
}

addtask deploy after do_compile before do_build
