SUMMARY = "WFA certification testing tool for QCA devices."
HOMEPAGE = "https://github.com/qualcomm/sigma-dut"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://README;md5=e877b35748195c6ab87bf2d1ebed9a89"

SRC_URI = "git://github.com/qualcomm/sigma-dut.git;branch=master;protocol=https"

PV = "1.11+git"
SRCREV = "bd455d362f824f8d15dee305fec32aefafdca0a1"

DEPENDS = "libnl"

do_install () {
	oe_runmake install DESTDIR=${D} BINDIR=${sbindir}
}
