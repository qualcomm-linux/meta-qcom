DESCRIPTION = "Tiny ramdisk image with RB1 devices firmware files"

PACKAGE_INSTALL += " \
    packagegroup-rb1-firmware \
"

BAD_RECOMMENDATIONS = " \
    linux-firmware-qcom-venus-6.0 \
"

require initramfs-firmware-image.inc
