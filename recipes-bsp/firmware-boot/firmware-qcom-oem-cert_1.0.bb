DESCRIPTION = "Boot config ELFs carrying the OEM capsule root certificate"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

COMPATIBLE_MACHINE = "hamoa|qcm6490|qcs615|qcs8300|qcs9100"

# Deploy-only. PACKAGES = "" would leave do_package running with nothing to
# split, and buildhistory then fails listing a packages-split that was
# never created.
inherit nopackages

inherit qcom-oem-cert

PACKAGE_ARCH = "${MACHINE_ARCH}"
