SUMMARY = "Firmware for Qualcomm M.2 wireless attachments"

inherit packagegroup

QCOM_M2_WIFI_FIRMWARE = " \
    linux-firmware-ath12k-qcc2072 \
    linux-firmware-ath12k-qcn9274 \
    linux-firmware-ath12k-wcn7850 \
"

QCOM_M2_BT_FIRMWARE = " \
    linux-firmware-qca-qcc2072 \
    linux-firmware-qca-wcn7850 \
"

RRECOMMENDS:${PN} = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', '${QCOM_M2_WIFI_FIRMWARE}', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', '${QCOM_M2_BT_FIRMWARE}', '', d)} \
"
