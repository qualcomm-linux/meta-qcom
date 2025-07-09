SUMMARY = "QMI Framework"
DESCRIPTION = "Qualcomm IPC Router-based QMI Framework"
HOMEPAGE = "https://github.com/quic/qmi-framework"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=65b8cd575e75211d9d4ca8603167da1c"

PACKAGE_ARCH    ?= "${MACHINE_ARCH}"

SRC_URI = "git://github.com/quic/qmi-framework.git;protocol=https;branch=main"
SRCREV = "${AUTOREV}"

PV = "0.3+${SRCPV}"

inherit autotools pkgconfig

DEPENDS = "autoconf automake libtool"
EXTRA_AUTORECONF = "--verbose --force --install"

do_configure:prepend() {
    cd ${S}
    autoreconf -vif
    ./configure --prefix=${prefix} --host=${HOST_SYS} --with-glib
}

do_compile() {
    cd ${S}
    oe_runmake
}

do_install() {
    cd ${S}
    oe_runmake install DESTDIR=${D}
}
