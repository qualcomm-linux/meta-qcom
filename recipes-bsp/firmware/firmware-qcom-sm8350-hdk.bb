# Specify location of the corresponding NON-HLOS.bin file by adding
# NHLOS_URI:pn-firmware-qcom-sm8350-hdk = "..."  to local.conf. Use "file://"
# if the file is provided locally.

DESCRIPTION = "QCOM Firmware for SM8350 HDK (aka HDK888) board"

LICENSE = "CLOSED"

FW_QCOM_NAME = "sm8350"

FW_QCOM_LIST = "\
    adsp.mbn adspr.jsn adspua.jsn battmgr.jsn \
    cdsp.mbn cdspr.jsn \
    ipa_fws.mbn \
    modem.mbn modemr.jsn \
    slpi.mbn slpir.jsn \
"

S = "${UNPACKDIR}"

require recipes-bsp/firmware/firmware-qcom.inc
require recipes-bsp/firmware/firmware-qcom-nhlos.inc

SPLIT_FIRMWARE_PACKAGES = "\
    linux-firmware-qcom-${FW_QCOM_NAME}-audio \
    linux-firmware-qcom-${FW_QCOM_NAME}-compute \
    linux-firmware-qcom-${FW_QCOM_NAME}-ipa \
    linux-firmware-qcom-${FW_QCOM_NAME}-modem \
    linux-firmware-qcom-${FW_QCOM_NAME}-sensors \
    linux-firmware-qcom-${FW_QCOM_NAME}-venus \
"

# Different vpu20_4v.mbn files are not compatible and support different
# platforms. Use platform-specific name for the file in generic location.
do_install:prepend() {
    if [ -r "${S}/proprietary/vpu20_4v.mbn" ] ; then
        install -d ${D}${FW_QCOM_BASE_PATH}/vpu
        install -m 0644 ${S}/proprietary/vpu20_4v.mbn ${D}${FW_QCOM_BASE_PATH}/vpu/vpu20_4v_sm8350.mbn
    fi
}
