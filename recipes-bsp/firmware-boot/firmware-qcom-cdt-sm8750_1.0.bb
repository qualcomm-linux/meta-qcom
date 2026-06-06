DESCRIPTION = "CDT (Configuration Data Table) Firmware for Qualcomm SM8750 platform"
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/LICENSE.qcom-2;md5=165287851294f2fb8ac8cbc5e24b02b0"

CDTBINARIES = "SM8750/cdt"

SRC_URI = " \
    https://${CDT_ARTIFACTORY}/${CDTBINARIES}/LICENSE.qcom-2;name=license \
    https://${CDT_ARTIFACTORY}/${CDTBINARIES}/sm8750-mtp_wcn7881.zip;downloadfilename=sm8750-mtp_wcn7881_${PV}.zip;name=sm8750-mtp_wcn7881 \
    "
SRC_URI[license.sha256sum] = "8401a2253b19272c59537567194d0b264e7ef466379334ff6cea0beec7b8165d"
SRC_URI[sm8750-mtp_wcn7881.sha256sum] = "474c35a6f06948808be118a8b0dac61ca53fd71182c1f03b92ca30d34dbb8a58"

QCOM_CDT_SUBDIR ?= "sm8750"

include firmware-qcom-cdt-common.inc
