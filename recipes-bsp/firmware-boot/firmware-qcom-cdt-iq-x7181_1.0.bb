DESCRIPTION = "CDT (Configuration Data Table) Firmware for Qualcomm IQ-X7181 (Hamoa) platform"
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/LICENSE.qcom-2;md5=165287851294f2fb8ac8cbc5e24b02b0"

CDTBINARIES = "X1E80100/cdt"

SRC_URI = " \
    https://${CDT_ARTIFACTORY}/${CDTBINARIES}/LICENSE.qcom-2;name=license \
    https://${CDT_ARTIFACTORY}/${CDTBINARIES}/IQ-X.1.2-EVK-CDT.tar.gz;downloadfilename=cdt-iq-x7181-evk_${PV}.tar.gz;name=cdt-iq-x7181-evk \
    "
SRC_URI[license.sha256sum] = "8401a2253b19272c59537567194d0b264e7ef466379334ff6cea0beec7b8165d"
SRC_URI[cdt-iq-x7181-evk.sha256sum] = "279c47ff8f1a7f4300d296fcb7fbb3d025d903e4c16f62fbb74939804949584e"

QCOM_CDT_SUBDIR ?= "iq-x7181"

include firmware-qcom-cdt-common.inc
