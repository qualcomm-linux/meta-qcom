SUMMARY = "U-Boot bootloader with support for Qualcom Computer on Modules"
HOMEPAGE = "https://git.codelinaro.org/linaro/qcomlt/u-boot"
SECTION = "bootloaders"

DESCRIPTION = "U-Boot based on mainline U-Boot used by Qualcomm BSP in \
order to provide support for some backported features and fixes, or because it \
was submitted for revision and it takes some time to become part of a stable \
version, or because it is not applicable for upstreaming."

BASEPV = "2024.10"
PV = "${BASEPV}+git"

require recipes-bsp/u-boot/u-boot.inc
require u-boot-qcom-common_${BASEPV}.inc

DEPENDS += " \
    xxd-native \
    dtc-native \
    qtestsign-native\
"

PROVIDES += "u-boot"

B = "${WORKDIR}/build"

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(qcom-armv8a)"

do_compile:append() {
    export CRYPTOGRAPHY_OPENSSL_NO_LEGACY=1
    qtestsign -v6 aboot -o ${B}/qcm6490_defconfig/u-boot.mbn ${B}/qcm6490_defconfig/u-boot.elf
}

do_deploy:append() {
    cp -r ${B}/qcm6490_defconfig/u-boot.mbn ${DEPLOYDIR}/u-boot-qcs6490-rb3gen2.mbn
    cp -r ${B}/qcm6490_defconfig/u-boot.elf ${DEPLOYDIR}/u-boot-qcs6490-rb3gen2.elf
}