# Common configurations and variables for Qualcomm platforms.

# Provider for linux kernel
PREFERRED_PROVIDER_virtual/kernel ?= "linux-qcom-next"

KERNEL_IMAGETYPE ?= "Image"
KERNEL_ALT_IMAGETYPE ?= "vmlinux"

KERNEL_IMAGETYPES ?= "Image.gz"

# For dtb.bin generation
KERNEL_CLASSES += "linux-qcom-dtbbin"

# QDL expects 4096 aligned ext4 image for flashing
IMAGE_FSTYPES += "ext4"
IMAGE_ROOTFS_ALIGNMENT = "4096"
EXTRA_IMAGECMD:ext4 = "-i 4096 -b 4096"

# Ship the flashable package (boot firmware, partition tables and rootfs)
# by default, it is the expected way of installing an image on these boards.
IMAGE_CLASSES += "image_types_qcom"
IMAGE_FSTYPES += "qcomflash"

SERIAL_CONSOLES ?= "115200;ttyMSM0"

QCOM_BOOTIMG_ROOTFS ?= "PARTLABEL=rootfs"

