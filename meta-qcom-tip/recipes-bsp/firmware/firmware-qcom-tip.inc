DESCRIPTION = "QCOM HLOS Firmware for Qualcomm ${QCOM_FW_BOARD} platform"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://Qualcomm-Technologies-Inc.-Proprietary;md5=58d50a3d36f27f1a1e6089308a49b403"

require conf/qcom-tip.conf

def get_var(d, var):
    name = var % d.getVar('QCOM_FW_SOC').upper()
    return d.getVar(name) or ""

PV = "${@get_var(d, 'QCOM_%s_PV')}"
SRC_URI[fw.sha256sum] = "${@get_var(d, 'QCOM_%s_FIRMWARE_SUM')}"

FW_ARTIFACTORY = "softwarecenter.qualcomm.com/download/software/chip/qualcomm_linux-spf-1-0/qualcomm-linux-spf-1-0_test_device_public"
FW_BUILD_ID = "r1.0_${PV}/${QCOM_FW_SOC}-le-1-0"
FW_BIN_PATH = "common/build/ufs/bin"

DSP_QCOM_SOC ?= "${FW_QCOM_NAME}"

DSP_QCOM_BASE_PATH = "${datadir}/qcom"
DSP_QCOM_PATH = "${DSP_QCOM_BASE_PATH}/${DSP_QCOM_SOC}/${DSP_QCOM_VENDOR}/${DSP_QCOM_DEVICE}"

SRC_URI = " \
    https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/${FW_BIN_PATH}/${QCOM_FW_NAME}.zip;downloadfilename=${QCOM_FW_NAME}_r1.0_${PV}.zip;name=fw \
    "

S = "${UNPACKDIR}/${QCOM_FW_NAME}"

require recipes-bsp/firmware/firmware-qcom.inc

do_install:append() {
    install -d ${D}${FW_QCOM_PATH}
    install -m 0644 ${S}/lib/firmware/qcom/${FW_QCOM_SUBDIR}/*.mbn ${D}${FW_QCOM_PATH}
    install -m 0644 ${S}/lib/firmware/qcom/${FW_QCOM_SUBDIR}/*.jsn ${D}${FW_QCOM_PATH}
    if [ -e "${S}/lib/firmware/qupv3fw.elf" ] ; then
        install -m 0644 "${S}/lib/firmware/qupv3fw.elf" ${D}${FW_QCOM_PATH}
    fi

    for subdir in `ls -1 ${S}/usr/lib/dsp` ; do
        install -d ${D}${DSP_QCOM_PATH}/dsp/${subdir}
        install -m 0644 ${S}/usr/lib/dsp/${subdir}/* ${D}${DSP_QCOM_PATH}/dsp/${subdir}
    done
}

SPLIT_FIRMWARE_PACKAGES = " \
    linux-firmware-qcom-${FW_QCOM_NAME}-audio \
    linux-firmware-qcom-${FW_QCOM_NAME}-compute \
    linux-firmware-qcom-${FW_QCOM_NAME}-generalpurpose \
    hexagon-dsp-binaries-${DSP_PKG_NAME}-adsp \
    hexagon-dsp-binaries-${DSP_PKG_NAME}-cdsp \
    hexagon-dsp-binaries-${DSP_PKG_NAME}-gdsp \
    hexagon-dsp-binaries-${DSP_PKG_NAME}-sdsp \
"

FILES:hexagon-dsp-binaries-${DSP_PKG_NAME}-adsp = "${DSP_QCOM_PATH}/dsp/adsp"
FILES:hexagon-dsp-binaries-${DSP_PKG_NAME}-cdsp = "${DSP_QCOM_PATH}/dsp/cdsp*"
FILES:hexagon-dsp-binaries-${DSP_PKG_NAME}-gdsp = "${DSP_QCOM_PATH}/dsp/gdsp*"
FILES:hexagon-dsp-binaries-${DSP_PKG_NAME}-sdsp = "${DSP_QCOM_PATH}/dsp/sdsp*"

INSANE_SKIP:hexagon-dsp-binaries-${DSP_PKG_NAME}-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:hexagon-dsp-binaries-${DSP_PKG_NAME}-cdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:hexagon-dsp-binaries-${DSP_PKG_NAME}-gdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:hexagon-dsp-binaries-${DSP_PKG_NAME}-sdsp = "arch libdir file-rdeps textrel"
