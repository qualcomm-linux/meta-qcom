DESCRIPTION = "QCOM NHLOS Firmware for Qualcomm Robotics RB3Gen2 platform"
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/${BOOTBINARIES}/LICENSE.qcom-2;md5=165287851294f2fb8ac8cbc5e24b02b0"

FW_ARTIFACTORY = "softwarecenter.qualcomm.com/nexus/generic/product/chip/tech-package/QCM6490_bootbinaries.1.0/qcm6490_bootbinaries.1.0-test-device-public"
BOOTBINARIES = "QCM6490_bootbinaries"

SRC_URI = " \
    https://${FW_ARTIFACTORY}/${PV}/${BOOTBINARIES}_${PV}.zip;name=bootbinaries \
    "

QCOM_BOOT_IMG_SUBDIR = "qcm6490"

include firmware-qcom-boot-common.inc
