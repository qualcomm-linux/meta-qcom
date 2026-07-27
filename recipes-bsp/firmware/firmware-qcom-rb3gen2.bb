# Placeholder recipe, actual modem firmware is provided in a separate layer

DESCRIPTION = "QCOM Firmware for Rb3 Gen2 board"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

FW_QCOM_NAME = "qcs6490"

FW_QCOM_LIST = "\
    modem.mbn modem_pr/ \
"

S = "${UNPACKDIR}"

require recipes-bsp/firmware/firmware-qcom.inc

SPLIT_FIRMWARE_PACKAGES = "\
    linux-firmware-qcom-${FW_QCOM_NAME}-modem \
"

FILES:linux-firmware-qcom-${FW_QCOM_NAME}-modem = ""
