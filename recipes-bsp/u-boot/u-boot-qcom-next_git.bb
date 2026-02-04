SUMMARY = "U-Boot bootloader for Qualcomm platforms"
DESCRIPTION = "U-Boot from Qualcomm Linux upstream repository"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=2ca5f2c35c8cc335f0a19756634782f1"

DEPENDS += "bison-native bc-native dtc-native gnutls-native python3-pyelftools-native xxd-native"
DEPENDS += "python3-native python3-cryptography-native openssl-native"

require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

COMPATIBLE_MACHINE = "(qcom)"

PV = "2026.01+git"

# Pinned to specific commit from qcom-next branch
SRCREV ?= "387b3e2f37a06337789531ffaaf3ac3e3c573ce2"

SRCBRANCH ?= "nobranch=1"
SRCBRANCH:class-devupstream ?= "branch=qcom-next"

SRC_URI = " \
    git://github.com/qualcomm-linux/u-boot.git;${SRCBRANCH};protocol=https;name=uboot \
    git://github.com/msm8916-mainline/qtestsign;protocol=https;destsuffix=git/qtestsign;name=qtestsign;branch=main \
"

# Latest commit from qtestsign repo
SRCREV_qtestsign = "${AUTOREV}"
SRCREV_FORMAT = "uboot_qtestsign"

# To build tip of qcom-next branch set preferred
# virtual/bootloader provider to 'u-boot-qcom-next-upstream'
BBCLASSEXTEND = "devupstream:target"
PN:class-devupstream = "u-boot-qcom-next-upstream"
SRCREV:class-devupstream ?= "${AUTOREV}"

S = "${UNPACKDIR}/${BP}"

# Provide virtual/bootloader
PROVIDES += "virtual/bootloader"

# Default config - can be overridden per machine
UBOOT_MACHINE ?= "qcom_defconfig"

# python3-cryptography needs the legacy provider, so set OPENSSL_MODULES to the
# right path until this is relocated automatically.
export OPENSSL_MODULES = "${STAGING_LIBDIR_NATIVE}/ossl-modules"

do_compile:append() {
    # Run the qtestsign script to sign and convert ELF to MBN
    bbnote "Signing U-Boot ELF with qtestsign..."
    ${PYTHON} ${UNPACKDIR}/git/qtestsign/qtestsign.py -v6 aboot -o ${B}/u-boot.mbn ${B}/u-boot.elf

    # Check if u-boot.mbn was created successfully
    if [ ! -f "${B}/u-boot.mbn" ]; then
        bbfatal "Failed to create u-boot.mbn"
    fi
    bbnote "Successfully created u-boot.mbn"
}

do_deploy:append() {
    # Deploy to standard DEPLOYDIR location
    install -m 0644 ${B}/u-boot.mbn ${DEPLOYDIR}/u-boot.mbn

    # Deploy to QCOM_BOOT_FILES_SUBDIR for qcomflash image
    if [ -n "${QCOM_BOOT_FILES_SUBDIR}" ]; then
        install -d ${DEPLOY_DIR_IMAGE}/${QCOM_BOOT_FILES_SUBDIR}
        install -m 0644 ${B}/u-boot.mbn ${DEPLOY_DIR_IMAGE}/${QCOM_BOOT_FILES_SUBDIR}/u-boot.mbn
    fi
}
