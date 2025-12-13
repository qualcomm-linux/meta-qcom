DESCRIPTION = "QCOM NHLOS Firmware for Qualcomm IQ-X7181 platform"
LICENSE = "Proprietary"
# Speicify the license in recipe folder, it will be part of tarball in next release.
LIC_FILES_CHKSUM = "file://${THISDIR}/files/license/Qualcomm-Technologies-Inc.-Proprietary;md5=58d50a3d36f27f1a1e6089308a49b403"

include firmware-qcom-boot-common.inc

BOOTBINARIES = "IQ-X.1.0-Ver.${PV}-ubuntu-X1E80100-nhlos-bins"
S = "${UNPACKDIR}/${BOOTBINARIES}/spinor"

SRC_URI = " \
    https://artifacts.codelinaro.org/artifactory/qli-ci/flashable-binaries/ubuntu-fw/X1E80100/IQ-X.1.0-Ver.${PV}/IQ-X.1.0-Ver.${PV}-ubuntu-X1E80100-nhlos-bins.tar.gz;downloadfilename=${BOOTBINARIES}.tar.gz;name=bootbinaries \
    https://artifacts.codelinaro.org/artifactory/codelinaro-le/Qualcomm_Linux/X1E80100/cdt/IQ-X.1.0-EVK-CDT.tar.gz;downloadfilename=cdt-iq-x7181-evk_${PV}.tar.gz;name=cdt-iq-x7181-evk \
    "
SRC_URI[cdt-iq-x7181-evk.sha256sum] = "d55cfc97c810582cdfc88c77b0fbe516cd77468ea40ab88f7e0cd53cadd9df12"

QCOM_BOOT_IMG_SUBDIR = "iq-x7181/spinor"

do_install() {
    find "${UNPACKDIR}" -name '*CDT.bin' -exec install -m 0644 {} ${UNPACKDIR}/cdt.bin \;

}
