SUMMARY = "Qualcomm QRTR applications and library"
HOMEPAGE = "https://github.com/linux-msm/qrtr.git"
SECTION = "devel"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=15329706fbfcb5fc5edcc1bc7c139da5"

inherit systemd

SRCREV = "d0d471c96e7d112fac6f48bd11f9e8ce209c04d2"
SRC_URI = "git://github.com/linux-msm/${BPN}.git;branch=master;protocol=https"

PV = "0.3+"

EXTRA_OEMAKE = "prefix=${prefix} bindir=${bindir} libdir=${libdir} includedir=${includedir}"

do_install () {
    oe_runmake install DESTDIR=${D} prefix=${prefix} servicedir=${systemd_unitdir}/system
}

SYSTEMD_SERVICE:${PN} = "qrtr-ns.service"
