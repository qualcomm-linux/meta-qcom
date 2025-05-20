# Configurations and variables for QCM2290 (aka QRB2210) SoC family.

SOC_FAMILY = "qcm2290"
require conf/machine/include/qcom-base.inc
require conf/machine/include/qcom-common.inc

QCOM_VFAT_SECTOR_SIZE = "512"

require conf/machine/include/arm/armv8a/tune-cortexa53.inc

KERNEL_CMDLINE_EXTRA ?= "clk_ignore_unused pd_ignore_unused"

MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS += " \
    packagegroup-qcom-boot-essential \
    packagegroup-machine-essential-qcom-qcm2290-soc \
"

MACHINE_EXTRA_RRECOMMENDS += " \
    packagegroup-qcom-boot-additional \
"

EXTRA_IMAGEDEPENDS += "firmware-qcom-boot-qrb2210-rb1"
