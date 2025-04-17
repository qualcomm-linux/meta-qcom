DESCRIPTION = "EFI System Partition Image to boot Qualcomm boards"

PACKAGE_INSTALL = " \
    systemd-boot \
"

KERNELDEPMODDEPEND = ""
KERNEL_DEPLOY_DEPEND = ""

ESPFOLDER = ""
inherit image uki uki-esp-image

UKI_FILENAME = "${EFI_LINUX_IMG}"

UKI_CMDLINE = "root=${QCOM_BOOTIMG_ROOTFS} rw rootwait"

# Remove 'upstream' dtb, rely on EFI provided one
KERNEL_DEVICETREE = ""

IMAGE_FSTYPES = "vfat"
IMAGE_FSTYPES_DEBUGFS = ""

EXTRA_IMAGECMD:vfat += " -S ${QCOM_VFAT_SECTOR_SIZE}"

# Align image size with the expected partition size (512MB)
IMAGE_ROOTFS_SIZE = "524288"
IMAGE_ROOTFS_MAXSIZE = "524288"
IMAGE_ROOTFS_EXTRA_SPACE = "0"

IMAGE_LINGUAS = ""
IMAGE_FEATURES = ""

remove_unused_files() {
    find ${IMAGE_ROOTFS} -mindepth 1 ! -path "${IMAGE_ROOTFS}/EFI*" -exec rm -rf {} +
}
IMAGE_PREPROCESS_COMMAND:append = " remove_unused_files"
