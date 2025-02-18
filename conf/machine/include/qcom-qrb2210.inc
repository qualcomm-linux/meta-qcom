# Configurations and variables for QCS6490 SoC family.

SOC_FAMILY = "qrb2210"
require conf/machine/include/qcom-base.inc
require conf/machine/include/qcom-common.inc

DEFAULTTUNE = "armv8-2a-crypto"
require conf/machine/include/arm/arch-armv8-2a.inc

KERNEL_CMDLINE_EXTRA ?= "pcie_pme=nomsi earlycon"

EXTRA_IMAGEDEPENDS += "firmware-qcom-boot-qrb2210-rb1 qcom-gen-partition-bins"
