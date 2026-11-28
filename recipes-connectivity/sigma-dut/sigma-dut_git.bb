SUMMARY = "WFA certification testing tool for QCA devices."
HOMEPAGE = "https://github.com/qualcomm/sigma-dut"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://README;md5=e877b35748195c6ab87bf2d1ebed9a89"

SRC_URI = "git://github.com/qualcomm/sigma-dut.git;branch=master;protocol=https"

PV = "1.11+git"
SRCREV = "fb47719c83dba67707c9557178ca12865bef775e"

DEPENDS = "libnl"

do_install () {
	oe_runmake install DESTDIR=${D} BINDIR=${sbindir}
}
