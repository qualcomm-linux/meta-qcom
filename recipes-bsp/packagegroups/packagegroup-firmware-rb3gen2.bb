SUMMARY = "Firmware packages for the RB3Gen2 platform"

inherit packagegroup

RRECOMMENDS:${PN} += " \
    firmware-qcom-rb3gen2 \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'linux-firmware-qcom-adreno-a660', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath11k-wcn6750 linux-firmware-qcom-qcs6490-wifi', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca', '', d)} \
    linux-firmware-qcom-qcs6490-audio \
    linux-firmware-qcom-qcs6490-compute \
    linux-firmware-qcom-vpu \
    hexagon-dsp-binaries-thundercomm-rb3gen2-adsp \
    hexagon-dsp-binaries-thundercomm-rb3gen2-cdsp \
"
