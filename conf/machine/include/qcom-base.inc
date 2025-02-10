# Common configurations and variables for Qualcomm platforms.

# Supported BSP selections are: base, custom.
# Specific MACHINEs or DISTROs may change this
# selection as needed in their configuration.
QCOM_SELECTED_BSP ??= "none"
MACHINEOVERRIDES =. "${@bb.utils.contains_any('QCOM_SELECTED_BSP', 'base custom', 'qcom-${QCOM_SELECTED_BSP}-bsp:', '', d)}"

# Provider for linux kernel
PREFERRED_PROVIDER_virtual/kernel ?= "linux-yocto-dev"

KERNEL_IMAGETYPE ?= "Image"
KERNEL_IMAGETYPES ?= "Image.gz"

# For dtb.bin generation
KERNEL_CLASSES += "linux-qcom-dtbbin"

# QDL expects 4096 aligned ext4 image for flashing
IMAGE_FSTYPES += "ext4"
IMAGE_ROOTFS_ALIGNMENT = "4096"
EXTRA_IMAGECMD:ext4 = "-i 4096 -b 4096"

SERIAL_CONSOLES ?= "115200;ttyMSM0"

QCOM_BOOTIMG_ROOTFS ?= "PARTLABEL=rootfs"

