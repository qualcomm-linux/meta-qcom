# Configurations and variables for Shikra SoC family.

SOC_FAMILY = "shikra"
require conf/machine/include/qcom-base.inc
require conf/machine/include/qcom-common.inc

DEFAULTTUNE = "armv8-2a-crypto"
require conf/machine/include/arm/arch-armv8-2a.inc

MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS += " \
    packagegroup-qcom-boot-essential \
    packagegroup-machine-essential-qcom-shikra-soc \
"

MACHINE_EXTRA_RRECOMMENDS += " \
    packagegroup-qcom-boot-additional \
"

# Shikra ships a single, EL2-agnostic xbl_config; there is no xbl_config_kvm.elf.
QCOM_XBL_CONFIG = "xbl_config.elf"

# the KVM variant must land on the uefi_dtbs partition when kvm is enabled.
QCOM_UEFI_DTB = "${@bb.utils.contains('MACHINE_FEATURES', 'kvm', 'uefi_dtbs_kvm.elf', 'uefi_dtbs.elf', d)}"
