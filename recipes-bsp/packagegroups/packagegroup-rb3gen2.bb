SUMMARY = "Packages for the RB3Gen2 platform"

inherit packagegroup

PACKAGES = " \
    ${PN}-firmware \
    ${PN}-hexagon-dsp-binaries \
"

RRECOMMENDS:${PN}-firmware = " \
    ${@bb.utils.contains_any('DISTRO_FEATURES', 'opencl opengl vulkan', 'linux-firmware-qcom-adreno-a660 linux-firmware-qcom-qcm6490-adreno', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath11k-wcn6750 linux-firmware-qcom-qcm6490-wifi', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca-wcn6750', '', d)} \
    linux-firmware-qcom-qcm6490-audio \
    linux-firmware-qcom-qcm6490-compute \
    linux-firmware-qcom-qcm6490-qupv3fw \
    linux-firmware-qcom-vpu \
"

RDEPENDS:${PN}-hexagon-dsp-binaries = " \
    hexagon-dsp-binaries-thundercomm-rb3gen2-adsp \
    hexagon-dsp-binaries-thundercomm-rb3gen2-cdsp \
"
