SUMMARY = "Firmware packages for the SM8350-HDK (aka HDK888) board"

inherit packagegroup

RRECOMMENDS:${PN} += " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'linux-firmware-qcom-adreno-a660', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca-qca2066', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath11k-wcn6855', '', d)} \
    firmware-qcom-sm8350-hdk \
    linux-firmware-lt9611uxc \
    linux-firmware-qcom-sm8350-adreno \
    linux-firmware-qcom-sm8350-audio \
    linux-firmware-qcom-sm8350-compute \
    linux-firmware-qcom-sm8350-ipa \
    linux-firmware-qcom-sm8350-modem \
    linux-firmware-qcom-sm8350-sensors \
    linux-firmware-qcom-vpu \
"
