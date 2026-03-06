DESCRIPTION = "QCOM NHLOS Firmware for Qualcomm QCS9100 platform"
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/LICENSE.${BPN};md5=6e1bae7ef13289c332a27b917fb49764"

FW_ARTIFACTORY = "softwarecenter.qualcomm.com/nexus/generic/product/chip/software-product-family/Qualcomm_Linux.SPF.2.0/qualcomm_linux.spf.2.0-test-device-public"
BUILD_ID = "${@(d.getVar('PV')).rsplit('-', 1)[0].replace('-', '_')}"
SPIN_ID = "${@d.getVar('PV').rsplit('-', 1)[-1]}"
FW_BUILD_ID = "${BUILD_ID}/QCS9100.LE.2.0"
FW_BIN_PATH = "common/build/ufs/bin"
BOOTBINARIES = "QCS9100_bootbinaries"

SRC_URI = " \
    https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/${FW_BIN_PATH}/${BOOTBINARIES}_${SPIN_ID}.zip;downloadfilename=${BOOTBINARIES}_${PV}.zip;name=bootbinaries \
    https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/LICENSE.txt;downloadfilename=LICENSE.${BPN};name=license \
    https://artifacts.codelinaro.org/artifactory/codelinaro-le/Qualcomm_Linux/QCS9100/cdt/ride-sx_v3.zip;downloadfilename=cdt-qcs9100-ride-sx-v3_${PV}.zip;name=qcs9100-ride-sx \
    https://artifacts.codelinaro.org/artifactory/codelinaro-le/Qualcomm_Linux/QCS9100/cdt/rb8_core_kit.zip;downloadfilename=cdt-qcs9100-rb8-core-kit_${PV}.zip;name=qcs9100-rb8-ck \
    "
SRC_URI[qcs9100-rb8-ck.sha256sum] = "a252244f800d7c9e15883e12935af4113f9f2ecba6490e46cd9b943169f15bfa"
SRC_URI[qcs9100-ride-sx.sha256sum] = "377a8405899ac82199deaf70bca3648c15b924a3fcef8f109555e661ed70f4b9"
SRC_URI[license.sha256sum] = "3ad8f1fd82f2918c858cec2d0887b7df6f71a06416beecfdb3efe7d62874d863"

QCOM_BOOT_IMG_SUBDIR = "qcs9100"

include firmware-qcom-boot-common.inc

do_deploy:append() {
    if [ -d "${UNPACKDIR}/${BOOTBINARIES}/sail_nor" ]; then
        SAIL_FILES="gpt_backup0.bin gpt_main0.bin prog_firehose_ddr.elf patch0.xml rawprogram0.xml sailhyp.elf sailsw1.elf"

        install -d ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}/sail_nor
        for sail_file in ${SAIL_FILES}; do
            install -m 0644 ${UNPACKDIR}/${BOOTBINARIES}/sail_nor/${sail_file} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}/sail_nor
        done
    fi
}
