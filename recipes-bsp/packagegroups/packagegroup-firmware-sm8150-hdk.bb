SUMMARY = "Firmware packages for the SM8150-HDK (aka HDK855) board"

inherit packagegroup

RRECOMMENDS:${PN} += " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'linux-firmware-qcom-adreno-a640 linux-firmware-qcom-sm8150-adreno', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca-wcn399x', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath10k-wcn3990', '', d)} \
    firmware-qcom-sm8150-hdk \
    linux-firmware-qcom-sm8150-audio \
    linux-firmware-qcom-sm8150-compute \
    linux-firmware-qcom-sm8150-ipa \
    linux-firmware-qcom-sm8150-modem \
    linux-firmware-qcom-sm8150-sensors \
"
