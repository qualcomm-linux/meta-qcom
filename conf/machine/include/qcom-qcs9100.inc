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

# Memory layout of the U-Boot SPL FIT boot flow and the other SoC specific
# inputs it needs (see conf/machine/include/qcom-uboot-spl-fit.inc). Only used
# by machines that require that include; the SPL entry matches the
# CONFIG_SPL_TEXT_BASE of the U-Boot configuration they select.
QCOM_UBOOT_SPL_ENTRY ?= "0x1c100000"
UBOOT_FIT_ARM_TRUSTED_FIRMWARE_LOADADDRESS ?= "0x1c200000"
UBOOT_FIT_ARM_TRUSTED_FIRMWARE_ENTRYPOINT ?= "0x1c200000"
UBOOT_FIT_TEE_LOADADDRESS ?= "0x1c300000"
UBOOT_FIT_TEE_ENTRYPOINT ?= "0x1c300000"
UBOOT_FIT_UBOOT_LOADADDRESS ?= "0xaf400000"
UBOOT_FIT_UBOOT_ENTRYPOINT ?= "0xaf400000"

# Platform name passed to swiv_build_utility when annotating the SPL ELF with
# the SWIV segment, before it is signed as the TZ image.
QCOM_UBOOT_SPL_SWIV_PLATFORM ?= "lemans"

# Pre-signed xbl_config.elf with the "uboot_spl" flag enabled, so the XBL hands
# off to the U-Boot SPL (tz partition) instead of the legacy loader.
# image_types_qcom flashes it in place of the stock xbl_config when the SPL FIT
# flow is on.
QCOM_XBLCONFIG_SPL_FILE ?= "${LAYERDIR_qcom}/recipes-bsp/firmware-boot/files/qcs9100/xbl_config-lemans-uboot-spl.elf"
