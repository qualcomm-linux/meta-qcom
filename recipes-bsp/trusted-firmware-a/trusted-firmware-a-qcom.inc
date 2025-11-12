SRCBRANCH = "qcom-next"
SRC_URI = "git://github.com/qualcomm-linux/trusted-firmware-a.git;protocol=https;name=tfa;branch=${SRCBRANCH}"
SRCREV_tfa = "164d443c8e92c18fd2cca03661dd1a2b9ac37848"
LIC_FILES_CHKSUM = "file://docs/license.rst;md5=6ed7bace7b0bc63021c6eba7b524039e"

DEPENDS += "qtestsign-native"

MACHINE_TFA_QCOM_REQUIRE ?= ""
MACHINE_TFA_QCOM_REQUIRE:qcm6490 = "trusted-firmware-a-qcm6490.inc"

require ${MACHINE_TFA_QCOM_REQUIRE}

do_install:append:qcm6490() {
    export CRYPTOGRAPHY_OPENSSL_NO_LEGACY=1

    ${OBJCOPY} -I binary -B aarch64 -O elf64-littleaarch64 ${D}/firmware/fip.bin ${D}/firmware/fip.o
    ${LD} ${D}/firmware/fip.o -o ${D}/firmware/fip_unsigned.elf -EL -T ${S}/tools/qti/fip-elf.lds --defsym=ELFENTRY=0x9fc00000 -Ttext=0x9fc00000
    rm -f ${D}/firmware/fip.o

    qtestsign -v6 aboot -o ${D}/firmware/fip.elf ${D}/firmware/fip_unsigned.elf
    rm -f ${D}/firmware/fip_unsigned.elf
}
