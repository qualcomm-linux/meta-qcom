DESCRIPTION = "QCOM NHLOS Firmware for Qualcomm Robotics RB3Gen2 platform"
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/LICENSE.${BPN};md5=6e1bae7ef13289c332a27b917fb49764"

FW_ARTIFACTORY = "softwarecenter.qualcomm.com/nexus/generic/product/chip/software-product-family/Qualcomm_Linux.SPF.2.0/qualcomm_linux.spf.2.0-test-device-public"
BUILD_ID = "${@(d.getVar('PV')).rsplit('-', 1)[0].replace('-', '_')}"
SPIN_ID = "${@d.getVar('PV').rsplit('-', 1)[-1]}"
FW_BUILD_ID = "${BUILD_ID}/QCM6490.LE.2.0"
FW_BIN_PATH = "common/build/ufs/bin"
BOOTBINARIES = "QCM6490_bootbinaries"

SRC_URI = " \
    https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/${FW_BIN_PATH}/${BOOTBINARIES}_${SPIN_ID}.zip;downloadfilename=${BOOTBINARIES}_${PV}.zip;name=bootbinaries \
    https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/LICENSE.txt;downloadfilename=LICENSE.${BPN};name=license \
    https://artifacts.codelinaro.org/artifactory/codelinaro-le/Qualcomm_Linux/QCS6490/cdt/qcm6490-idp.zip;downloadfilename=cdt-qcm6490-idp_${PV}.zip;name=qcm6490-idp \
    https://artifacts.codelinaro.org/artifactory/codelinaro-le/Qualcomm_Linux/QCS6490/cdt/rb3gen2-core-kit.zip;downloadfilename=cdt-rb3gen2-core-kit_${PV}.zip;name=rb3gen2-core-kit \
    https://artifacts.codelinaro.org/artifactory/codelinaro-le/Qualcomm_Linux/QCS6490/cdt/rb3gen2-industrial-kit.zip;downloadfilename=cdt-rb3gen2-industrial-kit_${PV}.zip;name=rb3gen2-industrial-kit \
    https://artifacts.codelinaro.org/artifactory/codelinaro-le/Qualcomm_Linux/QCS6490/cdt/rb3gen2-vision-kit.zip;downloadfilename=cdt-rb3gen2-vision-kit_${PV}.zip;name=rb3gen2-vision-kit \
    "
SRC_URI[qcm6490-idp.sha256sum] = "32226891c51fe6f2bf8def4be66e614bf2994bffaf0dac343b9baa05f7829e11"
SRC_URI[rb3gen2-core-kit.sha256sum] = "0fe1c0b4050cf54203203812b2c1f0d9698823d8defc8b6516414a4e5e0c557e"
SRC_URI[rb3gen2-industrial-kit.sha256sum] = "6cf70a1b9eb0ff27176bb77c679d519f58fbad2cdf2fd7bec1e305c1bf52c013"
SRC_URI[rb3gen2-vision-kit.sha256sum] = "a339e297b454c4dc3805fe8cd11d6d8dcb801aa8f0c2dc691561c2785019fa3c"
SRC_URI[license.sha256sum] = "3ad8f1fd82f2918c858cec2d0887b7df6f71a06416beecfdb3efe7d62874d863"

QCOM_BOOT_IMG_SUBDIR = "qcm6490"

include firmware-qcom-boot-common.inc
