SUMMARY = "Prebuilt Qualcomm sensors libraries and test applications"
DESCRIPTION = "Prebuilt core binaries required for sensor enablement and hardware \
sensor data access. These prebuilt binaries also include test applications to \
validate sensor services functionality through the Sensinghub Interface."
LICENSE = "LicenseRef-LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://LICENSE.qcom-2;md5=f33ba334514c4dfabc6ab7377babb377"

PBT_BUILD_DATE = "260928.1"
SRC_URI = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/sensors.qli.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/qcom-sensors-prebuilts_${PV}_armv8a.tar.gz"
SRC_URI[sha256sum] = "7588448c9e0c27f67a453a7b417b25a46e022e420d2c75c912b44b12ec254a07"

S = "${UNPACKDIR}"

DEPENDS = "glib-2.0 protobuf sensinghub qmi-framework libdiag fastrpc"

inherit systemd

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

do_install() {
    install -d ${D}${bindir}
    install -d ${D}${libdir}
    install -d ${D}${libdir}/pkgconfig
    install -d ${D}${includedir}

    # Install binaries
    install -m 0755 ${S}/usr/bin/* ${D}${bindir}/

    # Install library
    oe_libinstall -C ${S}/usr/lib -so libsensinghubapiprop ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libsensinghubapipropc ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libsnsdiaglog ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libsnsdiaglog-c ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libUSTANative ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libSEESalt ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libsns_direct_channel_stub ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libsns_remote_proc_state_stub ${D}${libdir}

    # Install pkgconfig
    install -m 0644 ${S}/usr/lib/pkgconfig/*.pc ${D}${libdir}/pkgconfig/

    # Install headers
    install -m 0644 ${S}/usr/include/*.h ${D}${includedir}/
}
