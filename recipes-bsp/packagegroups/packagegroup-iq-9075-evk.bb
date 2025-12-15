SUMMARY = "Packages for the IQ-9075-EVK platform"

inherit packagegroup

PACKAGES = " \
    ${PN}-firmware \
    ${PN}-hexagon-dsp-binaries \
"

RRECOMMENDS:${PN}-firmware = " \
    ${@bb.utils.contains_any('DISTRO_FEATURES', 'opencl opengl vulkan', 'linux-firmware-qcom-adreno-a663 linux-firmware-qcom-adreno-a660 linux-firmware-qcom-sa8775p-adreno', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath11k-qca6698aq linux-firmware-ath11k-wcn6855', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca-qca2066', '', d)} \
    linux-firmware-qcom-sa8775p-audio \
    linux-firmware-qcom-sa8775p-compute \
    linux-firmware-qcom-sa8775p-generalpurpose \
    linux-firmware-qcom-sa8775p-qupv3fw \
    linux-firmware-qcom-vpu \
"

RDEPENDS:${PN}-hexagon-dsp-binaries = " \
    hexagon-dsp-binaries-qcom-iq9075-evk-adsp \
    hexagon-dsp-binaries-qcom-iq9075-evk-cdsp \
    hexagon-dsp-binaries-qcom-iq9075-evk-gdsp \
"
