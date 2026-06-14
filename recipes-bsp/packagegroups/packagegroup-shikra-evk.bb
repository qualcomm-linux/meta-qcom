SUMMARY = "Packages for the Shikra-EVK platform"

inherit packagegroup

PACKAGES = " \
    ${PN}-firmware \
    ${PN}-hexagon-dsp-binaries \
"

RRECOMMENDS:${PN}-firmware = " \
    linux-firmware-qcom-shikra-qupv3fw \
    linux-firmware-qcom-shikra-compute \
    linux-firmware-qcom-vpu \
"

RDEPENDS:${PN}-hexagon-dsp-binaries = " \
    hexagon-dsp-binaries-qcom-shikra-cqs-evk-cdsp \
"
