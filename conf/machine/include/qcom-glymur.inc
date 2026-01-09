# Configurations and variables for glymur SoC family.

SOC_FAMILY = "glymur"
require conf/machine/include/qcom-base.inc
require conf/machine/include/qcom-common.inc

QCOM_VFAT_SECTOR_SIZE = "512"

DEFAULTTUNE = "armv8-6a-crypto"
require conf/machine/include/arm/arch-armv8-6a.inc

MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS += " \
    packagegroup-qcom-boot-essential \
"

MACHINE_EXTRA_RRECOMMENDS += " \
    packagegroup-qcom-boot-additional \
"
