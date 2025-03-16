DESCRIPTION = "QCOM NHLOS Firmware for Qualcomm QCS9100 platform"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/${BOOTBINARIES}/Qualcomm-Technologies-Inc.-Proprietary;md5=58d50a3d36f27f1a1e6089308a49b403"

COMPATIBLE_MACHINE = "qcs9100"

FW_ARTIFACTORY = "softwarecenter.qualcomm.com/download/software/chip/qualcomm_linux-spf-1-0/qualcomm-linux-spf-1-0_test_device_public"
FW_BUILD_ID = "r1.0_${PV}/qcs9100-le-1-0"
FW_BIN_PATH = "common/build/ufs/bin"
BOOTBINARIES = "QCS9100_bootbinaries"

SRC_URI = " \
    https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/${FW_BIN_PATH}/${BOOTBINARIES}.zip;downloadfilename=${BOOTBINARIES}_r1.0_${PV}.zip;name=bootbinaries \
    https://artifacts.codelinaro.org/artifactory/codelinaro-le/Qualcomm_Linux/QCS9100/cdt/ride-sx_v3.zip;downloadfilename=cdt-qcs9100-ride-sx-v3_${PV}.zip;name=qcs9100-ride-sx \
    "
SRC_URI[bootbinaries.sha256sum] = "bd024ffe419f13b19907b285d0369bf9dfdf77b7e95052b9e4869957ddcaf07f"
SRC_URI[qcs9100-ride-sx.sha256sum] = "377a8405899ac82199deaf70bca3648c15b924a3fcef8f109555e661ed70f4b9"

include firmware-qcom-boot-common.inc
