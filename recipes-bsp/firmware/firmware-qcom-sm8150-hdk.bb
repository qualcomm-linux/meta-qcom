# Specify location of the corresponding NON-HLOS.bin file by adding
# NHLOS_URI:pn-firmware-qcom-sm8350-hdk = "..."  to local.conf. Use "file://"
# if the file is provided locally.

DESCRIPTION = "QCOM Firmware for SM8150 HDK (aka HDK855) board"

LICENSE = "CLOSED"

FW_QCOM_NAME = "sm8150"

FW_QCOM_LIST = "\
    a640_zap.mbn \
    adsp.mbn adspr.jsn adspua.jsn \
    cdsp.mbn cdspr.jsn \
    ipa_fws.mbn \
    modem.mbn modemuw.jsn \
    slpi.mbn slpir.jsn \
    wlanmdsp.mbn \
"

S = "${UNPACKDIR}"

require recipes-bsp/firmware/firmware-qcom.inc
require recipes-bsp/firmware/firmware-qcom-nhlos.inc
include recipes-bsp/firmware/firmware-qcom-adreno.inc

SPLIT_FIRMWARE_PACKAGES = "\
    linux-firmware-qcom-${FW_QCOM_NAME}-adreno \
    linux-firmware-qcom-${FW_QCOM_NAME}-audio \
    linux-firmware-qcom-${FW_QCOM_NAME}-compute \
    linux-firmware-qcom-${FW_QCOM_NAME}-ipa \
    linux-firmware-qcom-${FW_QCOM_NAME}-modem \
    linux-firmware-qcom-${FW_QCOM_NAME}-sensors \
    linux-firmware-qcom-${FW_QCOM_NAME}-wifi \
    linux-firmware-qcom-adreno-a640 \
"

do_install:append() {
    if [ -n "${ADRENO_URI}" ] ; then
        install -m 0644 ${UNPACKDIR}/adreno/${ADRENO_PATH}/a640_gmu.bin ${D}${FW_QCOM_BASE_PATH}
    fi
}

FILES:linux-firmware-qcom-adreno-a640 += "${FW_QCOM_BASE_PATH}/a640_gmu.bin"
RDEPENDS:linux-firmware-qcom-adreno-a640 += "linux-firmware-qcom-adreno-a630"
RDEPENDS:linux-firmware-qcom-adreno-a640:remove = "${PN}"
