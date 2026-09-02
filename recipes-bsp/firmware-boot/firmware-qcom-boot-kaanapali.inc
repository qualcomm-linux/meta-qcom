DESCRIPTION = "QCOM NHLOS Firmware for Qualcomm Kaanapali-MTP platform"
LICENSE = "LicenseRef-LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/${BOOTBINARIES}/LICENSE.qcom-2;md5=165287851294f2fb8ac8cbc5e24b02b0"

FW_ARTIFACTORY = "softwarecenter.qualcomm.com/nexus/generic/product/chip/tech-package/Kaanapali_bootbinaries.1.0/kaanapali_bootbinaries.1.0-test-device-public"
BOOTBINARIES = "kaanapali_bootbinaries"

SRC_URI = " \
    https://${FW_ARTIFACTORY}/${PV}/${BOOTBINARIES}_${PV}.zip;name=bootbinaries \
    "

QCOM_BOOT_IMG_SUBDIR = "kaanapali"

include firmware-qcom-boot-common.inc
