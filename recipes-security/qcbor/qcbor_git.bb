SUMMARY = "Qualcomm QCBOR library"
DESCRIPTION = "QCBOR is a powerful, commercial-quality CBOR encoder-decoder"
HOMEPAGE = "https://github.com/laurencelundblade/QCBOR.git"
SECTION = "libs"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9abe2371333f4ab0e62402a486f308a5"

inherit cmake pkgconfig

SRC_URI = "\
    git://github.com/laurencelundblade/QCBOR.git;branch=master;protocol=https \
"
SRCREV = "7d9f0b787150d739ab50805008bc7142bc9b7822"
PV = "1.5.3+git"

EXTRA_OECMAKE = " -DBUILD_QCBOR_TEST=APP"

do_install:append() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/build/test/qcbortest ${D}${bindir}/
}

FILES:${PN} += " ${bindir}/qcbortest"
