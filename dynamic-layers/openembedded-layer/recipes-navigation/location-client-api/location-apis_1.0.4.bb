SUMMARY = "Qualcomm GNSS location client APIs and location_hal_daemon service"

DESCRIPTION = "Provides the Qualcomm Location Client APIs used by \
upper-layer applications to access GNSS positioning, batching, \
geofencing, and other location engine features over IPC"

LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=724395ab86695d415998c63582feff8c"

SRC_URI = "git://github.com/qualcomm-linux/location-apis-qcom.git;protocol=https;branch=location.lnx.0.0;tag=v${PV}"
SRCREV = "aa950a783a4b8fbb41807bab4d840d44643878a9"

inherit autotools pkgconfig systemd

DEPENDS = "glib-2.0 location-hal protobuf protobuf-native qmi-framework libcap"

EXTRA_OECONF = "--with-glib --with-systemd \
                --with-systemdsystemunitdir=${systemd_system_unitdir} \
                --with-tmpfilesdir=${nonarch_libdir}/tmpfiles.d"

RDEPENDS:${PN} = "location-hal"
SYSTEMD_SERVICE:${PN} = "location_hal_daemon.service"
FILES:${PN} += "${nonarch_libdir}/tmpfiles.d"

# Keep the client API test binary out of the production package
PACKAGE_BEFORE_PN += "${PN}-testapp"

RDEPENDS:${PN}-testapp = "${PN}"

FILES:${PN}-testapp = "${bindir}/location_client_api_testapp"
