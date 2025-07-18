SUMMARY = "QMI Framework"
DESCRIPTION = "QMI Framework is a messaging interface, \
enabling users to implement clients and servers for inter-process communication (IPC)."

HOMEPAGE = "https://github.com/quic/qmi-framework"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=65b8cd575e75211d9d4ca8603167da1c"

inherit autotools

SRCREV = "10de3bcdcff1f687f280cb34734ddc4af4dc9695"
SRC_URI = "git://github.com/quic/qmi-framework.git;protocol=https;branch=main"


PV = "0.1.0"

do_configure() {
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
