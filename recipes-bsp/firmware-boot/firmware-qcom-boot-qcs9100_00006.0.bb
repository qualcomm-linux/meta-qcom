DESCRIPTION = "QCOM NHLOS Firmware for Qualcomm QCS9100 platform"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/${BOOTBINARIES}/Qualcomm-Technologies-Inc.-Proprietary;md5=58d50a3d36f27f1a1e6089308a49b403"

COMPATIBLE_MACHINE = "qcs9100"

FW_ARTIFACTORY = "softwarecenter.qualcomm.com/download/software/chip/qualcomm_linux-spf-1-0/qualcomm-linux-spf-1-0_test_device_integrationandtest_publictest"
FW_BUILD_ID = "r1.0_${PV}/qcs9100-le-1-0"
FW_BIN_PATH = "common/build/ufs/bin"
BOOTBINARIES = "QCS9100_bootbinaries"

SRC_URI = "https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/${FW_BIN_PATH}/${BOOTBINARIES}.zip;downloadfilename=${BOOTBINARIES}_r1.0_${PV}.zip;name=bootbinaries"
SRC_URI[bootbinaries.sha256sum] = "480682759e27d63b0e44501ae2517b3671bea6dad21071880a22ed5feb5a458b"

SRC_URI:append:sa8775p-ride-sx = " https://artifacts.codelinaro.org/artifactory/codelinaro-le/Qualcomm_Linux/QCS9100/cdt/ride-sx.zip;downloadfilename=cdt-sa8775p-ride-sx_${PV}.zip;name=sa8775p-ride-sx"
SRC_URI[sa8775p-ride-sx.sha256sum] = "f5e37d1260627e9d6976827ea5bdc7ffa81c90b7561acfccf24a94bc1313dea5"

CDT_FILE:sa8775p-ride-sx ?= "cdt_ride_sx"

include firmware-qcom-boot-common.inc
