inherit packagegroup

PACKAGES = " \
    ${PN}-firmware \
    ${PN}-hexagon-dsp-binaries \
"

RRECOMMENDS:${PN}-firmware = " \
    ${@bb.utils.contains_any('DISTRO_FEATURES', 'opencl opengl vulkan', 'linux-firmware-qcom-adreno-a650 linux-firmware-qcom-adreno-gmu-a621 linux-firmware-qcom-sar2130p-adreno', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca-wcn7850', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath12k-wcn7850', '', d)} \
    firmware-qcom-qar2130p \
    linux-firmware-qcom-sar2130p-audio \
    linux-firmware-qcom-sar2130p-compute \
"

RDEPENDS:${PN}-hexagon-dsp-binaries = " \
    hexagon-dsp-binaries-qcom-qar2130p-adsp \
    hexagon-dsp-binaries-qcom-qar2130p-cdsp \
"
