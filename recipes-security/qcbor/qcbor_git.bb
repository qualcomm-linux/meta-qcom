HOMEPAGE = "https://github.com/laurencelundblade/QCBOR.git"
SUMMARY = "Qualcomm QCBOR library"
SECTION = "libs"
PACKAGE_ARCH = "${MACHINE_ARCH}"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9abe2371333f4ab0e62402a486f308a5"

inherit cmake pkgconfig

SRC_URI = "\
    git://github.com/laurencelundblade/QCBOR.git;branch=master;protocol=https \
"
SRCREV = "${AUTOREV}"
PV = "git${SRCREV}"
PN = "qcbor-git"

FILESPATH += "${WORKSPACE}:"
EXTRA_OECMAKE = " -DBUILD_QCBOR_TEST=APP"

do_install:append() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/build/test/qcbortest ${D}${bindir}/
}

FILES:${PN} += " ${bindir}/qcbortest"
