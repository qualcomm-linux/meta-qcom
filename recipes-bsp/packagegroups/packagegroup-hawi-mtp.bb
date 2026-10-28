SUMMARY = "Packages for the HAWI-MTP platform"

inherit packagegroup

PACKAGES = " \
    ${PN}-firmware \
    ${PN}-hexagon-dsp-binaries \
    "

RRECOMMENDS:${PN}-firmware = " \
    ${@bb.utils.contains_any('DISTRO_FEATURES', 'opencl opengl vulkan', 'linux-firmware-qcom-adreno-g811 linux-firmware-qcom-hawi-adreno', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath12k-wcn7850', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca-wcn7850', '', d)} \
    linux-firmware-qcom-hawi-audio \
    linux-firmware-qcom-hawi-compute \
    linux-firmware-qcom-vpu \
    "

RRECOMMENDS:${PN}-hexagon-dsp-binaries = " \
    hexagon-dsp-binaries-qcom-hawi-mtp-adsp \
    hexagon-dsp-binaries-qcom-hawi-mtp-cdsp \
    "
