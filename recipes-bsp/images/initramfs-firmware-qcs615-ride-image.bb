DESCRIPTION = "Tiny ramdisk image with QCS615 Ride firmware files"

PACKAGE_INSTALL += " \
    packagegroup-qcs615-ride-firmware \
"

require initramfs-firmware-image.inc
