DESCRIPTION = "Tiny ramdisk image with QCS8300 Ride firmware files"

PACKAGE_INSTALL += " \
    packagegroup-qcs8300-ride-firmware \
"

require initramfs-firmware-image.inc
