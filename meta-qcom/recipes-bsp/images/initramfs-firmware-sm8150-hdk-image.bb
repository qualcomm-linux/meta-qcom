DESCRIPTION = "Tiny ramdisk image with SM8150 HDK devices firmware files"

PACKAGE_INSTALL += " \
    packagegroup-sm8150-hdk-firmware \
"

BAD_RECOMMENDATIONS = " \
    linux-firmware-qcom-sm8150-sensors \
"

require initramfs-firmware-image.inc
