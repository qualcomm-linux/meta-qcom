SUMMARY = "Packages for the DragonBoard 820c board"

inherit packagegroup

PACKAGES = " \
    ${PN}-firmware \
    ${PN}-hexagon-dsp-binaries \
"

RRECOMMENDS:${PN}-firmware = " \
    ${@bb.utils.contains_any('DISTRO_FEATURES', 'opencl opengl', 'linux-firmware-qcom-adreno-a530 linux-firmware-qcom-apq8096-adreno', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath10k-qca6174', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca-qca61x4-serial', '', d)} \
    linux-firmware-qcom-apq8096-audio \
    linux-firmware-qcom-apq8096-modem \
    linux-firmware-qcom-venus-4.2 \
"

RDEPENDS:${PN}-hexagon-dsp-binaries = " \
    hexagon-dsp-binaries-qcom-db820c-adsp \
"
