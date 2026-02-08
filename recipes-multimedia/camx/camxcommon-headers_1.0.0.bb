SUMMARY = "CamX common headers"

DESCRIPTION = "This recipe provides headers for all Qualcomm CamX stacks"

LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://usr/share/doc/${BPN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf;md5=7a5da794b857d786888bbf2b7b7529c8"

PBT_BUILD_DATE = "260203"
SRC_URI = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/${BPN}_${PV}_armv8-2a.tar.gz"
SRC_URI[sha256sum] = "eb85fab4fb895e8223d323231b458c78a167cfcbdbc68554bcf9a9aba10dad95"

S = "${UNPACKDIR}"

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

# Disable configure and compile steps since this recipe uses prebuilt binaries.
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${includedir}/camx/kodiak
    install -d ${D}${datadir}/doc/${BPN}

    cp -r ${S}/usr/include/*  ${D}${includedir}/

    install -m 0644 ${S}/usr/share/doc/${BPN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf ${D}${datadir}/doc/${BPN}
}
