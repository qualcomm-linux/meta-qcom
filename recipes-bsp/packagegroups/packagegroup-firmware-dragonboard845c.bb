SUMMARY = "Firmware packages for the DragonBoard 845c board"

inherit packagegroup

RRECOMMENDS:${PN} += " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'linux-firmware-qcom-adreno-a630 linux-firmware-qcom-sdm845-adreno', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath10k-wcn3990 linux-firmware-qcom-sdm845-modem', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca-wcn399x linux-firmware-qcom-sdm845-modem', '', d)} \
    linux-firmware-qcom-sdm845-audio \
    linux-firmware-qcom-sdm845-compute \
    linux-firmware-qcom-venus-5.2 \
    hexagon-dsp-binaries-thundercomm-db845c-adsp \
    hexagon-dsp-binaries-thundercomm-db845c-cdsp \
"
