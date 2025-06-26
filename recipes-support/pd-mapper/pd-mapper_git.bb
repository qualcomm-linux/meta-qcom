SUMMARY = "Qualcomm pd-mapper application"
HOMEPAGE = "https://github.com/linux-msm/pd-mapper.git"
SECTION = "devel"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c5d4ab97bca4e843c5afdbf78aa5fdee"

DEPENDS = "qrtr xz"

inherit systemd

SRCREV = "10997ba7c43a3787a40b6b1b161408033e716374"
SRC_URI = "git://github.com/linux-msm/${BPN}.git;branch=master;protocol=https \
"

PV = "0.0+"

do_install () {
    oe_runmake install DESTDIR=${D} prefix=${prefix} servicedir=${systemd_unitdir}/system
}

SYSTEMD_SERVICE:${PN} = "pd-mapper.service"
RDEPENDS:${PN} += "qrtr"
