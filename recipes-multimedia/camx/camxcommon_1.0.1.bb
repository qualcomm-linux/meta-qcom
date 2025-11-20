SUMMARY = "Qualcomm camera common utility API used by all CamX components"
DESCRIPTION = "Collection of prebuilt libraries to support common utility API used by all CamX components."
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/usr/share/doc/${PN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf;md5=7a5da794b857d786888bbf2b7b7529c8"

SRC_URI = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/${BPN}_${PV}_armv8-2a.tar.gz"
SRC_URI[sha256sum] = "0d80f438c0bacd924e02b7d151ca9c3a484ad210449bbc0e70b0edd2b48b6d50"
PBT_BUILD_DATE = "251125"
S = "${UNPACKDIR}"

DEPENDS += "glib-2.0"

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

# Disable configure and compile steps since this recipe uses prebuilt binaries.
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${libdir}
    install -d ${D}${datadir}/doc/${PN}

    cp -r ${S}/usr/lib/* ${D}${libdir}
    install -m 0644 ${S}/usr/share/doc/${PN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf ${D}${datadir}/doc/${PN}
}

FILES:${PN} += "${libdir}/qcs9100/*${SOLIBS}"
FILES:${PN}-dev += "${libdir}/qcs9100/*${SOLIBSDEV}"
