SUMMARY = "Prebuilt Qualcomm sensors libraries and test applications"
DESCRIPTION = "Prebuilt core binaries required for sensor enablement and hardware \
sensor data access. These prebuilt binaries also include test applications to \
validate sensor services functionality through the Sensinghub Interface."
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://LICENSE.qcom-2;md5=f33ba334514c4dfabc6ab7377babb377"

PBT_BUILD_DATE = "260620.1"
SRC_URI = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/sensors.lnx.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/qcom-sensors-prebuilts_${PV}_armv8a.tar.gz"
SRC_URI[sha256sum] = "78ef50da376fcc2ba4e7add016dc8fbdfffdc76c099c021e06b2ac1b1c69d151"

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
    install -d ${D}${nonarch_base_libdir}/firmware/qcom/shikra/sensors/config
    install -d ${D}${sysconfdir}/udev/rules.d
    install -d ${D}${localstatedir}/lib/tqftpserv/sensors/registry

    # Install binaries
    install -m 0755 ${S}/usr/bin/* ${D}${bindir}/

    # Install library
    oe_libinstall -C ${S}/usr/lib -so libsensinghubapiprop ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libsensinghubapipropc ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libQshQmiIDL ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libQshSession ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libsnsdiaglog ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libUSTANative ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libSEESalt ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libsns_direct_channel_stub ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libsns_remote_proc_state_stub ${D}${libdir}

    # Install registry and Service files
    install -m 0644 ${S}/etc/sensors/sns_reg_config ${D}${sysconfdir}/sensors/
    install -m 0644 ${S}/etc/sensors/config/* ${D}${sysconfdir}/sensors/config/
    install -m 0644 ${S}/etc/sensors/registry/sns_reg_version ${D}${sysconfdir}/sensors/registry/
    install -m 0644 ${S}/lib/firmware/qcom/shikra/sensors/config/* ${D}${nonarch_base_libdir}/firmware/qcom/shikra/sensors/config/
    install -m 0644 ${S}/lib/firmware/qcom/shikra/sensors/sns_reg_config ${D}${nonarch_base_libdir}/firmware/qcom/shikra/sensors/
    install -m 0644 ${S}/etc/udev/rules.d/99-rpmsg.rules ${D}${sysconfdir}/udev/rules.d/
    install -m 0644 ${S}/var/lib/tqftpserv/sensors/registry/sns_reg_version ${D}${localstatedir}/lib/tqftpserv/sensors/registry/
    install -m 0644 ${S}/var/lib/tqftpserv/sensors/registry/sensors_registry ${D}${localstatedir}/lib/tqftpserv/sensors/registry/
    install -m 0644 ${S}${systemd_system_unitdir}/sscrpcd.service ${D}${systemd_system_unitdir}/sscrpcd.service

    # Install pkgconfig
    install -m 0644 ${S}/usr/lib/pkgconfig/*.pc ${D}${libdir}/pkgconfig/

    # Install headers
    install -m 0644 ${S}/usr/include/*.h ${D}${includedir}/
}

SYSTEMD_SERVICE:${PN} = "sscrpcd.service"

FILES:${PN} += "${nonarch_base_libdir}/firmware/qcom/shikra/sensors/*"
