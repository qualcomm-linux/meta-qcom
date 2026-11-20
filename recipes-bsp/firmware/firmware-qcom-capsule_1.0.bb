DESCRIPTION = "UEFI FMP capsule for Qualcomm platforms"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

COMPATIBLE_MACHINE = "hamoa|qcm6490|qcs615|qcs8300|qcs9100"

PROVIDES += "virtual/qcom-capsule-firmware"

inherit qcom-capsule
