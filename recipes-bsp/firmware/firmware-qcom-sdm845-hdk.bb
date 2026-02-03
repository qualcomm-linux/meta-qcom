# Specify location of the corresponding NON-HLOS.bin file by adding
# NHLOS_URI:pn-firmware-qcom-sdm845-hdk = "..."  to local.conf. Use "file://"
# if the file is provided locally.

DESCRIPTION = "QCOM Firmware for SDM845 HDK (aka HDK845) board"

LICENSE = "CLOSED"

FW_QCOM_NAME = "sdm845-hdk"
FW_QCOM_DIR = "sdm845/Qualcomm/SDM845-HDK"

# ADSP, CDSP, modem and WLAN are a part of linux-firmware
FW_QCOM_LIST = "\
    slpi.mbn slpir.jsn \
"

S = "${UNPACKDIR}"

require recipes-bsp/firmware/firmware-qcom.inc
require recipes-bsp/firmware/firmware-qcom-nhlos.inc

SPLIT_FIRMWARE_PACKAGES = "\
    linux-firmware-qcom-${FW_QCOM_NAME}-sensors \
"
