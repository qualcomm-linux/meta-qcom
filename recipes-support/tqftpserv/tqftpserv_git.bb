SUMMARY = "Qualcomm tqftpserv application"
HOMEPAGE = "https://github.com/linux-msm/tqftpserv.git"
SECTION = "devel"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=424e013ed97b36284f3b9ce27bb16a56"

DEPENDS = "qrtr"

inherit systemd

SRCREV = "de42697a2466cc5ee267ffe36ab4e8494f005fb0"
SRC_URI = "git://github.com/linux-msm/${BPN}.git;branch=master;protocol=https \
"

PV = "0.0+"

do_install () {
    oe_runmake install DESTDIR=${D} prefix=${prefix} servicedir=${systemd_unitdir}/system
}

SYSTEMD_SERVICE:${PN} = "tqftpserv.service"
RDEPENDS:${PN} += "qrtr"
