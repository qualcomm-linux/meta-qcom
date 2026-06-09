DESCRIPTION = "CDT (Configuration Data Table) Firmware for Qualcomm Kaanapali platform"
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/LICENSE.qcom-2;md5=165287851294f2fb8ac8cbc5e24b02b0"

CDTBINARIES = "SM8850/cdt"

SRC_URI = " \
    https://${CDT_ARTIFACTORY}/${CDTBINARIES}/LICENSE.qcom-2;name=license \
    https://${CDT_ARTIFACTORY}/${CDTBINARIES}/sm8850-mtp.zip;downloadfilename=sm8850-mtp_${PV}.zip;name=sm8850-mtp \
    "
SRC_URI[license.sha256sum] = "8401a2253b19272c59537567194d0b264e7ef466379334ff6cea0beec7b8165d"
SRC_URI[sm8850-mtp.sha256sum] = "41b15acaa06311c8c7500d7467a5d8adb9fdee05aed09d983d3dad045865b1d7"

QCOM_CDT_SUBDIR ?= "kaanapali"

include firmware-qcom-cdt-common.inc
