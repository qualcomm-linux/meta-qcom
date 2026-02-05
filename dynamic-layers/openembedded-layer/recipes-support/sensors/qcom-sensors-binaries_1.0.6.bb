SUMMARY = "Prebuilt Qualcomm sensors libraries and test applications"
DESCRIPTION = "Prebuilt core binaries required for sensor enablement and hardware \
sensor data access. These prebuilt binaries also include test applications to \
validate sensor services functionality through the Sensinghub Interface."
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://LICENSE.qcom-2;md5=f33ba334514c4dfabc6ab7377babb377"

PBT_BUILD_DATE = "260117"
SRC_URI = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/sensors.lnx.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/qcom-sensors-prebuilts_${PV}_armv8a.tar.gz"
SRC_URI[sha256sum] = "766b31a4382524939ea6cbc9db23f2aebd94f2b1250b5d9a8114765652758770"

S = "${UNPACKDIR}"

DEPENDS = "glib-2.0 protobuf-camx sensinghub qmi-framework libdiag fastrpc"

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
    install -d ${D}${sysconfdir}/sensors/config
    install -d ${D}${sysconfdir}/sensors/registry
    install -d ${D}${systemd_system_unitdir}

    # Install binaries
    install -m 0755 ${S}/usr/bin/* ${D}${bindir}/

    # Install library
    oe_libinstall -C ${S}/usr/lib -so libsnsutils ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libsuidlookup ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libsensinghubapiprop ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libQshQmiIDL ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libQshSession ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libSensorTest ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libsnsdiaglog ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libUSTANative ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libSEESalt ${D}${libdir}

    # Install registry and Service files
    install -m 0644 ${S}/etc/sensors/sns_reg_config ${D}${sysconfdir}/sensors/
    install -m 0644 ${S}/etc/sensors/config/* ${D}${sysconfdir}/sensors/config/
    install -m 0644 ${S}/etc/sensors/registry/sns_reg_version ${D}${sysconfdir}/sensors/registry/
    install -m 0644 ${S}${systemd_system_unitdir}/sscrpcd.service ${D}${systemd_system_unitdir}/sscrpcd.service

    # Install pkgconfig
    install -m 0644 ${S}/usr/lib/pkgconfig/*.pc ${D}${libdir}/pkgconfig/

    # Install headers
    install -m 0644 ${S}/usr/include/*.h ${D}${includedir}/
}

SYSTEMD_SERVICE:${PN} = "sscrpcd.service"
