# Configurations and variables for IPQ9574 SoC family.

SOC_FAMILY = "ipq9574"
require conf/machine/include/qcom-base.inc
require conf/machine/include/qcom-common.inc

DEFAULTTUNE = "armv8a-crc-crypto"
require conf/machine/include/arm/arch-armv8a.inc

INITRAMFS_IMAGE = ""
INITRAMFS_IMAGE_BUNDLE = "0"

# Compress the FIT kernel payload using LZMA
FIT_KERNEL_COMP_ALG = "lzma"
FIT_KERNEL_COMP_ALG_EXTENSION = ".lzma"

# FIT-based u-boot flow does not require bootimg
KERNEL_CLASSES:remove = "linux-qcom-bootimg"

# Enable FIT image generation
KERNEL_CLASSES += "kernel-fit-extra-artifacts"

UBOOT_LOADADDRESS = "0x40000000"
UBOOT_ENTRYPOINT = "0x40000000"
