require recipes-bsp/trusted-firmware-a/trusted-firmware-a.inc

DEPENDS += "qtestsign-native"

# u-boot-qcom is only needed to pull u-boot.bin into the FIP. The SPL FIT flow
# builds no FIP, and u-boot-qcom depends on this recipe for bl31.bin, so keeping
# the dependency both ways would make bitbake reject the task graph.
DEPENDS += "${@bb.utils.contains('QCOM_UBOOT_SPL_FIT', '1', '', 'u-boot-qcom', d)}"

DEPENDS += "${@'swiv-build-utility-native' if d.getVar('QCOM_FW_SWIV_PLATFORM') else ''}"

PV = "2.15.0-qcom-20260930+git"

SRC_TAG = "tag=qcom-next-2.15-20260930"
SRC_URI = "git://github.com/qualcomm-linux/trusted-firmware-a.git;protocol=https;name=tfa;nobranch=1;${SRC_TAG}"
SRCREV_tfa = "75ed48a34562c861b624017f1909fadb54271dda"

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
    if [ "${QCOM_UBOOT_SPL_FIT}" != "1" ]; then
        export CRYPTOGRAPHY_OPENSSL_NO_LEGACY=1

        ${OBJCOPY} -I binary -B aarch64 -O elf64-littleaarch64 ${D}${FIRMWARE_DIR}/fip.bin ${D}${FIRMWARE_DIR}/fip.o
        ${LD} ${D}${FIRMWARE_DIR}/fip.o -o ${D}${FIRMWARE_DIR}/fip_unsigned.elf -EL -T ${S}/tools/qti/fip-elf.lds --defsym=ELFENTRY=${FIP_ELF_ADDR} -Ttext=${FIP_ELF_ADDR}
        rm -f ${D}${FIRMWARE_DIR}/fip.o

        qtestsign -v6 aboot -o ${D}${FIRMWARE_DIR}/fip.elf ${D}${FIRMWARE_DIR}/fip_unsigned.elf
        rm -f ${D}${FIRMWARE_DIR}/fip_unsigned.elf

        bl2_elf=${D}${FIRMWARE_DIR}/bl2.elf
        if [ -n "${QCOM_FW_SWIV_PLATFORM}" ]; then
            swiv_build_utility ${D}${FIRMWARE_DIR}/bl2-swiv.elf ${D}${FIRMWARE_DIR}/bl2.elf ${QCOM_FW_SWIV_PLATFORM}
            bl2_elf=${D}${FIRMWARE_DIR}/bl2-swiv.elf
        fi
        qtestsign -v6 tz -o ${D}${FIRMWARE_DIR}/bl2.mbn $bl2_elf
        rm -f ${D}${FIRMWARE_DIR}/bl2-swiv.elf
    fi
}

FILES:${PN} += "${FIRMWARE_DIR}/*.mbn"
