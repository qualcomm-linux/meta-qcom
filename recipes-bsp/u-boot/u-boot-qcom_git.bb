require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

DEPENDS += "bc-native dtc-native gnutls-native python3-pyelftools-native qtestsign-native xxd-native"

COMPATIBLE_MACHINE:aarch64 = "(qcom)"

PV = "2026.01+2026.04-rc1+git"

# commit used from qcom-next branch
SRCREV = "387b3e2f37a06337789531ffaaf3ac3e3c573ce2"

SRCBRANCH = "nobranch=1"
SRCBRANCH:class-devupstream = "branch=qcom-next"

SRC_URI = "git://github.com/qualcomm-linux/u-boot.git;${SRCBRANCH};protocol=https;name=uboot"

# To build tip of qcom-next branch set preferred
# virtual/bootloader provider to 'u-boot-qcom-upstream'
BBCLASSEXTEND = "devupstream:target"
PN:class-devupstream = "u-boot-qcom-upstream"
SRCREV:class-devupstream = "${AUTOREV}"

UBOOT_MBN_HEADER_VERSION ?= "6"

uboot_compile_config:append:qcom() {
    export CRYPTOGRAPHY_OPENSSL_NO_LEGACY=1 
    qtestsign -v${UBOOT_MBN_HEADER_VERSION} aboot -o ${B}/${builddir}/u-boot.mbn ${B}/${builddir}/u-boot.elf
}

uboot_deploy_config:append:qcom() {
    install -m 0644 ${B}/${builddir}/u-boot.mbn ${DEPLOYDIR}/u-boot.mbn
}
