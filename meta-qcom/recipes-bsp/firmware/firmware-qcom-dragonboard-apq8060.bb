# Specify location of the corresponding NON-HLOS.bin file by adding
# NHLOS_URI:pn-firmware-qcom-dragonboard-apq8060 = "..."  to local.conf. Use
# "file://" if the file is provided locally.

DESCRIPTION = "QCOM Firmware for Dragonboard APQ8060 board"

LICENSE = "CLOSED"

# dragonboard8060 firmware is unsigned, so install into generic location
FW_QCOM_NAME = "apq8060"

FW_QCOM_LIST = "q6.mbn"

S = "${UNPACKDIR}"

require recipes-bsp/firmware/firmware-qcom.inc
require recipes-bsp/firmware/firmware-qcom-nhlos.inc

unpack_nhlos_image:append() {
    if [ -d ${B}/firmware/IMAGE ] ; then
        mv ${B}/firmware/IMAGE ${B}/firmware/image
        for file in ${B}/firmware/image/* ; do
            mv "${file}" ${B}/firmware/image/`basename ${file} | tr A-Z a-z`
        done
    fi
}

SPLIT_FIRMWARE_PACKAGES = " \
    linux-firmware-qcom-${FW_QCOM_NAME}-q6 \
"
