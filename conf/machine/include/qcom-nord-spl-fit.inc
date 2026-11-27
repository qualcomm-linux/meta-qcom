# Nord U-Boot SPL FIT settings, for the machines that also require
# qcom-uboot-spl-fit.inc.

# Memory layout
UBOOT_FIT_ARM_TRUSTED_FIRMWARE_LOADADDRESS ?= "0xbc300000"
UBOOT_FIT_ARM_TRUSTED_FIRMWARE_ENTRYPOINT ?= "0xbc300000"
UBOOT_FIT_TEE_LOADADDRESS ?= "0xbc600000"
UBOOT_FIT_TEE_ENTRYPOINT ?= "0xbc600000"
UBOOT_FIT_UBOOT_LOADADDRESS ?= "0xd8d00000"
UBOOT_FIT_UBOOT_ENTRYPOINT ?= "0xd8d00000"

# Recipes providing BL31, OP-TEE and the boot firmware
QCOM_UBOOT_SPL_FIT_ATF ?= "trusted-firmware-a-qcom-nord"
QCOM_UBOOT_SPL_FIT_TEE ?= "optee-os-qcom-nord"
QCOM_UBOOT_SPL_FIT_FIRMWARE ?= "firmware-qcom-boot-nord"

# The SPL loads the image data stored after the FIT
UBOOT_FIT_MKIMAGE_EXTRA_OPTS ?= "-E"

# The TZ access control config, from QCOM_UBOOT_SPL_FIT_FIRMWARE, loaded along
# with BL31/OP-TEE/U-Boot at post-ddr.
UBOOT_FIT_LOADABLES ?= "tz_ac_config"
UBOOT_FIT_LOADABLE_DESCRIPTION[tz_ac_config] ?= "TZ Access Control Config"
UBOOT_FIT_LOADABLE_FILENAME[tz_ac_config] ?= "tz_ac_config.bin"
UBOOT_FIT_LOADABLE_LOADADDRESS[tz_ac_config] ?= "0xbc000000"
UBOOT_FIT_LOADABLE_ENTRYPOINT[tz_ac_config] ?= "0xbc000000"
UBOOT_FIT_CONF_LOADABLES ?= "tz_ac_config"
