inherit packagegroup

RRECOMMENDS:${PN} += " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'linux-firmware-qcom-adreno-a650 linux-firmware-qcom-adreno-gmu-a621 linux-firmware-qcom-sar2130p-adreno', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca-wcn7850', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath12k-wcn7850', '', d)} \
    firmware-qcom-sar2130p \
    linux-firmware-qcom-sar2130p-audio \
    linux-firmware-qcom-sar2130p-compute \
"
