inherit packagegroup

PACKAGES = " \
    ${PN}-firmware \
    ${PN}-hexagon-dsp-binaries \
"

RRECOMMENDS:${PN}-firmware = " \
    ${@bb.utils.contains_any('DISTRO_FEATURES', 'opencl opengl vulkan', 'linux-firmware-qcom-adreno-a740 linux-firmware-qcom-sm8550-adreno', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca-wcn7850', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath12k-wcn7850', '', d)} \
    firmware-qcom-sm8550-hdk \
    linux-firmware-lt9611uxc \
    linux-firmware-qcom-sm8550-audio \
    linux-firmware-qcom-sm8550-compute \
    linux-firmware-qcom-sm8550-ipa \
    linux-firmware-qcom-sm8550-modem \
"

RDEPENDS:${PN}-hexagon-dsp-binaries = " \
    hexagon-dsp-binaries-qcom-sm8550-hdk-adsp \
    hexagon-dsp-binaries-qcom-sm8550-hdk-cdsp \
"
