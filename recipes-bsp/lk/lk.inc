LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5a1abdab641eec675725c843f43f03af"

LK_BRANCH ?= "master"
LK_URI ?= "git://git.codelinaro.org/linaro/qcomlt/lk.git;protocol=https;branch=${LK_BRANCH};name=lk"
LK_REVISION ?= "179191bda57a555ca1803e157a03d2f0d33fa3c5"
LK_SOC ?= "msm8916"
LK_BUILDARGS ?= "EMMC_BOOT=1 TOOLCHAIN_PREFIX=${UNPACKDIR}/gcc/bin/arm-eabi- LD=${UNPACKDIR}/gcc/bin/arm-eabi-ld NOECHO="
LK_PV ?= "1.2.7-03810"

LK_MACHINE ?= "none"
LK_FLAVOUR ?= ""

# Use the "original" gcc for now
LK_GCC_URI ?= "git://git.codelinaro.org/clo/la/platform/prebuilts/gcc/linux-x86/arm/arm-eabi-4.8.git;protocol=https;name=gcc;subdir=gcc;nobranch=1"
#caf_migration/LA.BR.1.1.3.c4
LK_GCC_REVISION = "26e93f6af47f7bd3a9beb5c102a5f45e19bfa38a"

SRC_URI = " \
    ${LK_URI} \
    ${LK_GCC_URI} \
"
SRCREV_lk = "${LK_REVISION}"
SRCREV_gcc = "${LK_GCC_REVISION}"

EXTRA_OEMAKE = "${LK_SOC} ${LK_BUILDARGS}"

SRCREV_FORMAT = "lk_gcc"
PV = "${LK_PV}+git"

DEPENDS += "qtestsign-native"

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
