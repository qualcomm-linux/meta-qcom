# Configurations and variables for QCS6490 SoC family.

SOC_FAMILY = "qrb2210"
require conf/machine/include/qcom-base.inc
require conf/machine/include/qcom-common.inc

require conf/machine/include/arm/armv8a/tune-cortexa53.inc

KERNEL_CMDLINE_EXTRA ?= "earlycon clk_ignore_unused pd_ignore_unused"

EXTRA_IMAGEDEPENDS += "firmware-qcom-boot-qrb2210-rb1 qcom-gen-partition-bins"
