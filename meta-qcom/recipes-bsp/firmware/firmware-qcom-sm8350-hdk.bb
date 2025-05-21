# Specify location of the corresponding NON-HLOS.bin file by adding
# NHLOS_URI:pn-firmware-qcom-sm8350-hdk = "..."  to local.conf. Use "file://"
# if the file is provided locally.

DESCRIPTION = "QCOM Firmware for SM8350 HDK (aka HDK888) board"

LICENSE = "CLOSED"

FW_QCOM_NAME = "sm8350"

FW_QCOM_LIST = "\
    a660_zap.mbn a615_zap.mbn \
    adsp.mbn adspr.jsn adspua.jsn battmgr.jsn \
    cdsp.mbn cdspr.jsn \
    ipa_fws.mbn \
    modem.mbn modemr.jsn \
    slpi.mbn slpir.jsn \
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
"
