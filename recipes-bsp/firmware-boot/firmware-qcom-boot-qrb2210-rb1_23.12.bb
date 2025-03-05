SUMMARY = "Prebuilt bootlader images for Qualcomm RB1"

LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/${BOOTBINARIES}/LICENSE;md5=cbbe399f2c983ad51768f4561587f000"

SRC_URI = "https://releases.linaro.org/96boards/rb1/linaro/rescue/23.12/rb1-bootloader-emmc-linux-47528.zip"
SRC_URI[sha256sum] = "c75b6c63eb24c8ca36dad08ba4d4e93f3f4cd7dce60cf1b6dfb5790dc181cc3d"

BOOTBINARIES = "rb1-bootloader-emmc-linux-47528"

include firmware-qcom-boot-common.inc
