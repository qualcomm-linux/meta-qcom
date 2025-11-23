SUMMARY = "Qualcomm camera development related libraries, binary"
DESCRIPTION = "Collection of prebuilt libraries to support camera downstream functionality."
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/usr/share/doc/${PN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf;md5=4ceffe94cb40cdce6d2f4fb93cc063d1 \
                    file://${UNPACKDIR}/usr/share/doc/${PN}/NOTICE;md5=9c80f7af9f8aeb34ef0beff70a22e3d3"

ARTIFACTORY_URL = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto"

SRC_URI = "${ARTIFACTORY_URL}/${BPN}_${PV}_armv8-2a.tar.gz"
SRC_URI[sha256sum] = "0680b2ef6e3bf3497139bf9663ed2c1f1954a06589409ba223f1fe6a05c3760b"
PBT_BUILD_DATE = "251123"
S = "${UNPACKDIR}"

DEPENDS += "camx-kt fastrpc camxlib-kt glib-2.0 libxml2 virtual/libopencl1"

do_install() {
    install -d ${D}${libdir}
    install -d ${D}${bindir}
    install -d ${D}${datadir}/doc/${PN}
	
    cp -r ${S}/usr/lib/* ${D}${libdir}
    cp -r ${S}/usr/bin/* ${D}${bindir}

    install -m 0644 ${S}/usr/share/doc/${PN}/NOTICE ${D}${datadir}/doc/${PN}
    install -m 0644 ${S}/usr/share/doc/${PN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf ${D}${datadir}/doc/${PN}
}

FILES:${PN} += "\
    ${libdir}/ \
    ${bindir}/"

# Skip additional QA checks:
# - dev-so: .so files are installed in main package, not split into -dev.
# - ldflags: linker flags check (irrelevant for prebuilt binaries)
INSANE_SKIP:${PN} += "dev-so ldflags"
