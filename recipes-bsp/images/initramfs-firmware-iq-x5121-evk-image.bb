DESCRIPTION = "Tiny ramdisk image with IQ-x5121 EVK firmware files"

PACKAGE_INSTALL += " \
    packagegroup-purwa-iot-evk-firmware \
"

require initramfs-firmware-image.inc
