SUMMARY = "Packages for the Shikra-EVK platform"

inherit packagegroup

COMPATIBLE_MACHINE = "(qcom)"

PACKAGES = " \
    ${PN}-firmware \
    ${PN}-hexagon-dsp-binaries \
"

RRECOMMENDS:${PN}-firmware = " \
    ${@bb.utils.contains_any('DISTRO_FEATURES', 'opencl opengl vulkan', 'linux-firmware-qcom-adreno-a702 linux-firmware-qcom-shikra-adreno', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca-wcn3950', '', d)} \
    linux-firmware-qcom-shikra-compute \
    linux-firmware-qcom-shikra-audio \
    linux-firmware-qcom-shikra-modem \
    linux-firmware-qcom-shikra-qupv3fw \
    linux-firmware-qcom-vpu \
"

RDEPENDS:${PN}-hexagon-dsp-binaries = " \
    hexagon-dsp-binaries-qcom-shikra-cqm-evk-cdsp \
    hexagon-dsp-binaries-qcom-shikra-cqs-evk-cdsp \
    hexagon-dsp-binaries-qcom-shikra-iqs-evk-cdsp \
"
