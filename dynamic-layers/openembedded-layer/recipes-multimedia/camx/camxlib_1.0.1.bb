SUMMARY = "Qualcomm camera algo related libraries"
DESCRIPTION = "Collection of prebuilt libraries of algo related prebuilt libraries."
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/usr/share/doc/${PN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf;md5=4ceffe94cb40cdce6d2f4fb93cc063d1 \
                    file://${UNPACKDIR}/usr/share/doc/${PN}/NOTICE;md5=198d001f49d9a313355d5219f669a76c"

ARTIFACTORY_URL = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto"

SRC_URI = "${ARTIFACTORY_URL}/${BPN}_${PV}_armv8-2a.tar.gz"
SRC_URI[sha256sum] = "e71ff83f1cf04ec8afd4700e56a7c97ca0231fbaebfd4856f58b099c545e9adf"
PBT_BUILD_DATE = "251123"
S = "${UNPACKDIR}"

DEPENDS += "camxcommon fastrpc protobuf libxml2"

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

    install -m 0644 ${S}/usr/share/doc/${PN}/NOTICE ${D}${datadir}/doc/${PN}
    install -m 0644 ${S}/usr/share/doc/${PN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf ${D}${datadir}/doc/${PN}
}

FILES:${PN} += "${libdir}/"

# Skip additional QA checks:
# - arch: The package contains prebuilt binaries or libraries that Yocto cannot validate
#   against the target architecture.
# - already-stripped: binaries are stripped so, Yocto cannot re-strip them (not applicable for prebuilt binaries)
# - dev-so: .so files are installed in main package, not split into -dev.
# - ldflags: linker flags check (irrelevant for prebuilt binaries)
INSANE_SKIP:${PN} = "arch already-stripped dev-so ldflags"
