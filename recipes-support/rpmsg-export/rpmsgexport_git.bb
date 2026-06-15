SUMMARY = "Qualcomm RPMSGEXPORT application"
HOMEPAGE = "https://github.com/andersson/rpmsgexport.git"
SECTION = "devel"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ff273e1fd41fa52668171e0817c89724"

inherit systemd

SRCREV = "324d88d668f36c6a5e6a9c2003a050b8a5a3cd60"
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI = "git://github.com/andersson/${BPN}.git;branch=master;protocol=https \
		   file://rpmsg.rules"


PV = "0.3+${SRCPV}"

EXTRA_OEMAKE = "prefix=${prefix} bindir=${bindir} libdir=${libdir} includedir=${includedir} CFLAGS='${CFLAGS}' LDFLAGS='${LDFLAGS}'"

do_install () {
    oe_runmake install DESTDIR=${D} prefix=${prefix} servicedir=${systemd_unitdir}/system ${sysconfdir}
	install -d ${D}/${sysconfdir}/udev/rules.d/
	install -m 0644 ${UNPACKDIR}/rpmsg.rules ${D}${sysconfdir}/udev/rules.d/rpmsg.rules
}

FILES:${PN} += "${systemd_unitdir}/system/"
