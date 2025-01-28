SUMMARY = "Firmware packages for the RB5 Robotics platform"

inherit packagegroup

RRECOMMENDS:${PN} += " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'linux-firmware-qcom-adreno-a650 linux-firmware-qcom-sm8250-adreno', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath11k-qca6390', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca-qca6390', '', d)} \
    linux-firmware-lt9611uxc \
    linux-firmware-qcom-sm8250-audio \
    linux-firmware-qcom-sm8250-compute \
    linux-firmware-qcom-vpu \
    hexagon-dsp-binaries-thundercomm-rb5-adsp \
    hexagon-dsp-binaries-thundercomm-rb5-cdsp \
"
