# Configurations and variables for Kaanapali SoC family.

SOC_FAMILY = "kaanapali"
require conf/machine/include/qcom-base.inc
require conf/machine/include/qcom-common.inc

DEFAULTTUNE = "armv8-7a"
require conf/machine/include/arm/arch-armv8-7a.inc

MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS += " \
    packagegroup-qcom-boot-essential \
"
