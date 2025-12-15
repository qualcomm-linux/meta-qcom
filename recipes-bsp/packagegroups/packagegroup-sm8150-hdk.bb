SUMMARY = "Packages for the SM8150-HDK (aka HDK855) board"

inherit packagegroup

PACKAGES = " \
    ${PN}-firmware \
    ${PN}-hexagon-dsp-binaries \
"

RRECOMMENDS:${PN}-firmware = " \
    ${@bb.utils.contains_any('DISTRO_FEATURES', 'opencl opengl vulkan', 'linux-firmware-qcom-adreno-a640 linux-firmware-qcom-sm8150-adreno', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca-wcn399x', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath10k-wcn3990', '', d)} \
    firmware-qcom-sm8150-hdk \
    linux-firmware-qcom-sm8150-audio \
    linux-firmware-qcom-sm8150-compute \
    linux-firmware-qcom-sm8150-ipa \
    linux-firmware-qcom-sm8150-modem \
    linux-firmware-qcom-sm8150-sensors \
"

RDEPENDS:${PN}-hexagon-dsp-binaries = " \
    hexagon-dsp-binaries-qcom-sm8150-hdk-adsp \
    hexagon-dsp-binaries-qcom-sm8150-hdk-cdsp \
    hexagon-dsp-binaries-qcom-sm8150-hdk-sdsp \
"
