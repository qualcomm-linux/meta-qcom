DESCRIPTION = "QCOM Iris Video Driver"
LICENSE = "GPL-2.0-only"

LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e"

PV = "0.0+git"

SRC_URI = " \
    git://github.com/qualcomm-linux/video-driver.git;protocol=https;branch=video.qclinux.main \
    file://blacklist-upstream-video.conf \
"
SRCREV  = "0e0fe75fb1910e011358485b078ea207a5c5f3e7"

inherit module

MAKE_TARGETS = "modules"

# This package is designed to run exclusively on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

do_install:append() {
    install -Dm 0644 ${UNPACKDIR}/blacklist-upstream-video.conf \
            ${D}${sysconfdir}/modprobe.d/blacklist-upstream-video.conf
}

# For QCS615 continue using upstream Venus Video Driver as Iris isn't supported
do_install:append:qcs615() {
    rm -f ${D}${sysconfdir}/modprobe.d/blacklist-upstream-video.conf
}

PACKAGE_BEFORE_PN += "${PN}-blacklist"
FILES:${PN}-blacklist = "${sysconfdir}/modprobe.d"
ALLOW_EMPTY:${PN}-blacklist = "1"
