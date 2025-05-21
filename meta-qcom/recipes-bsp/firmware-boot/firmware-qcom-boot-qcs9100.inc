DESCRIPTION = "QCOM NHLOS Firmware for Qualcomm QCS9100 platform"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://Qualcomm-Technologies-Inc.-Proprietary;md5=58d50a3d36f27f1a1e6089308a49b403"

FW_ARTIFACTORY = "softwarecenter.qualcomm.com/download/software/chip/qualcomm_linux-spf-1-0/qualcomm-linux-spf-1-0_test_device_public"
FW_BUILD_ID = "r1.0_${PV}/qcs9100-le-1-0"
FW_BIN_PATH = "common/build/ufs/bin"
BOOTBINARIES = "QCS9100_bootbinaries"

SRC_URI = " \
    https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/${FW_BIN_PATH}/${BOOTBINARIES}.zip;downloadfilename=${BOOTBINARIES}_r1.0_${PV}.zip;name=bootbinaries \
    https://artifacts.codelinaro.org/artifactory/codelinaro-le/Qualcomm_Linux/QCS9100/cdt/ride-sx_v3.zip;downloadfilename=cdt-qcs9100-ride-sx-v3_${PV}.zip;name=qcs9100-ride-sx \
    https://artifacts.codelinaro.org/artifactory/codelinaro-le/Qualcomm_Linux/QCS9100/cdt/rb8_core_kit.zip;downloadfilename=cdt-qcs9100-rb8-core-kit_${PV}.zip;name=qcs9100-rb8-ck \
    "
SRC_URI[qcs9100-rb8-ck.sha256sum] = "a252244f800d7c9e15883e12935af4113f9f2ecba6490e46cd9b943169f15bfa"
SRC_URI[qcs9100-ride-sx.sha256sum] = "377a8405899ac82199deaf70bca3648c15b924a3fcef8f109555e661ed70f4b9"

QCOM_BOOT_IMG_SUBDIR = "qcs9100"

include firmware-qcom-boot-common.inc

do_deploy:append() {
    if [ -d "${UNPACKDIR}/${BOOTBINARIES}/sail_nor" ]; then
        SAIL_FILES="gpt_backup0.bin gpt_main0.bin prog_firehose_ddr.elf patch0.xml rawprogram0.xml sailfreertos.elf"

        install -d ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}/sail_nor
        for sail_file in ${SAIL_FILES}; do
            install -m 0644 ${UNPACKDIR}/${BOOTBINARIES}/sail_nor/${sail_file} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}/sail_nor
        done
    fi
}
