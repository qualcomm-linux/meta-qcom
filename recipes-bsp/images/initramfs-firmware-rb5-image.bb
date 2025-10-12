DESCRIPTION = "Tiny ramdisk image with Qualcomm Robotics RB5 firmware files"

PACKAGE_INSTALL += " \
    packagegroup-rb5-firmware \
"

require initramfs-firmware-image.inc
