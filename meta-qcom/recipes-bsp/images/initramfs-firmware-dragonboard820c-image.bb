DESCRIPTION = "Tiny ramdisk image with Dragonboard 820c firmware files"

PACKAGE_INSTALL += " \
    packagegroup-dragonboard820c-firmware \
"

require initramfs-firmware-image.inc
