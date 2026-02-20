SUMMARY = "WFA certification testing tool for QCA devices."
HOMEPAGE = "https://github.com/qca/sigma-dut"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://README;md5=edb3527809487b74b4d4a7e02b05acf0"

SRC_URI = "git://github.com/qca/sigma-dut.git;branch=master;protocol=https"

PV = "1.11+git"
SRCREV = "ead874cb868cd99e93ff63435a2c454ebc668866"

DEPENDS = "libnl"

do_install () {
	oe_runmake install DESTDIR=${D} BINDIR=${sbindir}
}
