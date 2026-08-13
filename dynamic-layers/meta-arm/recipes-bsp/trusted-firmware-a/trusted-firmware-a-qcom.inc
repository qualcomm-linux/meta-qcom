require recipes-bsp/trusted-firmware-a/trusted-firmware-a.inc

DEPENDS += "qtestsign-native"

# u-boot-qcom is only needed to pull u-boot.bin into the FIP. The SPL FIT flow
# builds no FIP, and u-boot-qcom depends on this recipe for bl31.bin, so keeping
# the dependency both ways would make bitbake reject the task graph.
DEPENDS += "${@bb.utils.contains('QCOM_UBOOT_SPL_FIT', '1', '', 'u-boot-qcom', d)}"

PV = "2.15.0-qcom+git"

SRC_TAG = "tag=qcom-next-2.15-20260804"
SRC_URI = "git://github.com/qualcomm-linux/trusted-firmware-a.git;protocol=https;name=tfa;nobranch=1;${SRC_TAG}"
SRCREV_tfa = "10ecf7dcc641e8453e2e2ea2f21c13c7a867ffe0"

LIC_FILES_CHKSUM += "file://docs/license.rst;md5=6ed7bace7b0bc63021c6eba7b524039e"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(qcom)"

python () {
    if not d.getVar('FIP_ELF_ADDR'):
        bb.fatal('FIP_ELF_ADDR must be set by the device recipe')
}

TFA_BUILD_TARGET = "${@bb.utils.contains('QCOM_UBOOT_SPL_FIT', '1', 'bl31', 'bl2 bl31 fip', d)}"
TFA_SPD = "opteed"

EXTRA_OEMAKE:append = " \
    BL32=${RECIPE_SYSROOT}/${nonarch_base_libdir}/firmware/tee-raw.bin \
    BL33=${DEPLOY_DIR_IMAGE}/u-boot.bin \
   "

do_compile[depends] += "${@bb.utils.contains('QCOM_UBOOT_SPL_FIT', '1', '', 'u-boot-qcom:do_deploy', d)}"

do_install:append() {
    # Nothing to wrap and sign without a FIP.
    if [ "${QCOM_UBOOT_SPL_FIT}" = "1" ]; then
        return
    fi

    export CRYPTOGRAPHY_OPENSSL_NO_LEGACY=1

    ${OBJCOPY} -I binary -B aarch64 -O elf64-littleaarch64 ${D}${FIRMWARE_DIR}/fip.bin ${D}${FIRMWARE_DIR}/fip.o
    ${LD} ${D}${FIRMWARE_DIR}/fip.o -o ${D}${FIRMWARE_DIR}/fip_unsigned.elf -EL -T ${S}/tools/qti/fip-elf.lds --defsym=ELFENTRY=${FIP_ELF_ADDR} -Ttext=${FIP_ELF_ADDR}
    rm -f ${D}${FIRMWARE_DIR}/fip.o

    qtestsign -v6 aboot -o ${D}${FIRMWARE_DIR}/fip.elf ${D}${FIRMWARE_DIR}/fip_unsigned.elf
    rm -f ${D}${FIRMWARE_DIR}/fip_unsigned.elf
}
