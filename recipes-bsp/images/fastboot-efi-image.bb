DESCRIPTION = "Flash image booting fastboot.efi from the EFI System Partition, \
without a root filesystem"

inherit image features_check

require recipes-kernel/images/esp-qcom-common.inc
require conf/image-uefi.conf

REQUIRED_MACHINE_FEATURES = "efi"

PACKAGE_INSTALL = ""

IMAGE_FSTYPES = "vfat ${@bb.utils.contains('IMAGE_CLASSES', 'image_types_qcom', 'qcomflash', '', d)}"

# The vfat image built here is the ESP, there is no rootfs to flash.
QCOM_ESP_IMAGE = ""
QCOM_ESP_FILE = "${IMGDEPLOYDIR}/${IMAGE_LINK_NAME}.vfat"
IMAGE_QCOMFLASH_FS_TYPE = ""
IMAGE_TYPEDEP:qcomflash = "vfat"

setup_fastboot_efi() {
    find ${IMAGE_ROOTFS} -mindepth 1 -maxdepth 1 -exec rm -rf {} +
    install -D -m 0644 ${DEPLOY_DIR_IMAGE}/fastboot.efi \
        ${IMAGE_ROOTFS}${EFIDIR}/${EFI_BOOT_IMAGE}
}
IMAGE_PREPROCESS_COMMAND:append = " setup_fastboot_efi"

do_image[depends] += "fastboot-efi:do_deploy"
