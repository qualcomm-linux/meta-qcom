SUMMARY = "Packages for the IQ-615-EVK platform"

inherit packagegroup

PACKAGES = " \
    ${PN}-firmware \
    ${PN}-hexagon-dsp-binaries \
"

RRECOMMENDS:${PN}-firmware = " \
    ${@bb.utils.contains_any('DISTRO_FEATURES', 'opencl opengl vulkan', 'linux-firmware-qcom-adreno-a612 linux-firmware-qcom-qcs615-adreno', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath11k-qca6698aq', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca-qca6698', '', d)} \
    linux-firmware-qcom-qcs615-audio \
    linux-firmware-qcom-qcs615-compute \
    linux-firmware-qcom-venus-5.4 \
"
RRECOMMENDS:${PN}-hexagon-dsp-binaries = " \
    hexagon-dsp-binaries-qcom-qcs615-ride-adsp \
    hexagon-dsp-binaries-qcom-qcs615-ride-cdsp \
"
