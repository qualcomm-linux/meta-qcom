DESCRIPTION = "QCOM NHLOS Firmware for Qualcomm IQ10 RRD platform"
LICENSE = "LicenseRef-LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/LICENSE.${BPN};md5=165287851294f2fb8ac8cbc5e24b02b0"

FW_ARTIFACTORY = "softwarecenter.qualcomm.com/nexus/generic/product/chip/tech-package/Nord_bootbinaries.1.0/nord_bootbinaries.1.0-test-device-public"
BOOTBINARIES = "NORD_bootbinaries"

SRC_URI = " \
    https://${FW_ARTIFACTORY}/${PV}/${BOOTBINARIES}_${PV}.zip;name=bootbinaries \
    https://${FW_ARTIFACTORY}/${PV}/${BOOTBINARIES_IMMUTABLE}_${PV}.zip;name=bootbinaries-immutable \
    https://${FW_ARTIFACTORY}/${PV}/LICENSE.txt;downloadfilename=LICENSE.${BPN};name=license \
    "
SRC_URI[license.sha256sum] = "8401a2253b19272c59537567194d0b264e7ef466379334ff6cea0beec7b8165d"

QCOM_BOOT_IMG_SUBDIR = "nord"
QCOM_BOOT_SAIL_SUBDIR = "spinor"

include firmware-qcom-boot-common.inc
