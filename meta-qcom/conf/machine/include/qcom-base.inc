# Common configurations and variables for Qualcomm platforms.

# Provider for linux kernel
PREFERRED_PROVIDER_virtual/kernel ?= "linux-yocto-dev"

KERNEL_IMAGETYPE ?= "Image"
KERNEL_ALT_IMAGETYPE ?= "vmlinux"

KERNEL_IMAGETYPES ?= "Image.gz"

# For dtb.bin generation
KERNEL_CLASSES += "linux-qcom-dtbbin"

# QDL expects 4096 aligned ext4 image for flashing
IMAGE_FSTYPES += "ext4"
IMAGE_ROOTFS_ALIGNMENT = "4096"
EXTRA_IMAGECMD:ext4 = "-i 4096 -b 4096"

SERIAL_CONSOLES ?= "115200;ttyMSM0"

QCOM_BOOTIMG_ROOTFS ?= "PARTLABEL=rootfs"

