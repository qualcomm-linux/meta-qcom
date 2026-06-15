require initramfs-rootfs-image.bb

DESCRIPTION = "Ramdisk image for Qualcomm boards"

PACKAGE_INSTALL += "packagegroup-qcom-boot"
