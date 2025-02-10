# Common configurations and variables for Qualcomm platforms.

# Supported BSP selections are: 'base', 'custom'. Where 'custom' selection is an overlay on top of 
# 'base'.i.e. with 'custom' BSP selection both base & custom overrides come into effect.
# Specific MACHINEs or DISTROs may change this selection as needed in their configuration.
QCOM_SELECTED_BSP ??= "none"
MACHINEOVERRIDES =. "${@bb.utils.contains('QCOM_SELECTED_BSP', 'base', 'qcom-base-bsp:', '', d)}"
MACHINEOVERRIDES =. "${@bb.utils.contains('QCOM_SELECTED_BSP', 'custom', 'qcom-base-bsp:qcom-custom-bsp:', '', d)}"

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

