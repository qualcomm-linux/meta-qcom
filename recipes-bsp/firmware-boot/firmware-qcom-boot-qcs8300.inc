DESCRIPTION = "QCOM NHLOS Firmware for Qualcomm QCS8300 platform"
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/LICENSE.${BPN};md5=6e1bae7ef13289c332a27b917fb49764"

FW_ARTIFACTORY = "softwarecenter.qualcomm.com/download/software/chip/qualcomm_linux-spf-1-0/qualcomm-linux-spf-1-0_test_device_public"
FW_BUILD_ID = "r1.0_${PV}/qcs8300-le-1-0"
FW_BIN_PATH = "common/build/ufs/bin"
BOOTBINARIES = "QCS8300_bootbinaries"

SRC_URI = " \
    https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/${FW_BIN_PATH}/${BOOTBINARIES}.zip;downloadfilename=${BOOTBINARIES}_r1.0_${PV}.zip;name=bootbinaries \
    https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/LICENSE.txt;downloadfilename=LICENSE.${BPN};name=license \
    https://artifacts.codelinaro.org/artifactory/codelinaro-le/Qualcomm_Linux/QCS8300/cdt/ride-sx.zip;downloadfilename=cdt-qcs8300-ride-sx_${PV}.zip;name=qcs8300-ride-sx \
    https://artifacts.codelinaro.org/artifactory/codelinaro-le/Qualcomm_Linux/QCS8300/cdt/qcs8275-iq-8275-evk-pro-sku.zip;downloadfilename=cdt-iq8275-evk-pro-sku_${PV}.zip;name=cdt-iq8275-evk-pro-sku \
    "
SRC_URI[qcs8300-ride-sx.sha256sum] = "d7fc667372b28383a36d586333097d84b9d9c104f4dd1845d33904e2d6b39f80"
SRC_URI[cdt-iq8275-evk-pro-sku.sha256sum] = "cbe2009c8ef7dbacd716141bf01b8e1b26788c4a4f3145e60fe3b4a6b3aabc04"
SRC_URI[license.sha256sum] = "3ad8f1fd82f2918c858cec2d0887b7df6f71a06416beecfdb3efe7d62874d863"

QCOM_BOOT_IMG_SUBDIR = "qcs8300"

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
