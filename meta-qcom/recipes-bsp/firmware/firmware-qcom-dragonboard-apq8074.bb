# Specify location of the corresponding NON-HLOS.bin file by adding
# NHLOS_URI:pn-firmware-qcom-dragonboard-apq8074 = "..."  to local.conf. Use
# "file://" if the file is provided locally.

DESCRIPTION = "QCOM Firmware for Dragonboard APQ8074 board"

LICENSE = "CLOSED"

# dragonboard8074 firmware is unsigned, so install into generic location
FW_QCOM_NAME = "apq8074"

FW_QCOM_LIST = "adsp.mbn mba.mbn modem.mbn wcnss.mbn"

S = "${UNPACKDIR}"

require recipes-bsp/firmware/firmware-qcom.inc
require recipes-bsp/firmware/firmware-qcom-nhlos.inc

SPLIT_FIRMWARE_PACKAGES = " \
    linux-firmware-qcom-${FW_QCOM_NAME}-audio \
    linux-firmware-qcom-${FW_QCOM_NAME}-modem \
    linux-firmware-qcom-${FW_QCOM_NAME}-wifi \
"
