SUMMARY = "Packages for the IQ-8275-EVK platform"

inherit packagegroup

PACKAGES = " \
    ${PN}-firmware \
    ${PN}-hexagon-dsp-binaries \
"

RRECOMMENDS:${PN}-firmware = " \
    ${@bb.utils.contains_any('DISTRO_FEATURES', 'opencl opengl vulkan', 'linux-firmware-qcom-adreno-a623 linux-firmware-qcom-adreno-a650 linux-firmware-qcom-qcs8300-adreno', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath11k-wcn6855', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca-qca2066', '', d)} \
    linux-firmware-qcom-qcs8300-audio \
    linux-firmware-qcom-qcs8300-compute \
    linux-firmware-qcom-qcs8300-generalpurpose \
    linux-firmware-qcom-qcs8300-qupv3fw \
    linux-firmware-qcom-vpu \
"

RDEPENDS:${PN}-hexagon-dsp-binaries = " \
    hexagon-dsp-binaries-qcom-iq8275-evk-adsp \
    hexagon-dsp-binaries-qcom-iq8275-evk-cdsp \
    hexagon-dsp-binaries-qcom-iq8275-evk-gdsp \
"
