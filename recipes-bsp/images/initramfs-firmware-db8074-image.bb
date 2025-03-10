DESCRIPTION = "Tiny ramdisk image with Dragonboard APQ8074 firmware files"

PACKAGE_INSTALL += " \
    packagegroup-dragonboard-apq8074-firmware \
"

require initramfs-firmware-image.inc
