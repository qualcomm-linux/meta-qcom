
# Configurations and variables for IPQ9574 SoC family.

SOC_FAMILY = "ipq9574"
require conf/machine/include/qcom-base.inc
require conf/machine/include/qcom-common.inc

DEFAULTTUNE = "armv8a-crypto"
require conf/machine/include/arm/arch-armv8a.inc
