# Specify location of the corresponding NON-HLOS.bin file by adding
# NHLOS_URI:pn-firmware-qcom-sm8450-hdk = "..."  to local.conf. Use "file://"
# if the file is provided locally.

DESCRIPTION = "QCOM Firmware for SM8450 HDK board"

LICENSE = "CLOSED"

FW_QCOM_NAME = "sm8450"

FW_QCOM_LIST = "\
    a730_zap.mbn \
    adsp.mbn adspr.jsn adspua.jsn battmgr.jsn \
    cdsp.mbn cdspr.jsn \
    ipa_fws.mbn \
    modem.mbn modemr.jsn \
    slpi.mbn slpir.jsn slpius.jsn \
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
    linux-firmware-qcom-${FW_QCOM_NAME}-sensors \
    linux-firmware-qcom-adreno-a730 \
    linux-firmware-qcom-adreno-gmu-g700 \
"

do_install:append() {
    if [ -n "${ADRENO_URI}" ] ; then
        install -m 0644 ${UNPACKDIR}/adreno/${ADRENO_PATH}/a730_sqe.fw ${D}${FW_QCOM_BASE_PATH}
        install -m 0644 ${UNPACKDIR}/adreno/${ADRENO_PATH}/gmu_gen70000.bin ${D}${FW_QCOM_BASE_PATH}
    fi
}

FILES:linux-firmware-qcom-adreno-a730 += "${FW_QCOM_BASE_PATH}/a730_sqe.fw"
FILES:linux-firmware-qcom-adreno-gmu-g700 += "${FW_QCOM_BASE_PATH}/gmu_gen70000.bin"
RDEPENDS:linux-firmware-qcom-adreno-a730 += "linux-firmware-qcom-adreno-gmu-g700"
