# Shared configuration for EFI System Partition images

KERNELDEPMODDEPEND = ""
KERNEL_DEPLOY_DEPEND = ""

ESPFOLDER = ""

# VFAT filesystem for ESP
IMAGE_FSTYPES = "vfat"
IMAGE_FSTYPES_DEBUGFS = ""

EXTRA_IMAGECMD:vfat += " -S ${QCOM_VFAT_SECTOR_SIZE}"

# Align image size with the expected partition size (512MB)
IMAGE_ROOTFS_SIZE = "524288"
IMAGE_ROOTFS_MAXSIZE = "524288"
IMAGE_ROOTFS_EXTRA_SPACE = "0"

IMAGE_LINGUAS = ""
IMAGE_FEATURES = ""
