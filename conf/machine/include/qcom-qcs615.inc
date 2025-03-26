SOC_FAMILY = "qcs615"
require conf/machine/include/qcom-base.inc
require conf/machine/include/qcom-common.inc

DEFAULTTUNE = "armv8-2a-crypto"
require conf/machine/include/arm/arch-armv8-2a.inc

KERNEL_CMDLINE_EXTRA ?= "pci=noaer pcie_pme=nomsi earlycon systemd.gpt_auto=0"

EXTRA_IMAGEDEPENDS += "qcom-gen-partition-bins"
