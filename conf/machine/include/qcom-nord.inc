# Configurations and variables for Nord SoC family.

SOC_FAMILY = "nord"
require conf/machine/include/qcom-base.inc
require conf/machine/include/qcom-common.inc

DEFAULTTUNE = "armv8-6a-crypto"
require conf/machine/include/arm/arch-armv8-6a.inc

MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS += " \
    packagegroup-qcom-boot-essential \
    packagegroup-machine-essential-qcom-nord-soc \
"

MACHINE_EXTRA_RRECOMMENDS += " \
    packagegroup-qcom-boot-additional \
"

# Memory layout needed by the SPL FIT boot flow.
UBOOT_FIT_ARM_TRUSTED_FIRMWARE_LOADADDRESS ?= "0xbc300000"
UBOOT_FIT_ARM_TRUSTED_FIRMWARE_ENTRYPOINT ?= "0xbc300000"
UBOOT_FIT_TEE_LOADADDRESS ?= "0xbc600000"
UBOOT_FIT_TEE_ENTRYPOINT ?= "0xbc600000"
UBOOT_FIT_UBOOT_LOADADDRESS ?= "0xd8d00000"
UBOOT_FIT_UBOOT_ENTRYPOINT ?= "0xd8d00000"

# Recipes providing BL31 and OP-TEE for the FIT.
QCOM_UBOOT_SPL_FIT_ATF ?= "trusted-firmware-a-qcom-nord"
QCOM_UBOOT_SPL_FIT_TEE ?= "optee-os-qcom-nord"

# The Nord SPL loads a FIT with extra image data.
UBOOT_FIT_MKIMAGE_EXTRA_OPTS ?= "-E"

# The TZ access control config is loaded along with BL31, OP-TEE and U-Boot,
# from the boot firmware recipe.
QCOM_UBOOT_SPL_FIT_FIRMWARE ?= "firmware-qcom-boot-nord"
UBOOT_FIT_LOADABLES ?= "tz_ac_config"
UBOOT_FIT_LOADABLE_DESCRIPTION[tz_ac_config] ?= "TZ Access Control Config"
UBOOT_FIT_LOADABLE_FILENAME[tz_ac_config] ?= "tz_ac_config.bin"
UBOOT_FIT_LOADABLE_LOADADDRESS[tz_ac_config] ?= "0xbc000000"
UBOOT_FIT_LOADABLE_ENTRYPOINT[tz_ac_config] ?= "0xbc000000"
UBOOT_FIT_CONF_LOADABLES ?= "tz_ac_config"
