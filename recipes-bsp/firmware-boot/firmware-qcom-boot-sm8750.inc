DESCRIPTION = "QCOM NHLOS Firmware for Qualcomm SM8750-MTP platform"
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/LICENSE.${BPN};md5=6e1bae7ef13289c332a27b917fb49764"

FW_ARTIFACTORY = "softwarecenter.qualcomm.com/nexus/generic/software/chip/qualcomm_linux-spf-0-0/qualcomm-linux-spf-0-0_test_device_public"
FW_BUILD_ID = "r0.0_${PV}/pakala-le-0-0"
FW_BIN_PATH = "common/build/ufs/bin"
BOOTBINARIES = "pakala_bootbinaries"

SRC_URI = " \
    https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/${FW_BIN_PATH}/${BOOTBINARIES}.zip;downloadfilename=${BOOTBINARIES}_r0.0_${PV}.zip;name=bootbinaries \
    https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/LICENSE.txt;downloadfilename=LICENSE.${BPN};name=license \
    https://artifacts.codelinaro.org/artifactory/codelinaro-le/Qualcomm_Linux/SM8750/cdt/sm8750-mtp_wcn7881.zip;downloadfilename=sm8750-mtp_wcn7881_${PV}.zip;name=sm8750-mtp_wcn7881 \
    "
SRC_URI[license.sha256sum] = "3ad8f1fd82f2918c858cec2d0887b7df6f71a06416beecfdb3efe7d62874d863"
SRC_URI[sm8750-mtp_wcn7881.sha256sum] = "474c35a6f06948808be118a8b0dac61ca53fd71182c1f03b92ca30d34dbb8a58"

QCOM_BOOT_IMG_SUBDIR = "sm8750"

include firmware-qcom-boot-common.inc
