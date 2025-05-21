DESCRIPTION = "Tiny ramdisk image with Dragonboard 845c firmware files"

PACKAGE_INSTALL += " \
    packagegroup-dragonboard845c-firmware \
"

require initramfs-firmware-image.inc
