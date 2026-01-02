SUMMARY = "RMTFS QMI service"
HOMEPAGE = "https://github.com/linux-msm/rmtfs.git"
SECTION = "devel"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ca25dbf5ebfc1a058bfc657c895aac2f"

inherit systemd

SRCREV = "44facf5694036ebda53fd09c9535774982df5247"
SRC_URI = "git://github.com/linux-msm/${BPN}.git;branch=master;protocol=https;tag=v1.1.1"
DEPENDS = "qmic-native qrtr udev"

do_install () {
    oe_runmake install DESTDIR=${D} prefix=${prefix} servicedir=${systemd_unitdir}/system
}

SYSTEMD_PACKAGES = "${PN} ${PN}-dir"

SYSTEMD_SERVICE:${PN} = "rmtfs.service"
RDEPENDS:${PN} += "qrtr"

PACKAGES += "${PN}-dir"
SYSTEMD_SERVICE:${PN}-dir = "rmtfs-dir.service"
RDEPENDS:${PN}-dir += "${PN}"
