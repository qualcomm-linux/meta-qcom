DESCRIPTION = "Boot config ELFs carrying the OEM capsule root certificate"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

COMPATIBLE_MACHINE = "(qcom)"

inherit nopackages

inherit qcom-oem-cert

PACKAGE_ARCH = "${MACHINE_ARCH}"
