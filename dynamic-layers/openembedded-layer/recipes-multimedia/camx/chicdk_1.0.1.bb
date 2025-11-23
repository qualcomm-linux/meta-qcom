SUMMARY = "Qualcomm camera development related libraries, binary"
DESCRIPTION = "Collection of prebuilt libraries to support camera downstream functionality."
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/usr/share/doc/${PN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf;md5=4ceffe94cb40cdce6d2f4fb93cc063d1 \
                    file://${UNPACKDIR}/usr/share/doc/${PN}/NOTICE;md5=4097f46d763d038fbd21473c7bd18ef4"

ARTIFACTORY_URL = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto"

SRC_URI = "${ARTIFACTORY_URL}/${BPN}_${PV}_armv8-2a.tar.gz"
SRC_URI[sha256sum] = "af62163c20296a0b09c2e742516ed9a40c07fb61bb694bb36e0cbb7d92e4de8e"
PBT_BUILD_DATE = "251123"
S = "${UNPACKDIR}"

DEPENDS += "camx camxcommon fastrpc camxlib glib-2.0 libxml2"

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

# Disable configure and compile steps since this recipe uses prebuilt binaries.
do_configure[noexec] = "1"
do_compile[noexec] = "1"

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
# - already-stripped: binaries are stripped so, Yocto cannot re-strip them (not applicable for prebuilt binaries)
# - ldflags: linker flags check (irrelevant for prebuilt binaries)
INSANE_SKIP:${PN} = "already-stripped dev-so ldflags"
