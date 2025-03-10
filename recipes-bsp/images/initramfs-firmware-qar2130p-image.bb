DESCRIPTION = "Tiny ramdisk image with QAR2130P devices firmware files"

PACKAGE_INSTALL += " \
    packagegroup-qar2130p-firmware \
"

BAD_RECOMMENDATIONS = " \
"

require initramfs-firmware-image.inc
