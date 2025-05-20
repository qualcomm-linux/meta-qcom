# Specify location of the corresponding NON-HLOS.bin file by adding
# NHLOS_URI:pn-firmware-qcom-sm8450-hdk = "..."  to local.conf. Use "file://"
# if the file is provided locally.

DESCRIPTION = "QCOM Firmware for SM8550 HDK board"

LICENSE = "CLOSED"

FW_QCOM_NAME = "sm8550"

FW_QCOM_LIST = "\
    a740_zap.mbn \
    adsp.mbn adsp_dtb.mbn adspr.jsn adsps.jsn adspua.jsn battmgr.jsn \
    cdsp.mbn cdsp_dtb.mbn cdspr.jsn \
    ipa_fws.mbn \
    modem.mbn modemr.jsn \
"

S = "${UNPACKDIR}"

require recipes-bsp/firmware/firmware-qcom.inc
require recipes-bsp/firmware/firmware-qcom-nhlos.inc
require recipes-bsp/firmware/firmware-qcom-adreno.inc

SPLIT_FIRMWARE_PACKAGES = "\
    linux-firmware-qcom-${FW_QCOM_NAME}-adreno \
    linux-firmware-qcom-${FW_QCOM_NAME}-audio \
    linux-firmware-qcom-${FW_QCOM_NAME}-compute \
    linux-firmware-qcom-${FW_QCOM_NAME}-ipa \
    linux-firmware-qcom-${FW_QCOM_NAME}-modem \
    linux-firmware-qcom-adreno-a740 \
    linux-firmware-qcom-adreno-gmu-g720 \
"

do_install:append() {
    if [ -n "${ADRENO_URI}" ] ; then
        install -m 0644 ${UNPACKDIR}/adreno/${ADRENO_PATH}/a740_sqe.fw ${D}${FW_QCOM_BASE_PATH}
        install -m 0644 ${UNPACKDIR}/adreno/${ADRENO_PATH}/gmu_gen70200.bin ${D}${FW_QCOM_BASE_PATH}
    fi
}

FILES:linux-firmware-qcom-adreno-a740 += "${FW_QCOM_BASE_PATH}/a740_sqe.fw"
FILES:linux-firmware-qcom-adreno-gmu-g720 += "${FW_QCOM_BASE_PATH}/gmu_gen70200.bin"
RDEPENDS:linux-firmware-qcom-adreno-a740 += "linux-firmware-qcom-adreno-gmu-g720"
