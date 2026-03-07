DESCRIPTION = "Qualcomm Camera driver (CAMX)"
HOMEPAGE = "https://github.com/qualcomm-linux/camera-driver"
LICENSE = "GPL-2.0-with-Linux-syscall-note"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "git://github.com/qualcomm-linux/camera-driver.git;protocol=https;branch=camera-kernel.qclinux.0.0"

PV = "0.0+git"
SRCREV = "c62c54bb9c04efe5afb91576be55797aadf635f8"

inherit module

MAKE_TARGETS = "modules"
MODULES_INSTALL_TARGET = "modules_install"

do_install:append() {
    install -d ${D}${includedir}/camx/camera/media
    install -d ${D}${includedir}/camx/camera-kodiak/media

    install -m 0644 ${S}/camera_kt/include/uapi/camera/media/* \
        ${D}${includedir}/camx/camera-kodiak/media/

    install -m 0644 ${S}/camera/include/uapi/camera/media/* \
        ${D}${includedir}/camx/camera/media/
}

# This package is designed to run exclusively on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"
