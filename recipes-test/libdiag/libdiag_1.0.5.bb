SUMMARY = "Prebuilt Qualcomm diagnostic library and utility applications"
DESCRIPTION = "Prebuilt library and utility applications for diagnostic traffic"
LICENSE = "LicenseRef-LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/usr/share/doc/diag/LICENSE.QCOM-2.txt;md5=165287851294f2fb8ac8cbc5e24b02b0"

SRC_URI = "https://softwarecenter.qualcomm.com/nexus/generic/software/chip/component/core-technologies.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/diag_15.0.qcom+really${PV}_armv8a.tar.gz"

PBT_BUILD_DATE = "260616"
SRC_URI[sha256sum] = "6f064a43f6e2682ff9201f403cc10168f50846a2c7f402711667b0ee46d50d86"

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
