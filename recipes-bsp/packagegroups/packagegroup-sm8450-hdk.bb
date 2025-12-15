SUMMARY = "Packages for the SM8450-HDK board"

inherit packagegroup

PACKAGES = " \
    ${PN}-firmware \
    ${PN}-hexagon-dsp-binaries \
"

RRECOMMENDS:${PN}-firmware = " \
    ${@bb.utils.contains_any('DISTRO_FEATURES', 'opencl opengl vulkan', 'linux-firmware-qcom-adreno-a730 linux-firmware-qcom-sm8450-adreno', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca-qca2066', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath11k-wcn6855', '', d)} \
    firmware-qcom-sm8450-hdk \
    linux-firmware-lt9611uxc \
    linux-firmware-qcom-sm8450-audio \
    linux-firmware-qcom-sm8450-compute \
    linux-firmware-qcom-sm8450-modem \
    linux-firmware-qcom-sm8450-sensors \
"

RDEPENDS:${PN}-hexagon-dsp-binaries = " \
    hexagon-dsp-binaries-qcom-sm8450-hdk-adsp \
    hexagon-dsp-binaries-qcom-sm8450-hdk-cdsp \
    hexagon-dsp-binaries-qcom-sm8450-hdk-sdsp \
"
