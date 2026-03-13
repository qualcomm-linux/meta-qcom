SUMMARY = "Packages for the Purwa IoT EVK platform"

inherit packagegroup

PACKAGES = " \
    ${PN}-firmware \
    ${PN}-hexagon-dsp-binaries \
"

# WLAN/BT/Audio/Video/Compute share the same firmware as Hamoa
RRECOMMENDS:${PN}-firmware = " \
    ${@bb.utils.contains_any('DISTRO_FEATURES', 'opencl opengl vulkan', 'linux-firmware-qcom-adreno-g715 linux-firmware-qcom-x1p42100-adreno', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca-qca61x4-usb linux-firmware-qca-wcn7850', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath11k-wcn6855 linux-firmware-ath12k-wcn7850', '', d)} \
    linux-firmware-qcom-x1e80100-audio \
    linux-firmware-qcom-x1e80100-compute \
    linux-firmware-qcom-vpu \
"

# Purwa IoT EVK and Hamoa IoT EVK share the same Hexagon DSP binaries.
RDEPENDS:${PN}-hexagon-dsp-binaries = " \
    hexagon-dsp-binaries-qcom-hamoa-iot-evk-adsp \
    hexagon-dsp-binaries-qcom-hamoa-iot-evk-cdsp \
"
