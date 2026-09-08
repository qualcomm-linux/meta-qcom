SUMMARY = "Qualcomm Location Client APIs and location_hal_daemon service"

DESCRIPTION = "Provides the Qualcomm Location Client APIs used by \
upper-layer applications to access GNSS positioning, batching, \
geofencing, and other location engine features over IPC"

LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=724395ab86695d415998c63582feff8c"

SRC_URI = "git://github.com/qualcomm-linux/location-apis-qcom.git;protocol=https;branch=location.lnx.0.0;tag=v${PV}"
SRCREV = "e5627ee5a0e39ad95bc17e6348e09664ac5371d1"

inherit autotools pkgconfig systemd

DEPENDS = "glib-2.0 location-hal protobuf protobuf-native qmi-framework libcap"

# qmi-framework 0.1.3 (currently pinned in meta-qcom) predates upstream
# pkg-config support, so point configure.ac at the staged headers directly
# instead of relying on PKG_CHECK_MODULES([QMIFW], [qmi-framework]).
EXTRA_OECONF += "--with-glib --with-systemd --with-qmi=${STAGING_INCDIR} \
                 --with-systemdsystemunitdir=${systemd_system_unitdir} \
                 --with-tmpfilesdir=${nonarch_libdir}/tmpfiles.d"
CPPFLAGS += "-DLOC_QCLINUX_TARGET"

RDEPENDS:${PN} = "location-hal"
SYSTEMD_SERVICE:${PN} = "location_hal_daemon.service"

FILES:${PN} += "${nonarch_libdir}/tmpfiles.d"
