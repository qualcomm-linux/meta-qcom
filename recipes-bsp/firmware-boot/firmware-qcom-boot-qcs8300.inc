DESCRIPTION = "QCOM NHLOS Firmware for Qualcomm QCS8300 platform"
LICENSE = "LicenseRef-LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/${BOOTBINARIES}/LICENSE.qcom-2;md5=165287851294f2fb8ac8cbc5e24b02b0"

FW_ARTIFACTORY = "softwarecenter.qualcomm.com/nexus/generic/product/chip/tech-package/QCS8300_bootbinaries.1.0/qcs8300_bootbinaries.1.0-test-device-public"
BOOTBINARIES = "QCS8300_bootbinaries"

SRC_URI = " \
    https://${FW_ARTIFACTORY}/${PV}/${BOOTBINARIES}_${PV}.zip;name=bootbinaries \
    https://${FW_ARTIFACTORY}/${PV}/${BOOTBINARIES_IMMUTABLE}_${PV}.zip;name=bootbinaries-immutable \
    "

QCOM_BOOT_IMG_SUBDIR = "qcs8300"
QCOM_BOOT_SAIL_SUBDIR = "sail_nor"

include firmware-qcom-boot-common.inc
