SUMMARY = "Qualcomm camera algo related libraries"
DESCRIPTION = "Collection of prebuilt libraries of algo related prebuilt libraries."
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/usr/share/doc/${PN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf;md5=4ceffe94cb40cdce6d2f4fb93cc063d1 \
                    file://${UNPACKDIR}/usr/share/doc/${PN}/NOTICE;md5=04facc2e07e3d41171a931477be0c690"

ARTIFACTORY_URL = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto"

SRC_URI = "${ARTIFACTORY_URL}/${BPN}_${PV}_armv8-2a.tar.gz"
SRC_URI[sha256sum] = "00c88d824c15c2cf3b69bd01b277cd67d723194c74aa654da99a2bc3933a7486"
PBT_BUILD_DATE = "251123"
S = "${UNPACKDIR}"

DEPENDS += "glib-2.0 fastrpc protobuf libxml2  virtual/egl virtual/libgles2"

do_install() {
    install -d ${D}${libdir}
    install -d ${D}${datadir}/doc/${PN}

    cp -r ${S}/usr/lib/* ${D}${libdir}

    install -m 0644 ${S}/usr/share/doc/${PN}/NOTICE ${D}${datadir}/doc/${PN}
    install -m 0644 ${S}/usr/share/doc/${PN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf ${D}${datadir}/doc/${PN}    
}

FILES:${PN} = "${libdir}/"

# Skip additional QA checks:
# - arch: The package contains prebuilt binaries or libraries that Yocto cannot validate
#   against the target architecture.
# - already-stripped: binaries are stripped so, Yocto cannot re-strip them (not applicable for prebuilt binaries)
# - dev-so: .so files are installed in main package, not split into -dev.
INSANE_SKIP:${PN} += "arch already-stripped dev-so"

