require recipes-bsp/lk/lk-toolchain.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5a1abdab641eec675725c843f43f03af"

LK_BRANCH ?= "master"
LK_URI ?= "git://git.codelinaro.org/linaro/qcomlt/lk.git;protocol=https;branch=${LK_BRANCH};name=lk"
LK_REVISION ?= "179191bda57a555ca1803e157a03d2f0d33fa3c5"
LK_SOC ?= "msm8916"
# This LK tree predates GCC 10 (-fno-common) and GCC 14 (implicit declarations
# and int/pointer conversions became errors).
LK_BUILDARGS ?= "EMMC_BOOT=1 TOOLCHAIN_PREFIX=arm-none-eabi- CC='arm-none-eabi-gcc -fcommon -fpermissive' LD=arm-none-eabi-ld NOECHO="
LK_PV ?= "1.2.7-03810"

LK_MACHINE ?= "none"
LK_FLAVOUR ?= ""

SRC_URI = "${LK_URI}"
SRCREV_lk = "${LK_REVISION}"

EXTRA_OEMAKE = "${LK_SOC} ${LK_BUILDARGS}"

PV = "${LK_PV}+git"

DEPENDS += "gcc-arm-none-eabi-native qtestsign-native"

do_compile:append() {
    export CRYPTOGRAPHY_OPENSSL_NO_LEGACY=1
    qtestsign -v3 aboot -o ${S}/build-${LK_SOC}/emmc_appsboot_signed.mbn ${S}/build-${LK_SOC}/emmc_appsboot.mbn
}

#ALLOW_EMPTY:${PN} = "1"

inherit deploy

do_deploy() {
    install -d ${DEPLOYDIR}/${LK_MACHINE}
    install -D -p -m644 ${S}/build-${LK_SOC}/emmc_appsboot.mbn ${DEPLOYDIR}/${LK_MACHINE}/emmc_appsboot${LK_FLAVOUR}-unsigned.mbn
    install -D -p -m644 ${S}/build-${LK_SOC}/emmc_appsboot_signed.mbn ${DEPLOYDIR}/${LK_MACHINE}/emmc_appsboot${LK_FLAVOUR}.mbn
}

addtask deploy before do_build after do_compile
