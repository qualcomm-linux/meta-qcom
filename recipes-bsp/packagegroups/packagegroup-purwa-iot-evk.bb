SUMMARY = "Packages for the Purwa IoT EVK platform"

inherit packagegroup

PACKAGES = " \
    ${PN}-firmware \
    ${PN}-hexagon-dsp-binaries \
"

RRECOMMENDS:${PN}-firmware = " \
    ${@bb.utils.contains_any('DISTRO_FEATURES', 'opencl opengl vulkan', 'linux-firmware-qcom-adreno-g705 linux-firmware-qcom-x1e80100-adreno', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca-qca61x4-usb linux-firmware-qca-wcn7850', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath11k-wcn6855 linux-firmware-ath12k-wcn7850', '', d)} \
    linux-firmware-qcom-x1e80100-audio \
    linux-firmware-qcom-x1e80100-compute \
    linux-firmware-qcom-vpu \
"

RDEPENDS:${PN}-hexagon-dsp-binaries = " \
    hexagon-dsp-binaries-qcom-purwa-iot-evk-cdsp \
"
