# Configurations and variables for IPQ9574 SoC family.

SOC_FAMILY = "ipq9574"
require conf/machine/include/qcom-common.inc

DEFAULTTUNE = "cortexa73-crypto"
require conf/machine/include/arm/armv8a/tune-cortexa73.inc

# Deploy the kernel FIT with every image; it is flashed to the kernel partition
EXTRA_IMAGEDEPENDS += "linux-yocto-fitimage:do_deploy"

IMAGE_FSTYPES = "squashfs-xz"

INITRAMFS_IMAGE:ipq9574 = ""

# Compress the FIT kernel payload using LZMA
FIT_KERNEL_COMP_ALG = "lzma"

# Enable FIT image generation
KERNEL_CLASSES += "kernel-fit-extra-artifacts"

# The kernel is deployed separately as a FIT image.
RRECOMMENDS:${KERNEL_PACKAGE_NAME}-base = ""

KERNEL_IMAGETYPE = "Image"

PREFERRED_PROVIDER_virtual/kernel ?= "linux-qcom-next"

# Doesn't support UEFI
QCOM_UEFI_BOOT = "0"

UBOOT_LOADADDRESS = "0x40000000"
UBOOT_ENTRYPOINT = "0x40000000"
