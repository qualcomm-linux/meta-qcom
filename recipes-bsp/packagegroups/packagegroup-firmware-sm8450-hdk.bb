SUMMARY = "Firmware packages for the SM8450-HDK board"

inherit packagegroup

RRECOMMENDS:${PN} += " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'linux-firmware-qcom-adreno-a730 linux-firmware-qcom-sm8450-adreno', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath11k-wcn6855', '', d)} \
    firmware-qcom-sm8450-hdk \
    linux-firmware-lt9611uxc \
    linux-firmware-qcom-sm8450-audio \
    linux-firmware-qcom-sm8450-compute \
    linux-firmware-qcom-sm8450-modem \
    linux-firmware-qcom-sm8450-sensors \
"
