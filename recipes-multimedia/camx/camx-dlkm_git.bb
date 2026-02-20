DESCRIPTION = "Qualcomm Camera driver (CAMX)"
HOMEPAGE = "https://github.com/qualcomm-linux/camera-driver"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "git://github.com/qualcomm-linux/camera-driver.git;protocol=https;branch=camera-kernel.qclinux.0.0"

PV = "0.0+git"
SRCREV = "3db4525b96d9d7730dedcae7ae50b9a89669d1cf"

inherit module

MAKE_TARGETS = "modules"
MODULES_INSTALL_TARGET = "modules_install"

do_install:append() {
    install -d ${D}${includedir}/camera/media
    install -d ${D}${includedir}/camera_kt/media

    install -m 0644 ${S}/camera_kt/include/uapi/camera/media/* \
        ${D}${includedir}/camera_kt/media/

    install -m 0644 ${S}/camera/include/uapi/camera/media/* \
        ${D}${includedir}/camera/media/
}

# This package is designed to run exclusively on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"
