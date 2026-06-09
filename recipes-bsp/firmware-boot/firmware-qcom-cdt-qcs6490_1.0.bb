DESCRIPTION = "CDT (Configuration Data Table) Firmware for Qualcomm QCS6490 platform"
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/LICENSE.qcom-2;md5=165287851294f2fb8ac8cbc5e24b02b0"

CDTBINARIES = "QCS6490/cdt"

SRC_URI = " \
    https://${CDT_ARTIFACTORY}/${CDTBINARIES}/LICENSE.qcom-2;name=license \
    https://${CDT_ARTIFACTORY}/${CDTBINARIES}/qcm6490-idp.zip;downloadfilename=cdt-qcm6490-idp_${PV}.zip;name=qcm6490-idp \
    https://${CDT_ARTIFACTORY}/${CDTBINARIES}/rb3gen2-core-kit.zip;downloadfilename=cdt-rb3gen2-core-kit_${PV}.zip;name=rb3gen2-core-kit \
    https://${CDT_ARTIFACTORY}/${CDTBINARIES}/rb3gen2-industrial-kit.zip;downloadfilename=cdt-rb3gen2-industrial-kit_${PV}.zip;name=rb3gen2-industrial-kit \
    https://${CDT_ARTIFACTORY}/${CDTBINARIES}/rb3gen2-vision-kit.zip;downloadfilename=cdt-rb3gen2-vision-kit_${PV}.zip;name=rb3gen2-vision-kit \
    "
SRC_URI[license.sha256sum] = "8401a2253b19272c59537567194d0b264e7ef466379334ff6cea0beec7b8165d"
SRC_URI[qcm6490-idp.sha256sum] = "32226891c51fe6f2bf8def4be66e614bf2994bffaf0dac343b9baa05f7829e11"
SRC_URI[rb3gen2-core-kit.sha256sum] = "0fe1c0b4050cf54203203812b2c1f0d9698823d8defc8b6516414a4e5e0c557e"
SRC_URI[rb3gen2-industrial-kit.sha256sum] = "6cf70a1b9eb0ff27176bb77c679d519f58fbad2cdf2fd7bec1e305c1bf52c013"
SRC_URI[rb3gen2-vision-kit.sha256sum] = "a339e297b454c4dc3805fe8cd11d6d8dcb801aa8f0c2dc691561c2785019fa3c"

QCOM_CDT_SUBDIR ?= "qcm6490"

include firmware-qcom-cdt-common.inc
