DESCRIPTION = "Tiny ramdisk image with SM8650 HDK devices firmware files"

PACKAGE_INSTALL += " \
    packagegroup-sm8650-hdk-firmware \
    packagegroup-sm8550-hdk-firmware \
"

BAD_RECOMMENDATIONS = " \
"

require initramfs-firmware-image.inc
