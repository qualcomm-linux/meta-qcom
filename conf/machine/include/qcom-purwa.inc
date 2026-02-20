# Configurations and variables for Purwa SoC family.

SOC_FAMILY = "purwa"
require conf/machine/include/qcom-base.inc
require conf/machine/include/qcom-common.inc

QCOM_VFAT_SECTOR_SIZE = "512"

DEFAULTTUNE = "armv8-6a-crypto"
require conf/machine/include/arm/arch-armv8-6a.inc

MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS += " \
    packagegroup-qcom-boot-essential \
    packagegroup-machine-essential-qcom-purwa-soc \
"

MACHINE_EXTRA_RRECOMMENDS += " \
    packagegroup-qcom-boot-additional \
"
