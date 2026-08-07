DEPENDS += "qtestsign-native optee-os-qcom u-boot-qcom"

SRC_URI += "git://github.com/coreboot/qc_blobs.git;protocol=https;name=qc_blobs;subdir=qc_blobs;branch=main"
SRCREV_qc_blobs = "16207f367ddbf280a0c2c8a3d3ee454a59710a25"

LICENSE += "& LICENSE.qcom"
LIC_FILES_CHKSUM:qcm6490 += "file://${UNPACKDIR}/qc_blobs/sc7280/qtiseclib/LICENSE;md5=fa83f30385e617b56ef0934f13645621"

COMPATIBLE_MACHINE = "qcm6490|qcs9100"
TFA_PLATFORM:qcm6490 = "rb3gen2"
TFA_PLATFORM:qcs9100 = "lemans_evk"

TFA_BUILD_TARGET = "bl2 bl31 fip"
TFA_SPD = "opteed"

EXTRA_OEMAKE:append = " \
    BL32=${RECIPE_SYSROOT}/${nonarch_base_libdir}/firmware/tee-raw.bin \
    BL33=${DEPLOY_DIR_IMAGE}/u-boot.bin \
   "

EXTRA_OEMAKE:append:qcm6490 = " \
    QTISECLIB_PATH=${UNPACKDIR}/qc_blobs/sc7280/qtiseclib/libqtisec.a \
   "

do_compile[depends] += " u-boot-qcom:do_deploy"

FIP_ELF_ADDR:qcm6490 = "0x9fc00000"
FIP_ELF_ADDR:qcs9100 = "0xaf000000"

do_install:append() {
    export CRYPTOGRAPHY_OPENSSL_NO_LEGACY=1

    ${OBJCOPY} -I binary -B aarch64 -O elf64-littleaarch64 ${D}${FIRMWARE_DIR}/fip.bin ${D}${FIRMWARE_DIR}/fip.o
    ${LD} ${D}${FIRMWARE_DIR}/fip.o -o ${D}${FIRMWARE_DIR}/fip_unsigned.elf -EL -T ${S}/tools/qti/fip-elf.lds --defsym=ELFENTRY=${FIP_ELF_ADDR} -Ttext=${FIP_ELF_ADDR}
    rm -f ${D}${FIRMWARE_DIR}/fip.o

    qtestsign -v6 aboot -o ${D}${FIRMWARE_DIR}/fip.elf ${D}${FIRMWARE_DIR}/fip_unsigned.elf
    rm -f ${D}${FIRMWARE_DIR}/fip_unsigned.elf
}
