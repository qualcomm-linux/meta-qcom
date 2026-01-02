SUMMARY = "Prebuilt Qualcomm diagnostic library and utility applications"
DESCRIPTION = "Prebuilt library and utility applications for diagnostic traffic"
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/usr/share/doc/diag/NO.LOGIN.BINARY.LICENSE.QTI.pdf;md5=7a5da794b857d786888bbf2b7b7529c8"

SRC_URI = "https://softwarecenter.qualcomm.com/nexus/generic/software/chip/component/core-technologies.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/diag_15.0.qcom+really${PV}_armv8a.tar.gz"

PBT_BUILD_DATE = "251127"
SRC_URI[sha256sum] = "e6780a404aaa89ffa7f20e659efae4f7b45beaeda8ca1fc05b5a1229ae648c49"

S = "${UNPACKDIR}"

DEPENDS += "glib-2.0"
RDEPENDS:${PN} += "diag-router"

inherit lib_package

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

do_install() {
    install -d ${D}${bindir}
    install -d ${D}${libdir}/pkgconfig
    install -d ${D}${includedir}

    # Install binaries
    install -m 0755 ${S}/usr/bin/* ${D}${bindir}/

    # Install library
    install -m 0755 ${S}/usr/lib/libdiag.so.${PV} ${D}${libdir}/
    ln -sf libdiag.so.${PV} ${D}${libdir}/libdiag.so.1
    ln -sf libdiag.so.${PV} ${D}${libdir}/libdiag.so

    # Install pkgconfig
    install -m 0644 ${S}/usr/lib/pkgconfig/*.pc ${D}${libdir}/pkgconfig/

    # Install headers
    install -d ${D}${includedir}/diag
    install -m 0644 ${S}/usr/include/diag/*.h ${D}${includedir}/diag/
}
