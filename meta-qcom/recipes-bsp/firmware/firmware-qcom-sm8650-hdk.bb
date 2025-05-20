# Specify location of the corresponding NON-HLOS.bin file by adding
# NHLOS_URI:pn-firmware-qcom-sm8450-hdk = "..."  to local.conf. Use "file://"
# if the file is provided locally.

DESCRIPTION = "QCOM Firmware for SM8650 HDK board"

LICENSE = "CLOSED"

FW_QCOM_NAME = "sm8650"

FW_QCOM_LIST = "\
    adsp.mbn adsp_dtb.mbn adspr.jsn adsps.jsn adspua.jsn battmgr.jsn \
    cdsp.mbn cdsp_dtb.mbn cdspr.jsn \
    ipa_fws.mbn \
    gen70900_zap.mbn \
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
    linux-firmware-qcom-adreno-g790 \
"

do_install:append() {
    if [ -n "${ADRENO_URI}" ] ; then
        install -m 0644 ${UNPACKDIR}/adreno/${ADRENO_PATH}/gen70900_sqe.fw ${D}${FW_QCOM_BASE_PATH}
        install -m 0644 ${UNPACKDIR}/adreno/${ADRENO_PATH}/gmu_gen70900.bin ${D}${FW_QCOM_BASE_PATH}
    fi
}

FILES:linux-firmware-qcom-adreno-g790 += "${FW_QCOM_BASE_PATH}/gen70900_sqe.fw ${FW_QCOM_BASE_PATH}/gmu_gen70900.bin"
