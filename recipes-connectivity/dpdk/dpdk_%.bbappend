
# This image is compatible only with aarch64 (ARMv8)
COMPATIBLE_MACHINE:aarch64 = "(.*)"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://0001-config-arm-Allow-generic-platform.patch"

