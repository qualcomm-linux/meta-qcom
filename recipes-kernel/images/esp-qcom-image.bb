DESCRIPTION = "EFI System Partition Image to boot Qualcomm boards"

PACKAGE_INSTALL = " \
    systemd-boot \
"

KERNELDEPMODDEPEND = ""
KERNEL_DEPLOY_DEPEND = ""

ESPFOLDER = ""
inherit image uki-esp-image

inherit uki
UKI_FILENAME = "${EFI_LINUX_IMG}"

UKI_CMDLINE="root=${QCOM_BOOTIMG_ROOTFS} rw rootwait"

# Remove leading 'qcom/' from KERNEL_DEVICEREE, we store the file in DEPLOY_DIR_IMAGE directly, not in the qcom/ subfolder
#KERNEL_DEVICETREE := "${@d.getVar('KERNEL_DEVICETREE').strip().replace('qcom/','')}"

# Remove 'upstream' dtb, rely on EFI provided one
KERNEL_DEVICETREE = ""

IMAGE_FSTYPES = "vfat"
IMAGE_FSTYPES_DEBUGFS = ""

# UFS requires vfat sector size of 4096 (default is 512)
VFAT_SECTOR_SIZE ?= "4096"
EXTRA_IMAGECMD:vfat += " -S ${VFAT_SECTOR_SIZE}"

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
