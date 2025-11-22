DESCRIPTION = "QCOM NHLOS Firmware for Qualcomm IQ-X7181 platform"

LICENSE = "CLOSED"

BOOTBINARIES = "IQ-X7181_bootbinaries"

SRC_URI = " \
    https://artifacts.codelinaro.org/artifactory/qli-ci/flashable-binaries/ubuntu-fw/X1E80100/IQ-X.1.0-Ver.${PV}/IQ-X.1.0-Ver.${PV}-ubuntu-X1E80100-nhlos-bins.tar.gz;downloadfilename=${BOOTBINARIES}_${PV}.tar.gz;name=bootbinaries \
    https://artifacts.codelinaro.org/artifactory/codelinaro-le/Qualcomm_Linux/X1E80100/cdt/IQ-X.1.0-EVK-CDT.tar.gz;downloadfilename=cdt-iq-x7181-evk_${PV}.tar.gz;name=iq-x7181-evk \
    "
SRC_URI[iq-x7181-evk.sha256sum] = "c8b8891438bb933d59bfae76452699640e28411125798ebc5bc112f4ab003742"

QCOM_BOOT_IMG_SUBDIR = "iq-x7181"

include firmware-qcom-boot-common.inc

do_deploy:append() {
    install -d ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}/spinor
    find "${S}/spinor" -maxdepth 1 -name '*.bin' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}/spinor \;
    find "${S}/spinor" -maxdepth 1 -name '*.elf' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}/spinor \;
    find "${S}/spinor" -maxdepth 1 -name '*.fv' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}/spinor \;
    find "${S}/spinor" -maxdepth 1 -name '*.mbn' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}/spinor \;
    find "${S}/spinor" -maxdepth 1 -name '*.melf' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}/spinor \;
    find "${S}/spinor" -maxdepth 1 -name '*.xml' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}/spinor \;
    find "${S}/spinor" -maxdepth 1 -name '*.lzma' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}/spinor \;
    find "${S}/spinor" -maxdepth 1 -name '*.xz' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}/spinor \;

    find "${UNPACKDIR}" -name '*CDT.bin' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}/spinor \;
}

