# Nord U-Boot SPL FIT settings, for the machines that also require
# qcom-uboot-spl-fit.inc.

# Memory layout
UBOOT_FIT_ARM_TRUSTED_FIRMWARE_LOADADDRESS ?= "0xbc300000"
UBOOT_FIT_ARM_TRUSTED_FIRMWARE_ENTRYPOINT ?= "0xbc300000"
UBOOT_FIT_TEE_LOADADDRESS ?= "0xbc600000"
UBOOT_FIT_TEE_ENTRYPOINT ?= "0xbc600000"
UBOOT_FIT_UBOOT_LOADADDRESS ?= "0xd8d00000"
UBOOT_FIT_UBOOT_ENTRYPOINT ?= "0xd8d00000"

# Recipes providing BL31 and OP-TEE
QCOM_UBOOT_SPL_FIT_ATF ?= "trusted-firmware-a-qcom-nord"
QCOM_UBOOT_SPL_FIT_TEE ?= "optee-os-qcom-nord"

# The SPL loads the image data stored after the FIT
UBOOT_FIT_MKIMAGE_EXTRA_OPTS ?= "-E"
