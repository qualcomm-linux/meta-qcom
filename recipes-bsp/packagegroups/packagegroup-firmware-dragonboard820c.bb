SUMMARY = "Firmware packages for the DragonBoard 820c board"

inherit packagegroup

RRECOMMENDS:${PN} += " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'linux-firmware-qcom-adreno-a530 linux-firmware-qcom-apq8096-adreno', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath10k-qca6174', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca', '', d)} \
    linux-firmware-qcom-apq8096-audio \
    linux-firmware-qcom-apq8096-modem \
    linux-firmware-qcom-venus-4.2 \
    hexagon-dsp-binaries-qcom-db820c-adsp \
"
