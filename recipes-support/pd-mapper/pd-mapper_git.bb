SUMMARY = "Qualcomm pd-mapper application"
HOMEPAGE = "https://github.com/linux-msm/pd-mapper.git"
SECTION = "devel"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c5d4ab97bca4e843c5afdbf78aa5fdee"

DEPENDS = "qrtr xz"

inherit systemd

SRCREV = "5ecd2fe926aca7abfe40724177f63b942cff3947"
SRC_URI = "git://github.com/linux-msm/${BPN}.git;branch=master;protocol=https \
"

PV = "1.0+"

do_install () {
    oe_runmake install DESTDIR=${D} prefix=${prefix} servicedir=${systemd_unitdir}/system
}

SYSTEMD_SERVICE:${PN} = "pd-mapper.service"
RDEPENDS:${PN} += "qrtr"
