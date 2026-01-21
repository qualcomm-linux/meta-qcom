SUMMARY = "Essential packages for machine support"

inherit packagegroup

PACKAGES = " \
    ${PN}-board-generic \
    ${PN}-qcom-generic \
    ${PN}-qcom-hamoa-soc \
    ${PN}-qcom-purwa-soc \
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
    kernel-module-ax88179-178a \
    kernel-module-cdc-ncm \
    kernel-module-display-connector \
    kernel-module-dwmac-qcom-ethqos \
    kernel-module-i2c-gpio \
    kernel-module-lontium-lt9611uxc \
    kernel-module-mcp251xfd \
    kernel-module-nb7vpq904m \
    kernel-module-nvmem-reboot-mode \
    kernel-module-pci-pwrctrl-pwrseq \
    kernel-module-pci-pwrctrl-tc9563 \
    kernel-module-snd-soc-hdmi-codec \
    kernel-module-xhci-pci-renesas \
"

# This packagegroup lists 'generic' qcom modules that are shared between
# the SoCs we support
RRECOMMENDS:${PN}-qcom-generic += " \
    kernel-module-fastrpc \
    kernel-module-gpi \
    kernel-module-i2c-qcom-cci \
    kernel-module-i2c-qcom-geni \
    kernel-module-icc-bwmon \
    kernel-module-icc-osm-l3 \
    kernel-module-imx412 \
    kernel-module-leds-qcom-lpg \
    kernel-module-lmh \
    kernel-module-msm \
    kernel-module-nvmem-qcom-spmi-sdam \
    kernel-module-phy-qcom-edp \
    kernel-module-phy-qcom-qmp-combo \
    kernel-module-phy-qcom-qmp-usbc \
    kernel-module-phy-qcom-qusb2 \
    kernel-module-phy-qcom-snps-femto-v2 \
    kernel-module-q6apm-dai \
    kernel-module-q6apm-lpass-dais \
    kernel-module-q6prm-clocks \
    kernel-module-qcom-battmgr \
    kernel-module-qcom-camss \
    kernel-module-qcom-iris \
    kernel-module-qcom-pd-mapper \
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
    kernel-module-qcrypto \
    kernel-module-qrtr \
    kernel-module-qrtr-mhi \
    kernel-module-qrtr-smd \
    kernel-module-rmtfs-mem \
    kernel-module-rpmsg-ctrl \
    kernel-module-rtc-pm8xxx \
    kernel-module-snd-q6apm \
    kernel-module-snd-soc-lpass-rx-macro \
    kernel-module-snd-soc-lpass-tx-macro \
    kernel-module-snd-soc-lpass-va-macro \
    kernel-module-snd-soc-lpass-wsa-macro \
    kernel-module-snd-soc-sc8280xp \
    kernel-module-socinfo \
    kernel-module-soundwire-qcom \
    kernel-module-spi-geni-qcom \
    kernel-module-ucsi-glink \
    kernel-module-venus-dec \
    kernel-module-venus-enc \
"

# The packagegroups below are SoC specific

RRECOMMENDS:${PN}-qcom-hamoa-soc += " \
    ${PN}-board-generic \
    ${PN}-qcom-generic \
    kernel-module-ath11k-pci \
    kernel-module-ath12k \
    kernel-module-camcc-x1e80100 \
    kernel-module-dispcc-x1e80100 \
    kernel-module-gpucc-x1e80100 \
    kernel-module-lpasscc-sc8280xp \
    kernel-module-pinctrl-sm8550-lpass-lpi \
    kernel-module-pmic-glink \
    kernel-module-pmic-glink-altmode \
    kernel-module-pwrseq-qcom-wcn \
    kernel-module-snd-soc-wcd938x \
    kernel-module-snd-soc-wsa884x \
    kernel-module-snd-soc-x1e80100 \
    kernel-module-tscrcc-x1e80100 \
    kernel-module-videocc-sm8550 \
"

RRECOMMENDS:${PN}-qcom-purwa-soc += " \
    ${PN}-board-generic \
    ${PN}-qcom-generic \
    kernel-module-ath11k-pci \
    kernel-module-ath12k \
    kernel-module-camcc-x1e80100 \
    kernel-module-dispcc-x1e80100 \
    kernel-module-gpucc-x1e80100 \
    kernel-module-lpasscc-sc8280xp \
    kernel-module-pinctrl-sm8550-lpass-lpi \
    kernel-module-pmic-glink \
    kernel-module-pmic-glink-altmode \
    kernel-module-pwrseq-qcom-wcn \
    kernel-module-snd-soc-wcd938x \
    kernel-module-snd-soc-wsa884x \
    kernel-module-snd-soc-x1e80100 \
    kernel-module-tscrcc-x1e80100 \
    kernel-module-videocc-sm8550 \
"

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
    kernel-module-snd-soc-wsa883x \
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
    kernel-module-phy-qcom-sgmii-eth \
    kernel-module-pwrseq-qcom-wcn \
    kernel-module-stmmac-platform \
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
    kernel-module-phy-qcom-sgmii-eth \
    kernel-module-pwrseq-qcom-wcn \
    kernel-module-stmmac-platform \
    kernel-module-videocc-sa8775p \
"

