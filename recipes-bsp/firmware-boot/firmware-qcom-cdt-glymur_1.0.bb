DESCRIPTION = "CDT (Configuration Data Table) Firmware for Qualcomm Glymur platform"
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/LICENSE.qcom-2;md5=165287851294f2fb8ac8cbc5e24b02b0"

CDTBINARIES = "SC8480XP/cdt"

SRC_URI = " \
    https://${CDT_ARTIFACTORY}/${CDTBINARIES}/LICENSE.qcom-2;name=license \
    https://${CDT_ARTIFACTORY}/${CDTBINARIES}/sc8480xp-crd.zip;downloadfilename=sc8480xp-crd_${PV}.zip;name=sc8480xp-crd \
    "
SRC_URI[license.sha256sum] = "8401a2253b19272c59537567194d0b264e7ef466379334ff6cea0beec7b8165d"
SRC_URI[sc8480xp-crd.sha256sum] = "d694eedb0addcc5ee588d6993661cd23996fba1d1a43afc3b196dad12534bffc"

QCOM_CDT_SUBDIR ?= "glymur-crd"

include firmware-qcom-cdt-common.inc
