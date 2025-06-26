SUMMARY = "RMTFS QMI service"
HOMEPAGE = "https://github.com/linux-msm/rmtfs.git"
SECTION = "devel"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ca25dbf5ebfc1a058bfc657c895aac2f"

inherit systemd

SRCREV = "7a5ae7e0a57be3e09e0256b51b9075ee6b860322"
SRC_URI = "git://github.com/linux-msm/${BPN}.git;branch=master;protocol=https"
DEPENDS = "qmic-native qrtr udev"

PV = "0.2+"

do_install () {
    oe_runmake install DESTDIR=${D} prefix=${prefix} servicedir=${systemd_unitdir}/system
}

SYSTEMD_SERVICE:${PN} = "rmtfs.service"
RDEPENDS:${PN} += "qrtr"
