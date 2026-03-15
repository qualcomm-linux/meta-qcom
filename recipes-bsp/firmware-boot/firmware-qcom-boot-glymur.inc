DESCRIPTION = "QCOM NHLOS Firmware for Qualcomm GLYMUR-CRD platform"
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/LICENSE.${BPN};md5=6e1bae7ef13289c332a27b917fb49764"

FW_ARTIFACTORY = "softwarecenter.qualcomm.com/nexus/generic/software/chip/qualcomm_linux-spf-0-0/qualcomm-linux-spf-0-0_test_device_public"
FW_BUILD_ID = "r0.0_${PV}/glymur-le-0-0"
FW_BIN_PATH = "common/build/bin"
BOOTBINARIES = "Glymur_bootbinaries"

S = "${UNPACKDIR}/${BOOTBINARIES}/spinor"

SRC_URI = " \
    https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/${FW_BIN_PATH}/${BOOTBINARIES}.zip;downloadfilename=${BOOTBINARIES}_r0.0_${PV}.zip;name=bootbinaries \
    https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/LICENSE.txt;downloadfilename=LICENSE.${BPN};name=license \
    https://artifacts.codelinaro.org/artifactory/codelinaro-le/Qualcomm_Linux/SC8480XP/cdt/sc8480xp-crd.zip;downloadfilename=sc8480xp-crd_${PV}.zip;name=sc8480xp-crd \
    "
SRC_URI[license.sha256sum] = "3ad8f1fd82f2918c858cec2d0887b7df6f71a06416beecfdb3efe7d62874d863"
SRC_URI[sc8480xp-crd.sha256sum] = "d694eedb0addcc5ee588d6993661cd23996fba1d1a43afc3b196dad12534bffc"

QCOM_BOOT_IMG_SUBDIR = "glymur-crd/spinor"

include firmware-qcom-boot-common.inc
