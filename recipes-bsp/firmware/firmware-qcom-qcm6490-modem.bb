# Specify location of the corresponding NON-HLOS.bin file by adding
# NHLOS_URI:pn-firmware-qcom-qcm6490-modem = "..."  to local.conf. Use "file://"
# if the file is provided locally.

DESCRIPTION = "QCOM Modem Firmware for QCM6490-IDP board"

LICENSE = "CLOSED"

FW_QCOM_NAME = "qcm6490"

FW_QCOM_LIST = "\
    modem.mbn modemuw.jsn \
"

S = "${UNPACKDIR}"

require recipes-bsp/firmware/firmware-qcom.inc
require recipes-bsp/firmware/firmware-qcom-nhlos.inc

SPLIT_FIRMWARE_PACKAGES = "\
    linux-firmware-qcom-${FW_QCOM_NAME}-modem \
"
