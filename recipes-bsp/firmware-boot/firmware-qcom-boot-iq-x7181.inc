DESCRIPTION = "QCOM NHLOS Firmware for Qualcomm IQ-X7181 platform"
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/${BOOTBINARIES}/LICENSE.qcom-2;md5=fd357a091fc044021a9ddf5c1e3b5117"

include firmware-qcom-boot-common.inc

FW_ARTIFACTORY = "softwarecenter.qualcomm.com/nexus/generic/product/chip/tech-package/Hamoa_bootbinaries.1.0/hamoa_bootbinaries.1.0-test-device-public/"
BOOTBINARIES = "HAMOA_bootbinaries"

SRC_URI = " \
    https://${FW_ARTIFACTORY}/${PV}/HAMOA_bootbinaries_${PV}.zip;name=bootbinaries \
    https://${FW_ARTIFACTORY}/${PV}/HAMOA_bootbinaries_immutable_${PV}.zip;name=bootbinaries-immutable \
    "

QCOM_BOOT_IMG_SUBDIR = "iq-x7181/spinor"
