SUMMARY = "Qualcomm camera common utility API used by all CamX components"
DESCRIPTION = " Qualcomm CamX common utility libraries \
    This package provides the foundational utility libraries shared across all \
    components of the Qualcomm Camera X (CamX). It is separated \
    from the main qcom-camx package as these utilities are required by camx, \
    chi-cdk, and camx-lib during compilation."
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://usr/share/doc/${BPN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf;md5=7a5da794b857d786888bbf2b7b7529c8"

SRC_URI = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/${BPN}_${PV}_armv8-2a.tar.gz"
SRC_URI[sha256sum] = "2a3e534ec3715741bce958b1c1f48d2983351cdf64925bafb7ce72f0914c0121"
PBT_BUILD_DATE = "251207"
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
    install -d ${D}${datadir}/doc/${BPN}

    cp -r ${S}/usr/lib/* ${D}${libdir}
    install -m 0644 ${S}/usr/share/doc/${BPN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf ${D}${datadir}/doc/${BPN}
}

RPROVIDES:${PN} = "camxcommon-monaco"

FILES:${PN} += "${libdir}/lemans/*${SOLIBS}"
FILES:${PN}-dev += "${libdir}/lemans/*${SOLIBSDEV}"
