# Shikra settings for the U-Boot SPL FIT boot flow: the memory layout, the
# recipes providing the images and the contents of the FIT. Only the machines
# using that flow need them, so they require this file along with
# conf/machine/include/qcom-uboot-spl-fit.inc.

# Memory layout needed by the SPL FIT boot flow.
QCOM_UBOOT_SPL_ENTRY ?= "0x0c219000"
UBOOT_FIT_ARM_TRUSTED_FIRMWARE_LOADADDRESS ?= "0xa1300000"
UBOOT_FIT_ARM_TRUSTED_FIRMWARE_ENTRYPOINT ?= "0xa1300000"
UBOOT_FIT_TEE_LOADADDRESS ?= "0xa1400000"
UBOOT_FIT_TEE_ENTRYPOINT ?= "0xa1400000"
UBOOT_FIT_UBOOT_LOADADDRESS ?= "0x9f400000"
UBOOT_FIT_UBOOT_ENTRYPOINT ?= "0x9f400000"

# Recipes providing BL31, OP-TEE and the boot firmware for the SPL FIT boot
# flow, and the XBL-sec ELF taken from the boot firmware.
QCOM_UBOOT_SPL_FIT_ATF ?= "trusted-firmware-a-qcom-shikra"
QCOM_UBOOT_SPL_FIT_TEE ?= "optee-os-qcom-shikra"
QCOM_UBOOT_SPL_FIT_FIRMWARE ?= "firmware-qcom-boot-shikra"
QCOM_UBOOT_SPL_FIT_XBL_SEC ?= "qc_sec.mbn"

# The Shikra SPL loads the images from data stored after the FIT.
UBOOT_FIT_MKIMAGE_EXTRA_OPTS ?= "-E"
