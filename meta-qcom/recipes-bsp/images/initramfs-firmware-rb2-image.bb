DESCRIPTION = "Tiny ramdisk image with RB2 devices firmware files"

PACKAGE_INSTALL += " \
    packagegroup-rb2-firmware \
"

BAD_RECOMMENDATIONS = " \
    linux-firmware-qcom-venus-6.0 \
"

require initramfs-firmware-image.inc
