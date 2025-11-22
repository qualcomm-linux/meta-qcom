SUMMARY = "Qualcomm camera common utility API used by camera driver"
DESCRIPTION = "Collection of prebuilt libraries to support camera downstream functionality."
LICENSE = "CLOSED"

PBT_BUILD_DATE = "251121"
ARTIFACTORY_URL = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto"

SRC_URI = "${ARTIFACTORY_URL}/${BPN}_${PV}_armv8-2a.tar.gz"
SRC_URI[sha256sum] = "6cf60e49e2a65ad6c65b48f05c63116b0b23652aabca29223f6881d0dab418cf"
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
    cp -r ${S}/usr/lib/* ${D}${libdir}
}

FILES_SOLIBSDEV += "${libdir}/qcs9100/*.so"
FILES:${PN} += "${libdir}/qcs9100/*"
