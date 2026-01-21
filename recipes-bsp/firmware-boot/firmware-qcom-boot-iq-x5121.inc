DESCRIPTION = "QCOM NHLOS Firmware for Qualcomm IQ-X5121 platform"
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/LICENSE.${BPN};md5=6e1bae7ef13289c332a27b917fb49764"

include firmware-qcom-boot-common.inc

FW_ARTIFACTORY = "softwarecenter.qualcomm.com/download/software/chip/ubuntu_qualcomm_iot-spf-1-0/ubuntu-qualcomm-iot-spf-1-0_test_device_public"
#FW_BUILD_ID = "r1.0.r1_${PV}/hamoa_iot-ubun-1-0-r1"
FW_BUILD_ID = "r1.0_00001.0/hamoa_iot-ubun-1-0"
FW_BIN_PATH = "common/build/bin"
BOOTBINARIES = "HAMOA_bootbinaries"

S = "${UNPACKDIR}/${BOOTBINARIES}/spinor"

#SRC_URI = " \
#    https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/${FW_BIN_PATH}/HAMOA_bootbinaries.zip;downloadfilename=HAMOA_bootbinaries_${PV}.zip;name=bootbinaries \
#    https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/LICENSE.txt;downloadfilename=LICENSE.${BPN};name=license \
#    https://artifacts.codelinaro.org/artifactory/codelinaro-le/Qualcomm_Linux/X1E80100/cdt/IQ-X.1.2-EVK-CDT.tar.gz;downloadfilename=cdt-iq-x7181-evk_${PV}.tar.gz;name=cdt-iq-x7181-evk \
#    "
SRC_URI = " \
    https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/HAMOA_bootbinaries.zip;downloadfilename=HAMOA_bootbinaries_${PV}.zip;name=bootbinaries \
    https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/LICENSE.txt;downloadfilename=LICENSE.${BPN};name=license \
    https://artifacts.codelinaro.org/artifactory/codelinaro-le/Qualcomm_Linux/X1E80100/cdt/IQ-X.1.2-EVK-CDT.tar.gz;downloadfilename=cdt-iq-x7181-evk_${PV}.tar.gz;name=cdt-iq-x7181-evk \
    "
SRC_URI[cdt-iq-x7181-evk.sha256sum] = "279c47ff8f1a7f4300d296fcb7fbb3d025d903e4c16f62fbb74939804949584e"
SRC_URI[license.sha256sum] = "3ad8f1fd82f2918c858cec2d0887b7df6f71a06416beecfdb3efe7d62874d863"

QCOM_BOOT_IMG_SUBDIR = "iq-x5121/spinor"
