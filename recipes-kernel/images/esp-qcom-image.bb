DESCRIPTION = "EFI System Partition Image to boot Qualcomm boards"

PACKAGE_INSTALL = " \
    systemd-boot \
"

KERNELDEPMODDEPEND = ""
KERNEL_DEPLOY_DEPEND = ""

ESPFOLDER = ""
inherit image uki uki-esp-image features_check

UKI_FILENAME = "${EFI_LINUX_IMG}"

UKI_CMDLINE = "root=${QCOM_BOOTIMG_ROOTFS} rw rootwait console=${KERNEL_CONSOLE}"
UKI_CMDLINE += "${@d.getVar('KERNEL_CMDLINE_EXTRA') or ''}"

# Remove 'upstream' dtb, rely on EFI provided one
KERNEL_DEVICETREE = ""
KERNEL_DEVICETREE:sm8750-mtp = "${QCOM_DTB_DEFAULT}.dtb"
KERNEL_DEVICETREE:kaanapali-mtp = "${QCOM_DTB_DEFAULT}.dtb"

IMAGE_FSTYPES = "vfat"
IMAGE_FSTYPES_DEBUGFS = ""

EXTRA_IMAGECMD:vfat += " -S ${QCOM_VFAT_SECTOR_SIZE}"

# Align image size with the expected partition size (512MB)
IMAGE_ROOTFS_SIZE = "524288"
IMAGE_ROOTFS_MAXSIZE = "524288"
IMAGE_ROOTFS_EXTRA_SPACE = "0"

IMAGE_LINGUAS = ""
IMAGE_FEATURES = ""

setup_efi_folder() {
    # Move EFI content from packages expecting /boot to be the ESP location
    if [ -d ${IMAGE_ROOTFS}/boot/EFI ]; then
        mv ${IMAGE_ROOTFS}/boot/EFI/* ${IMAGE_ROOTFS}/EFI
    fi
    find ${IMAGE_ROOTFS} -mindepth 1 ! -path "${IMAGE_ROOTFS}/EFI*" -exec rm -rf {} +
}
IMAGE_PREPROCESS_COMMAND:append = " setup_efi_folder"

do_uki[vardeps] += "KERNEL_CMDLINE_EXTRA"

# ESP image is currently only used on EFI machines
REQUIRED_MACHINE_FEATURES = "efi"
