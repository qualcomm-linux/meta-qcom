#
# Copyright (c) 2026 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear
#
# Sign the images of a firmware recipe in place, between do_install and
# do_deploy/do_package, when QCOM_FIRMWARE_SIGN_ENABLE is "1".

inherit qcom-sign-common

# The recipes are allarch unless signing is on, which binds the images to
# the machine's Security Profile, keys and fuse ids and so packages them
# per machine.  Recipes that opt out of allarch get the tune arch when
# unsigned.  Hash the value, not the variables it is picked from, so
# MACHINE does not reach the task hashes of an unsigned build.
inherit_defer ${@'allarch' if d.getVar('QCOM_FIRMWARE_SIGN_ENABLE') != '1' else ''}
QCOM_FIRMWARE_SIGN_PACKAGE_ARCH ?= "${@d.getVar('MACHINE_ARCH') if d.getVar('QCOM_FIRMWARE_SIGN_ENABLE') == '1' else d.getVar('TUNE_PKGARCH')}"
QCOM_FIRMWARE_SIGN_PACKAGE_ARCH[vardepvalue] = "${QCOM_FIRMWARE_SIGN_PACKAGE_ARCH}"
PACKAGE_ARCH = "${QCOM_FIRMWARE_SIGN_PACKAGE_ARCH}"

# Directories whose images are signed in place.  Recipes point this at
# wherever their images live; the default suits a recipe that stages
# images for signing between do_compile and do_deploy.
QCOM_FIRMWARE_SIGN_DIRS ?= "${B}/firmware-to-sign"

# File suffixes to sign.
QCOM_FIRMWARE_SIGN_SUFFIXES ?= "mbn elf"

# Sign every file with a QCOM_FIRMWARE_SIGN_SUFFIXES suffix under $1.
qcom_firmware_sign_dir() {
    dir="$1"
    set --
    for s in ${QCOM_FIRMWARE_SIGN_SUFFIXES}; do
        set -- "$@" -o -iname "*.${s}"
    done
    shift

    bbnote "qcom-firmware-sign: signing under ${dir}"
    find "${dir}" -type f \( "$@" \) | while read -r f; do
        if ! qcom_sign_verify_file "${f}"; then
            bbfatal "Failed to sign: ${f}"
        fi
    done
}

do_qcom_firmware_sign() {
    qcom_firmware_sign_check_prereqs

    for dir in ${QCOM_FIRMWARE_SIGN_DIRS}; do
        if [ -d "${dir}" ]; then
            qcom_firmware_sign_dir "${dir}"
        fi
    done
}

do_qcom_firmware_sign[vardeps] += "\
    QCOM_FIRMWARE_SIGN_ENABLE \
    QCOM_FIRMWARE_SIGN_SECPROFILE \
    QCOM_FIRMWARE_SIGN_SECPROFILE_DIR \
    QCOM_FIRMWARE_SIGN_ANTI_ROLLBACK \
    QCOM_FIRMWARE_SIGN_DIRS \
    QCOM_FIRMWARE_SIGN_SUFFIXES \
    QCOM_FIRMWARE_SIGN_IMAGE_ID_MAP \
    QCOM_FUSE_OEM_HW_ID \
    QCOM_FUSE_OEM_PRODUCT_ID \
    QCOM_FUSE_SEC_KEY_DERIVATION_KEY \
"

# The task always exists and is noexec unless signing is on, which also
# keeps its prefuncs and postfuncs from running.  bitbake only accepts "1"
# for the noexec flag, so it is cleared from anonymous python rather than
# set from an expression.  Runs under pseudo so files rewritten in ${D}
# keep their ownership.  The key material is hashed by content, so keys
# rotated in place under the same path re-sign instead of reusing the
# previously signed images from sstate.
addtask qcom_firmware_sign after do_install before do_deploy do_package do_populate_sysroot
do_qcom_firmware_sign[noexec] = "1"
do_qcom_firmware_sign[fakeroot] = "1"
do_qcom_firmware_sign[depends] += "virtual/fakeroot-native:do_populate_sysroot"
do_qcom_firmware_sign[file-checksums] += "${@qcom_firmware_sign_key_checksums(d)}"

python () {
    if d.getVar('QCOM_FIRMWARE_SIGN_ENABLE') == '1':
        d.delVarFlag('do_qcom_firmware_sign', 'noexec')
}
