DESCRIPTION = "Tiny ramdisk image with SM8650 HDK devices firmware files"

PACKAGE_INSTALL += " \
    packagegroup-sm8650-hdk-firmware \
"

BAD_RECOMMENDATIONS = " \
"

require initramfs-firmware-image.inc
