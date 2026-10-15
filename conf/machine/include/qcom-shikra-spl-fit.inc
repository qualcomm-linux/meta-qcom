# Shikra U-Boot SPL FIT settings, for the machines that also require
# qcom-uboot-spl-fit.inc.

# Memory layout
QCOM_UBOOT_SPL_ENTRY ?= "0x0c219000"
UBOOT_FIT_ARM_TRUSTED_FIRMWARE_LOADADDRESS ?= "0xa1300000"
UBOOT_FIT_ARM_TRUSTED_FIRMWARE_ENTRYPOINT ?= "0xa1300000"
UBOOT_FIT_TEE_LOADADDRESS ?= "0xa1400000"
UBOOT_FIT_TEE_ENTRYPOINT ?= "0xa1400000"
UBOOT_FIT_UBOOT_LOADADDRESS ?= "0x9f400000"
UBOOT_FIT_UBOOT_ENTRYPOINT ?= "0x9f400000"

# Recipes providing BL31 and OP-TEE
QCOM_UBOOT_SPL_FIT_ATF ?= "trusted-firmware-a-qcom-shikra"
QCOM_UBOOT_SPL_FIT_TEE ?= "optee-os-qcom-shikra"

# The SPL loads the image data stored after the FIT
UBOOT_FIT_MKIMAGE_EXTRA_OPTS ?= "-E"
