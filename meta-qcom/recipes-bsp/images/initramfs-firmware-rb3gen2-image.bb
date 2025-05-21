DESCRIPTION = "Tiny ramdisk image with RB3-Gen2 firmware files"

PACKAGE_INSTALL += " \
    packagegroup-rb3gen2-firmware \
"

require initramfs-firmware-image.inc
