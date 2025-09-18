HOMEPAGE = "https://github.com/quic/quic-teec.git"
SUMMARY = "Qualcomm quic-teec library"
SECTION = "libs"
PACKAGE_ARCH = "${MACHINE_ARCH}"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2b1366ebba1ebd9ae25ad19626bbca93"

inherit cmake pkgconfig

SRC_URI = "\
    git://github.com/quic/quic-teec.git;branch=main;protocol=https \
"
SRCREV = "${AUTOREV}"
PV = "git${SRCREV}"
PN = "qcomtee-git"
DEPENDS += " qcbor-git"

FILESPATH += "${WORKSPACE}:"
EXTRA_OECMAKE = " -DBUILD_UNITTEST=ON -DCMAKE_POLICY_VERSION_MINIMUM=3.5"

do_install:append() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/build/tests/unittest ${D}${bindir}/

    install -d ${D}${includedir}/qcomtee
    install ${S}/libqcomtee/include/*.h ${D}${includedir}/qcomtee/
}

FILES:${PN} += " ${bindir} ${includedir}"
