SUMMARY = "Qualcomm AudioReach Kernel Module"
DESCRIPTION = "Kernel module for Qualcomm AudioReach"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=0a5a2ad232bafb6974f9a29d1ba0f488"

inherit module

SRC_URI = "git://github.com/AudioReach/audioreach-kernel.git;protocol=https;branch=master"
SRCREV = "f414028b67971a830c956298338831dd8217969a"
S = "${UNPACKDIR}/audioreach-driver-${PV}/audioreach-driver"

EXTRA_OEMAKE += "DESTDIR=${D}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# Restrict build to supported machines
COMPATIBLE_MACHINE:aarch64 = "qcm6490|qcs6490|qcs8275|qcs8300|qcs9075|qcs9100"

FILES:${PN} = "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/updates/audioreach_driver.ko"
RPROVIDES:${PN} += "kernel-module-audio-reach-driver kernel-module-audioreach-driver-${KERNEL_VERSION}"

