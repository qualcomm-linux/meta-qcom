DESCRIPTION = "Qualcomm Camera driver (CAMX)"
HOMEPAGE = "https://github.com/qualcomm-linux/camera-driver"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "git://github.com/qualcomm-linux/camera-driver.git;protocol=https;branch=camera-kernel.qclinux.0.0"

PV = "0.0+git"
SRCREV = "336a8f53aa022c4c8795e7d65b315493f40963d9"

inherit module

MAKE_TARGETS = "modules"
MODULES_INSTALL_TARGET = "modules_install"

# This package is designed to run exclusively on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"
