SUMMARY = "Packages for the KAANAPALI-MTP platform"

inherit packagegroup

PACKAGES = " \
    ${PN}-firmware \
"

RRECOMMENDS:${PN}-firmware = " \
    ${@bb.utils.contains_any('DISTRO_FEATURES', 'opencl opengl vulkan', 'linux-firmware-qcom-adreno-g802 linux-firmware-qcom-kaanapali-adreno', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath12k-wcn7850', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca-wcn7850', '', d)} \
    linux-firmware-qcom-kaanapali-audio \
    linux-firmware-qcom-kaanapali-compute \
    linux-firmware-qcom-kaanapali-soccp \
    linux-firmware-qcom-vpu \
"
