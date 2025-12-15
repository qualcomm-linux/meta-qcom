SUMMARY = "Prebuilt Qualcomm diagnostic router application"
DESCRIPTION = "Prebuilt routing application for diagnostic traffic"
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://usr/share/doc/diag-router/NO.LOGIN.BINARY.LICENSE.QTI.pdf;md5=7a5da794b857d786888bbf2b7b7529c8"

SRC_URI = "https://softwarecenter.qualcomm.com/nexus/generic/software/chip/component/core-technologies.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/diag-router_15.0+really${PV}_armv8a.tar.gz"

PBT_BUILD_DATE = "251127"
SRC_URI[sha256sum] = "18c7fbe1e3cd6ef470cee7fa17210419795060c5ac1e092e14af204580e2908a"

S = "${UNPACKDIR}"

DEPENDS += "glib-2.0 qrtr"
RPROVIDES:${PN} = "virtual-diag-router"
RCONFLICTS:${PN} = "diag"

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

do_install() {
    install -d ${D}${bindir}

    # Install binaries
    install -m 0755 ${S}/usr/bin/* ${D}${bindir}/
}
