DESCRIPTION = "Tiny ramdisk image with SA8775P Ride firmware files"

PACKAGE_INSTALL += " \
    packagegroup-sa8775p-ride-firmware \
"

require initramfs-firmware-image.inc
