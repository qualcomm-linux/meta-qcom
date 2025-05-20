DESCRIPTION = "Tiny ramdisk image with Qualcomm Robotics RB5 firmware files"

# Do not install anything by default
PACKAGE_INSTALL = ""

PACKAGE_INSTALL:qcom-armv8a = " \
    packagegroup-rb5-firmware \
"

require initramfs-firmware-image.inc
