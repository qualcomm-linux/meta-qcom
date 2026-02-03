DESCRIPTION = "Tiny ramdisk image with SDM845 HDK devices firmware files"

PACKAGE_INSTALL += " \
    packagegroup-sdm845-hdk-firmware \
"

require initramfs-firmware-image.inc
