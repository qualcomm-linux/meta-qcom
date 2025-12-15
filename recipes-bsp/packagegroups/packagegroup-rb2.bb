SUMMARY = "Packages for the RB2 Robotics platform"

inherit packagegroup

PACKAGES = " \
    ${PN}-firmware \
    ${PN}-hexagon-dsp-binaries \
"

RRECOMMENDS:${PN}-firmware = " \
    ${@bb.utils.contains_any('DISTRO_FEATURES', 'opencl opengl vulkan', 'linux-firmware-qcom-adreno-a630 linux-firmware-qcom-qrb4210-adreno', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath10k-wcn3990 linux-firmware-qcom-qrb4210-wifi', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca-wcn3988', '', d)} \
    linux-firmware-lt9611uxc \
    linux-firmware-qcom-qrb4210-audio \
    linux-firmware-qcom-qrb4210-compute \
    linux-firmware-qcom-qrb4210-modem \
    linux-firmware-qcom-venus-6.0 \
"

RDEPENDS:${PN}-hexagon-dsp-binaries = " \
    hexagon-dsp-binaries-thundercomm-rb2-adsp \
    hexagon-dsp-binaries-thundercomm-rb2-cdsp \
"
