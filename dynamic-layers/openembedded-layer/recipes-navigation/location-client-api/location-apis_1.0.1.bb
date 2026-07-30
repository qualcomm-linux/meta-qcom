SUMMARY = "Qualcomm location client APIs and location_hal_daemon service"

DESCRIPTION = "Provides the Qualcomm Location Client APIs used by \
upper-layer applications to access GNSS positioning, batching, \
geofencing, and other location engine features over IPC"

LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/${BP}/LICENSE.txt;md5=724395ab86695d415998c63582feff8c"

SRC_URI = "git://github.com/qualcomm-linux/location-apis-qcom.git;protocol=https;branch=location.lnx.0.0;tag=v${PV} \
           file://location_hal_daemon.service \
           file://location_hal_daemon-tmpfilesd.conf \
           "
SRCREV = "2242d59f85ac64e7007bdac5b64615f4956c021a"


inherit autotools pkgconfig systemd

DEPENDS = "glib-2.0 location-hal protobuf protobuf-native qmi-framework libcap"
RDEPENDS:${PN} = "location-hal"

# qmi-framework 0.1.3 (currently pinned in meta-qcom) predates upstream
# pkg-config support, so point configure.ac at the staged headers directly
# instead of relying on PKG_CHECK_MODULES([QMIFW], [qmi-framework]).
EXTRA_OECONF += " --with-glib --with-systemd --with-qmi=${STAGING_INCDIR}"
CPPFLAGS:append = " -DLOC_QCLINUX_TARGET "

SYSTEMD_SERVICE:${PN} = "location_hal_daemon.service"

do_compile:prepend () {
    echo "Running location_api_msg_protobuf_gen.sh"
    cd ${S}/location_api_msg_proto
    export LD_LIBRARY_PATH="${STAGING_DIR_NATIVE}/usr/lib/x86_64-linux-gnu:${LD_LIBRARY_PATH}"
    bash ./location_api_msg_protobuf_gen.sh
    cd -
}

do_install:append () {
    install -d ${D}${sysconfdir}/tmpfiles.d/
    install -m 0644 ${UNPACKDIR}/location_hal_daemon-tmpfilesd.conf ${D}${sysconfdir}/tmpfiles.d/${BPN}.conf

    install -Dm 0644 ${UNPACKDIR}/location_hal_daemon.service \
            ${D}${systemd_system_unitdir}/location_hal_daemon.service
}
