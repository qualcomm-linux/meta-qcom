DESCRIPTION = "CDT (Configuration Data Table) Firmware for Qualcomm QCS8300 platform"
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/LICENSE.qcom-2;md5=165287851294f2fb8ac8cbc5e24b02b0"

CDTBINARIES = "QCS8300/cdt"

SRC_URI = " \
    https://${CDT_ARTIFACTORY}/${CDTBINARIES}/LICENSE.qcom-2;name=license \
    https://${CDT_ARTIFACTORY}/${CDTBINARIES}/ride-sx.zip;downloadfilename=cdt-qcs8300-ride-sx_${PV}.zip;name=qcs8300-ride-sx \
    https://${CDT_ARTIFACTORY}/${CDTBINARIES}/qcs8275-iq-8275-evk-pro-sku.zip;downloadfilename=cdt-iq8275-evk-pro-sku_${PV}.zip;name=cdt-iq8275-evk-pro-sku \
    "
SRC_URI[license.sha256sum] = "8401a2253b19272c59537567194d0b264e7ef466379334ff6cea0beec7b8165d"
SRC_URI[qcs8300-ride-sx.sha256sum] = "d7fc667372b28383a36d586333097d84b9d9c104f4dd1845d33904e2d6b39f80"
SRC_URI[cdt-iq8275-evk-pro-sku.sha256sum] = "cbe2009c8ef7dbacd716141bf01b8e1b26788c4a4f3145e60fe3b4a6b3aabc04"

QCOM_CDT_SUBDIR ?= "qcs8300"

include firmware-qcom-cdt-common.inc
