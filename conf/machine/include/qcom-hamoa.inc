# Configurations and variables for hamoa SoC family.

SOC_FAMILY = "hamoa"
require conf/machine/include/qcom-base.inc
require conf/machine/include/qcom-common.inc

DEFAULTTUNE = "armv8-6a-crypto"
require conf/machine/include/arm/arch-armv8-6a.inc

MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS += " \
    packagegroup-qcom-boot-essential \
    packagegroup-machine-essential-qcom-hamoa-soc \
"

MACHINE_EXTRA_RRECOMMENDS += " \
    packagegroup-qcom-boot-additional \
"
