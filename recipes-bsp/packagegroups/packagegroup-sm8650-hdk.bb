SUMMARY = "Packages for the SM8650-HDK board"

inherit packagegroup

PACKAGES = " \
    ${PN}-firmware \
    ${PN}-hexagon-dsp-binaries \
"

RRECOMMENDS:${PN}-firmware = " \
    ${@bb.utils.contains_any('DISTRO_FEATURES', 'opencl opengl vulkan', 'linux-firmware-qcom-adreno-g790 linux-firmware-qcom-sm8650-adreno', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca-wcn7850', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath12k-wcn7850', '', d)} \
    firmware-qcom-sm8650-hdk \
    linux-firmware-lt9611uxc \
    linux-firmware-qcom-sm8650-audio \
    linux-firmware-qcom-sm8650-audio-tplg \
    linux-firmware-qcom-sm8650-compute \
    linux-firmware-qcom-sm8650-ipa \
"

RDEPENDS:${PN}-hexagon-dsp-binaries = " \
    hexagon-dsp-binaries-qcom-sm8650-hdk-adsp \
    hexagon-dsp-binaries-qcom-sm8650-hdk-cdsp \
"
