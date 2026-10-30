# Configurations and variables for SM8975 (Hawi) SoC family.

SOC_FAMILY = "hawi"

require conf/machine/include/qcom-base.inc
require conf/machine/include/qcom-common.inc

DEFAULTTUNE = "armv8-6a-crypto"
require conf/machine/include/arm/arch-armv8-6a.inc

MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS += " \
    packagegroup-qcom-boot-essential \
    "

MACHINE_EXTRA_RRECOMMENDS += " \
    packagegroup-qcom-boot-additional \
    "
