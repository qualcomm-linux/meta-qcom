# Configurations and variables for QCS9100 SoC family.

SOC_FAMILY = "qcs9100"
require conf/machine/include/qcom-base.inc
require conf/machine/include/qcom-common.inc

DEFAULTTUNE = "armv8-2a-crypto"
require conf/machine/include/arm/arch-armv8-2a.inc

MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS += " \
    packagegroup-qcom-boot-essential \
    packagegroup-machine-essential-qcom-qcs9100-soc \
"

MACHINE_EXTRA_RRECOMMENDS += " \
    packagegroup-qcom-boot-additional \
"

# Memory layout needed by the SPL FIT boot flow.
QCOM_UBOOT_SPL_ENTRY ?= "0x1c100000"
UBOOT_FIT_ARM_TRUSTED_FIRMWARE_LOADADDRESS ?= "0x1c200000"
UBOOT_FIT_ARM_TRUSTED_FIRMWARE_ENTRYPOINT ?= "0x1c200000"
UBOOT_FIT_TEE_LOADADDRESS ?= "0x1c300000"
UBOOT_FIT_TEE_ENTRYPOINT ?= "0x1c300000"
UBOOT_FIT_UBOOT_LOADADDRESS ?= "0xaf400000"
UBOOT_FIT_UBOOT_ENTRYPOINT ?= "0xaf400000"

# Recipes providing BL31 and OP-TEE for the FIT.
QCOM_UBOOT_SPL_FIT_ATF ?= "trusted-firmware-a-qcom-lemans-evk"
QCOM_UBOOT_SPL_FIT_TEE ?= "optee-os-qcom-lemans"

# Platform name for the SWIV annotation of the SPL ELF.
QCOM_UBOOT_SPL_SWIV_PLATFORM ?= "lemans"
