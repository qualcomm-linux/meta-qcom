SUMMARY = "Packages for the RB1 Robotics platform"

inherit packagegroup

PACKAGES = " \
    ${PN}-firmware \
    ${PN}-hexagon-dsp-binaries \
"

RRECOMMENDS:${PN}-firmware = " \
    ${@bb.utils.contains_any('DISTRO_FEATURES', 'opencl opengl vulkan', 'linux-firmware-qcom-adreno-a702 linux-firmware-qcom-qcm2290-adreno', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath10k-wcn3990 linux-firmware-qcom-qcm2290-wifi ', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca-wcn3950', '', d)} \
    linux-firmware-lt9611uxc \
    linux-firmware-qcom-qcm2290-audio \
    linux-firmware-qcom-qcm2290-modem \
    linux-firmware-qcom-venus-6.0 \
"

RDEPENDS:${PN}-hexagon-dsp-binaries = " \
    hexagon-dsp-binaries-thundercomm-rb1-adsp \
"
