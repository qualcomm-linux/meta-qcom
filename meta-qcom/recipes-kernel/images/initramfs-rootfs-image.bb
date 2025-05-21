DESCRIPTION = "Ramdisk image for pivoting into rootfs"

PACKAGE_INSTALL = " \
    base-passwd \
    initramfs-module-copy-modules \
    initramfs-module-rootfs \
    initramfs-module-udev \
    ${VIRTUAL-RUNTIME_base-utils} \
    ${MACHINE_ESSENTIAL_EXTRA_RDEPENDS} \
    ${ROOTFS_BOOTSTRAP_INSTALL} \
"

# Do not pollute the initrd image with rootfs features
IMAGE_FEATURES = ""
IMAGE_LINGUAS = ""

LICENSE = "MIT"

IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
IMAGE_NAME_SUFFIX ?= ""
inherit core-image

IMAGE_ROOTFS_SIZE = "8192"
IMAGE_ROOTFS_EXTRA_SPACE = "0"

# Exclude all kernel images from the rootfs
PACKAGE_EXCLUDE = "kernel-image-*"
