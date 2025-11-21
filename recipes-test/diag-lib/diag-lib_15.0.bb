SUMMARY = "Prebuilt Qualcomm diagnostic library and utility applications"
DESCRIPTION = "Prebuilt library and utility applications for diagnostic traffic"
LICENSE = "CLOSED"

SRC_URI = "https://softwarecenter.qualcomm.com/nexus/generic/software/chip/component/core-technologies.qclinux.0.0/251015/prebuilt_yocto/diag_${PV}.qcom_armv8a.tar.gz"
SRC_URI[sha256sum] = "7adcff6c4271834d5cfc87f692512d7c07c23f9f871c7e48b7f2e018a3034d0e"

S = "${UNPACKDIR}"

RDEPENDS:${PN} += "glib-2.0"

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
    install -m 0755 ${S}/usr/lib/libdiag.so.1.0.0 ${D}${libdir}/
    ln -sf libdiag.so.1.0.0 ${D}${libdir}/libdiag.so.1
    ln -sf libdiag.so.1.0.0 ${D}${libdir}/libdiag.so

    # Install pkgconfig
    install -m 0644 ${S}/usr/lib/pkgconfig/*.pc ${D}${libdir}/pkgconfig/

    # Install headers
    install -d ${D}${includedir}/diag
    install -m 0644 ${S}/usr/include/diag/*.h ${D}${includedir}/
}
