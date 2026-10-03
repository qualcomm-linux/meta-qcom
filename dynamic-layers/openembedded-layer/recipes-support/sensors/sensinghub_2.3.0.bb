SUMMARY = "Qualcomm Sensing hub library"
DESCRIPTION = "Userspace libraries that provides interface to interact with Qualcomm Sensing Hub"
HOMEPAGE = "https://github.com/qualcomm/sensinghub"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=9701d0ef17353f1d05d7b74c8712ebbd"

SRCREV = "7c50fa7c599721a99087bb2ccadcfadf129025ad"

SRC_URI = "git://github.com/qualcomm/sensinghub.git;protocol=https;branch=main;tag=v${PV}"

DEPENDS = "protobuf protobuf-native glib-2.0 nanopb-runtime nanopb-generator-native qmi-framework fastrpc"

inherit autotools pkgconfig systemd

EXTRA_OECONF = "--enable-versioned-lib"
EXTRA_OECONF += " --with-qmi-oss "
EXTRA_OECONF += " --with-qmi=${STAGING_INCDIR}/qmi_framework"
EXTRA_OECONF += " --with-base-libdir=${nonarch_base_libdir}"
EXTRA_OECONF += " --with-fastrpc-includes=${STAGING_INCDIR}/fastrpc"

INSANE_SKIP:${PN} += "dev-so"

FILES:${PN} += " \
${libdir}/lib*.so* \
${sysconfdir}/sensors \
${nonarch_base_libdir}/firmware/qcom/shikra/sensors \
${localstatedir}/lib/tqftpserv/sensors/registry \
${systemd_unitdir}/system/sensors.qti.service \
"

FILES:${PN}-dev += " \
${includedir} \
${libdir}/pkgconfig \
${libdir}/lib*.so \
"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/services/sensorsdaemon/sensors.qti.service \
        ${D}${systemd_unitdir}/system/sensors.qti.service
}

SYSTEMD_SERVICE:${PN} = "sensors.qti.service"
