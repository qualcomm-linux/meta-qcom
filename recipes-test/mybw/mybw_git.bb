SUMMARY = "Quick hack to measure memory read performance."
HOMEPAGE = "https://github.com/andersson/mybw"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://mybw.c;beginline=2;endline=29;md5=3489beaa3d1785b9e0ffe48b6a2eb19c"

SRCREV = "f4bdeee1266c273e308d0651522bb59afb5b8211"
SRC_URI = "git://github.com/andersson/mybw;protocol=https;branch=main \
           file://0001-makefile-Allow-CFLAGS-LDFLAGS-from-environment.patch \
           "

PV = "0.0+git"

do_compile () {
	oe_runmake
}

do_install () {
	install -d ${D}${bindir}
	install -m 0755 mybw ${D}${bindir}/mybw
}

LDFLAGS += "-lgcc"
