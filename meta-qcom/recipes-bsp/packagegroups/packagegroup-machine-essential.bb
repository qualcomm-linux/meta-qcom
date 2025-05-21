SUMMARY = "Essential packages for machine support"

inherit packagegroup

PACKAGES = " \
    ${PN}-board-generic \
    ${PN}-qcom-generic \
    ${PN}-qcom-qcm2290-soc \
    ${PN}-qcom-qcs6490-soc \
    ${PN}-qcom-qcs8300-soc \
    ${PN}-qcom-qcs9100-soc \
"

# The list of packages is kept sorted alphabetically which might break
# up groups of modules and firmwares.

# This packagegroup lists non-qcom specific modules that are needed,
# like hdmi and gpio support
RRECOMMENDS:${PN}-board-generic += " \
    kernel-module-cdc-ncm \
    kernel-module-display-connector \
    kernel-module-i2c-gpio \
    kernel-module-lontium-lt9611uxc \
    kernel-module-mcp251xfd \
    kernel-module-nb7vpq904m \
    kernel-module-snd-soc-hdmi-codec \
    kernel-module-xhci-pci-renesas \
"

# This packagegroup lists 'generic' qcom modules that are shared between
# the SoCs we support
RRECOMMENDS:${PN}-qcom-generic += " \
    kernel-module-fastrpc \
    kernel-module-gpi \
    kernel-module-i2c-qcom-geni \
    kernel-module-icc-bwmon \
    kernel-module-leds-qcom-lpg \
    kernel-module-lmh \
    kernel-module-msm \
    kernel-module-phy-qcom-edp \
    kernel-module-phy-qcom-qmp-combo \
    kernel-module-phy-qcom-qmp-usbc \
    kernel-module-phy-qcom-qusb2 \
    kernel-module-phy-qcom-snps-femto-v \
    kernel-module-qcom-pmic-tcpm \
    kernel-module-qcom-pon \
    kernel-module-qcom-q6v5-mss \
    kernel-module-qcom-q6v5-pas \
    kernel-module-qcom-rng \
    kernel-module-qcom-spmi-adc5 \
    kernel-module-qcom-spmi-adc-tm5 \
    kernel-module-qcom-spmi-temp-alarm \
    kernel-module-qcom-spmi-vadc \
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
    kernel-module-venus-dec \
    kernel-module-venus-enc \
"

# The packagegroups below are SoC specific

RRECOMMENDS:${PN}-qcom-qcm2290-soc += " \
    ${PN}-board-generic \
    ${PN}-qcom-generic \
    kernel-module-ath10k-snoc \
    kernel-module-dispcc-qcm2290 \
    kernel-module-gpucc-qcm2290 \
"

RRECOMMENDS:${PN}-qcom-qcs6490-soc += " \
    ${PN}-board-generic \
    ${PN}-qcom-generic \
    kernel-module-ath11k-ahb \
    kernel-module-camcc-sc7280 \
    kernel-module-dispcc-sc7280 \
    kernel-module-gpucc-sc7280 \
    kernel-module-lpassaudiocc-sc7280 \
    kernel-module-pinctrl-sc7280-lpass-lpi \
    kernel-module-pmic-glink \
    kernel-module-pmic-glink-altmode \
    kernel-module-pwrseq-qcom-wcn \
    kernel-module-videocc-sc7280 \
"

RRECOMMENDS:${PN}-qcom-qcs8300-soc += " \
    ${PN}-board-generic \
    ${PN}-qcom-generic \
    kernel-module-ath11k-pci \
    kernel-module-camcc-sa8775p \
    kernel-module-dispcc0-sa8775p \
    kernel-module-dispcc1-sa8775p \
    kernel-module-gpucc-sa8775p \
    kernel-module-pwrseq-qcom-wcn \
    kernel-module-videocc-sa8775p \
"

RRECOMMENDS:${PN}-qcom-qcs9100-soc += " \
    ${PN}-board-generic \
    ${PN}-qcom-generic \
    kernel-module-ath11k-pci \
    kernel-module-camcc-sa8775p \
    kernel-module-dispcc0-sa8775p \
    kernel-module-dispcc1-sa8775p \
    kernel-module-gpucc-sa8775p \
    kernel-module-pwrseq-qcom-wcn \
    kernel-module-videocc-sa8775p \
"

