DESCRIPTION = "QCOM NHLOS Firmware for Qualcomm QCS615 platform"
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/LICENSE.${BPN};md5=6e1bae7ef13289c332a27b917fb49764"

FW_ARTIFACTORY = "softwarecenter.qualcomm.com/nexus/generic/product/chip/software-product-family/Qualcomm_Linux.SPF.2.0/qualcomm_linux.spf.2.0-test-device-public"
BUILD_ID = "${@(d.getVar('PV')).rsplit('-', 1)[0].replace('-', '_')}"
SPIN_ID = "${@d.getVar('PV').rsplit('-', 1)[-1]}"
FW_BUILD_ID = "${BUILD_ID}/QCS615.LE.2.0"
FW_BIN_PATH = "common/build/common/bin"
BOOTBINARIES = "QCS615_bootbinaries"

SRC_URI = " \
    https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/${FW_BIN_PATH}/${BOOTBINARIES}_${SPIN_ID}.zip;downloadfilename=${BOOTBINARIES}_${PV}.zip;name=bootbinaries \
    https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/LICENSE.txt;downloadfilename=LICENSE.${BPN};name=license \
    https://artifacts.codelinaro.org/artifactory/codelinaro-le/Qualcomm_Linux/QCS615/cdt/ADP_AIR_SA6155P_V2.zip;downloadfilename=cdt-qcs615-adp-air_${PV}.zip;name=qcs615-adp-air \
    "
SRC_URI[qcs615-adp-air.sha256sum] = "37d99eb113e286400bce0d70aa12a74d05f93d01f045bf67e7a46b3c606c8fd0"
SRC_URI[license.sha256sum] = "3ad8f1fd82f2918c858cec2d0887b7df6f71a06416beecfdb3efe7d62874d863"

QCOM_BOOT_IMG_SUBDIR = "qcs615"

include firmware-qcom-boot-common.inc
