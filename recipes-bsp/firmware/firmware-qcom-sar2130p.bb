# Specify location of the corresponding NON-HLOS.bin file by adding
# NHLOS_URI:pn-firmware-qcom-sar2130p = "..."  to local.conf. Use "file://"
# if the file is provided locally.

DESCRIPTION = "QCOM Firmware for SAR2130P boards"

LICENSE = "CLOSED"

FW_QCOM_NAME = "sar2130p"

FW_QCOM_LIST = "\
    a620_zap.mbn \
    adsp.mbn adsp_dtb.mbn adspr.jsn adsps.jsn adspua.jsn \
    cdsp.mbn cdsp_dtb.mbn cdspr.jsn \
    ipa_fws.mbn \
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
    linux-firmware-qcom-adreno-gmu-a621 \
"

do_install:append() {
    if [ -n "${ADRENO_URI}" ] ; then
        install -m 0644 ${UNPACKDIR}/adreno/${ADRENO_PATH}/a621_gmu.bin ${D}${FW_QCOM_BASE_PATH}
    fi

    if [ -n "${NHLOS_URI}" ] ; then
        install -d ${D}/${nonarch_base_libdir}/firmware/ath12k/WCN7850/hw2.0/
        install -m 0644 ${UNPACKDIR}/firmware/image/kiwi/bdwlan.e30 ${D}/${nonarch_base_libdir}/firmware/ath12k/WCN7850/hw2.0/board.bin
    fi
}

FILES:linux-firmware-qcom-adreno-gmu-a621 += "${FW_QCOM_BASE_PATH}/a621_gmu.bin"
FILES:${PN} += "${nonarch_base_libdir}/firmware/ath12k"
