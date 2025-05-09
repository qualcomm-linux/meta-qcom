DESCRIPTION = "QCOM NHLOS Firmware for Qualcomm QCS615 platform"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/${BOOTBINARIES}/Qualcomm-Technologies-Inc.-Proprietary;md5=58d50a3d36f27f1a1e6089308a49b403"

FW_ARTIFACTORY = "softwarecenter.qualcomm.com/download/software/chip/qualcomm_linux-spf-1-0/qualcomm-linux-spf-1-0_test_device_public"
FW_BUILD_ID = "r1.0_${PV}/qcs615-le-1-0"
FW_BIN_PATH = "common/build/common/bin"
BOOTBINARIES = "QCS615_bootbinaries"

SRC_URI = " \
    https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/${FW_BIN_PATH}/${BOOTBINARIES}.zip;downloadfilename=${BOOTBINARIES}_r1.0_${PV}.zip;name=bootbinaries \
    "
SRC_URI[bootbinaries.sha256sum] = "a63531cd711c4d8ac5a1aaa90a3152f8e89ba37866464b46fd19285ae3940029"

QCOM_BOOT_IMG_SUBDIR = "qcs615"

include firmware-qcom-boot-common.inc
