SUMMARY = "Prebuilt bootlader images for Dragonboard 410c"

LICENSE = "LICENSE.qcom"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4d087ee0965cb059f1b2f9429e166f64"

SRC_URI = "https://releases.linaro.org/96boards/dragonboard410c/linaro/rescue/17.09/dragonboard410c_bootloader_sd_linux-88.zip"
SRC_URI[md5sum] = "e15da2a623442d66587aea506599fb69"
SRC_URI[sha256sum] = "9885f915ebd4986432340cf1d03b8fd2bfdd97ad6a4a7466200fddbe41cdcf5c"

BOOTBINARIES = "."

QCOM_BOOT_IMG_SUBDIR = "dragonboard410c"

include firmware-qcom-boot-common.inc
