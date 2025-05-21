DESCRIPTION = "Tiny ramdisk image with SM8450 HDK devices firmware files"

PACKAGE_INSTALL += " \
    packagegroup-sm8450-hdk-firmware \
"

BAD_RECOMMENDATIONS = " \
    linux-firmware-qcom-sm8450-sensors \
"

require initramfs-firmware-image.inc
