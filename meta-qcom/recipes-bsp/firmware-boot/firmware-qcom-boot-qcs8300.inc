DESCRIPTION = "QCOM NHLOS Firmware for Qualcomm QCS8300 platform"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://Qualcomm-Technologies-Inc.-Proprietary;md5=58d50a3d36f27f1a1e6089308a49b403"

FW_ARTIFACTORY = "softwarecenter.qualcomm.com/download/software/chip/qualcomm_linux-spf-1-0/qualcomm-linux-spf-1-0_test_device_public"
FW_BUILD_ID = "r1.0_${PV}/qcs8300-le-1-0"
FW_BIN_PATH = "common/build/ufs/bin"
BOOTBINARIES = "QCS8300_bootbinaries"

SRC_URI = " \
    https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/${FW_BIN_PATH}/${BOOTBINARIES}.zip;downloadfilename=${BOOTBINARIES}_r1.0_${PV}.zip;name=bootbinaries \
    https://artifacts.codelinaro.org/artifactory/codelinaro-le/Qualcomm_Linux/QCS8300/cdt/ride-sx.zip;downloadfilename=cdt-qcs8300-ride-sx_${PV}.zip;name=qcs8300-ride-sx \
    "
SRC_URI[qcs8300-ride-sx.sha256sum] = "d7fc667372b28383a36d586333097d84b9d9c104f4dd1845d33904e2d6b39f80"

QCOM_BOOT_IMG_SUBDIR = "qcs8300"

include firmware-qcom-boot-common.inc
