SUMMARY = "WFA certification testing tool for QCA devices."
HOMEPAGE = "https://github.com/qca/sigma-dut"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://README;md5=edb3527809487b74b4d4a7e02b05acf0"

SRC_URI = "git://github.com/qca/sigma-dut.git;branch=master;protocol=https"

PV = "1.11+git"
SRCREV = "${AUTOREV}"

DEPENDS = "libnl"

do_install () {
	oe_runmake install DESTDIR=${D} BINDIR=${sbindir}
}
