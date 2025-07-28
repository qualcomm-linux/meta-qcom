SUMMARY = "WFA certification testing tool for QCA devices."
HOMEPAGE = "https://github.com/qca/sigma-dut"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://README;md5=edb3527809487b74b4d4a7e02b05acf0"

SRC_URI = "git://github.com/qca/sigma-dut.git;branch=master;protocol=https"

PV = "1.11+git"
SRCREV = "21f3bb1c426367d9f480acb4f5f011dd0aa17875"

DEPENDS = "libnl"

do_install () {
	oe_runmake install DESTDIR=${D} BINDIR=${sbindir}
}
