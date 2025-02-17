DESCRIPTION = "Tiny ramdisk image with QAR2130P devices firmware files"

PACKAGE_INSTALL += " \
    packagegroup-firmware-qar2130p \
"

BAD_RECOMMENDATIONS = " \
"

require initramfs-firmware-image.inc
