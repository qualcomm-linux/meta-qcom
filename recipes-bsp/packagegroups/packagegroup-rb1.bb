SUMMARY = "Packages for the RB1 Robotics platform"

inherit packagegroup

PACKAGES = " \
    ${PN}-firmware \
    ${PN}-hexagon-dsp-binaries \
    ${PN}-modules \
"

RRECOMMENDS:${PN}-firmware = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'linux-firmware-qcom-adreno-a702 linux-firmware-qcom-qcm2290-adreno', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath10k-wcn3990 linux-firmware-qcom-qcm2290-wifi ', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca-wcn3950', '', d)} \
    linux-firmware-lt9611uxc \
    linux-firmware-qcom-qcm2290-audio \
    linux-firmware-qcom-qcm2290-modem \
    linux-firmware-qcom-venus-6.0 \
"

RRECOMMENDS:${PN}-hexagon-dsp-binaries = " \
    hexagon-dsp-binaries-thundercomm-rb1-adsp \
"

RRECOMMENDS:${PN}-modules = " \
    kernel-module-ath10k-snoc \
    kernel-module-cdc-ncm \
    kernel-module-dispcc-qcm2290 \
    kernel-module-display-connector \
    kernel-module-gpi \
    kernel-module-gpucc-qcm2290 \
    kernel-module-i2c-gpio \
    kernel-module-i2c-qcom-geni \
    kernel-module-icc-bwmon \
    kernel-module-lmh \
    kernel-module-lontium-lt9611uxc \
    kernel-module-mcp251xfd \
    kernel-module-msm \
    kernel-module-phy-qcom-qmp-usbc \
    kernel-module-phy-qcom-qusb2 \
    kernel-module-qcom-pmic-tcpm \
    kernel-module-qcom-pon \
    kernel-module-qcom-q6v5-pas \
    kernel-module-qcom-rng \
    kernel-module-qcom-stats \
    kernel-module-qcom-usb-vbus-regulator \
    kernel-module-qcom-wdt \
    kernel-module-qrtr \
    kernel-module-qrtr-mhi \
    kernel-module-qrtr-smd \
    kernel-module-rmtfs-mem \
    kernel-module-rpmsg-ctrl \
    kernel-module-rtc-pm8xxx \
    kernel-module-socinfo \
    kernel-module-spi-geni-qcom \
"
