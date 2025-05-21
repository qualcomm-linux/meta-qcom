DESCRIPTION = "Tiny ramdisk image with Dragonboard 410c firmware files"

PACKAGE_INSTALL += " \
    packagegroup-dragonboard410c-firmware \
"

require initramfs-firmware-image.inc
