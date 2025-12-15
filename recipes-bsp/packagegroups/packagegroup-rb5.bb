SUMMARY = "Packages for the RB5 Robotics platform"

inherit packagegroup

PACKAGES = " \
    ${PN}-firmware \
    ${PN}-hexagon-dsp-binaries \
"

RRECOMMENDS:${PN}-firmware = " \
    ${@bb.utils.contains_any('DISTRO_FEATURES', 'opencl opengl vulkan', 'linux-firmware-qcom-adreno-a650 linux-firmware-qcom-sm8250-adreno', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath11k-qca6390', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca-qca6390', '', d)} \
    linux-firmware-lt9611uxc \
    linux-firmware-qcom-sm8250-audio \
    linux-firmware-qcom-sm8250-compute \
    linux-firmware-qcom-sm8250-thundercomm-rb5-sensors \
    linux-firmware-qcom-vpu \
"

RDEPENDS:${PN}-hexagon-dsp-binaries = " \
    hexagon-dsp-binaries-thundercomm-rb5-adsp \
    hexagon-dsp-binaries-thundercomm-rb5-cdsp \
    hexagon-dsp-binaries-thundercomm-rb5-sdsp \
"
