# We do not use kernel image or kernel modules in the image, so remove the
# dependency on the kernel
KERNELDEPMODDEPEND = ""
KERNEL_DEPLOY_DEPEND = ""

IMAGE_LINGUAS = ""
LICENSE = "MIT"

IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
IMAGE_NAME_SUFFIX ?= ""
inherit core-image

IMAGE_ROOTFS_SIZE = "8192"
IMAGE_ROOTFS_EXTRA_SPACE = "0"

# Inhibit installing /init
IMAGE_BUILDING_DEBUGFS = "true"
