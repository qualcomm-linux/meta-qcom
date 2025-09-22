SUMMARY = "Mink IDL compiler (prebuilt binary)"
DESCRIPTION = " \
Mink IDL is used to describe programming interfaces that can be used to communicate across security domain boundaries. \
Once an interface is described in an IDL source file, the Mink IDL compiler can generate target language header files. \
"
HOMEPAGE = "https://github.com/quic/mink-idl-compiler"
SECTION = "devel"

LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

SRC_URI = "https://github.com/quic/mink-idl-compiler/releases/download/v0.2.0/idlc;downloadfilename=minkidlc;unpack=0"
SRC_URI[sha256sum] = "2a6dd5b2fdad5e307489849b3a4ce56daf241ff517e89e1eb207813008e558d7"

inherit native

COMPATIBLE_HOST = "x86_64.*-linux"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/minkidlc ${D}${bindir}/minkidlc
}

FILES:${PN} += "${bindir}/minkidlc"

