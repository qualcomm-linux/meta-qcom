SUMMARY = "Qualcomm camera core driver and pipeline related libraries"
DESCRIPTION = "Collection of prebuilt libraries to support camera downstream functionality."
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/usr/share/doc/${PN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf;md5=4ceffe94cb40cdce6d2f4fb93cc063d1 \
                    file://${UNPACKDIR}/usr/share/doc/${PN}/NOTICE;md5=d74666dd7176dae6623e6577e3e02302"

ARTIFACTORY_URL = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto"

SRC_URI = "${ARTIFACTORY_URL}/${BPN}_${PV}_armv8-2a.tar.gz"
SRC_URI[sha256sum] = "16f369a4f5469e8494bbceba123ec9aa94acca8c7fe0fb0e5dcb89931ba1101b"
PBT_BUILD_DATE = "251123"
S = "${UNPACKDIR}"

DEPENDS += "camxlib fastrpc protobuf"

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
# - dev-so: .so files are installed in main package, not split into -dev.
# - ldflags: linker flags check (irrelevant for prebuilt binaries)
INSANE_SKIP:${PN} = "dev-so ldflags"
