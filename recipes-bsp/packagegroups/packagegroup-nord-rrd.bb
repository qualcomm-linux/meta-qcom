SUMMARY = "Packages for the IQ10 RRD platform"

inherit packagegroup

PACKAGES = " \
    ${PN}-firmware \
    ${PN}-hexagon-dsp-binaries \
"

RRECOMMENDS:${PN}-firmware = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath12k-wcn7850', '', d)} \
"

RRECOMMENDS:${PN}-firmware:qcom = " \
    linux-firmware-qcom-nord-audio \
    linux-firmware-qcom-nord-compute \
    linux-firmware-qcom-nord-qupv3fw \
"

RDEPENDS:${PN}-hexagon-dsp-binaries = " \
"
