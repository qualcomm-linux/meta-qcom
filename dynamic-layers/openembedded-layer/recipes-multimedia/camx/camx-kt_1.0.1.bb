SUMMARY = "Qualcomm camera core driver and pipeline related libraries"
DESCRIPTION = "Collection of prebuilt libraries to support camera downstream functionality."
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/usr/share/doc/${PN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf;md5=4ceffe94cb40cdce6d2f4fb93cc063d1 \
                    file://${UNPACKDIR}/usr/share/doc/${PN}/NOTICE;md5=5cb949bdf666599edd42c70b3972950f"

ARTIFACTORY_URL = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto"

SRC_URI = "${ARTIFACTORY_URL}/${BPN}_${PV}_armv8-2a.tar.gz"
SRC_URI[sha256sum] = "b809db730397dbdf08e04a96bf21caec902719d1087063aeb3d017e835d430e2"
PBT_BUILD_DATE = "251123"
S = "${UNPACKDIR}"

DEPENDS += "camxlib-kt fastrpc libxml2 protobuf abseil-cpp"

do_install() {
    install -d ${D}${libdir}

    cp -r ${S}/usr/lib/* ${D}${libdir}

    install -m 0644 ${S}/usr/share/doc/${PN}/NOTICE ${D}${datadir}/doc/${PN}
    install -m 0644 ${S}/usr/share/doc/${PN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf ${D}${datadir}/doc/${PN}
}

FILES:${PN} = "${libdir}/"

# Skip additional QA checks:
# - dev-so: .so files are installed in main package, not split into -dev.
# - ldflags: linker flags check (irrelevant for prebuilt binaries)
INSANE_SKIP:${PN} += "dev-so ldflags"


