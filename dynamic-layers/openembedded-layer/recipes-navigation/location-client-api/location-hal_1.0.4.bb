SUMMARY = "Qualcomm Location HAL libraries"

DESCRIPTION = "Provides the Qualcomm location hardware abstraction \
layer (HAL) libraries used by upper-layer location services and \
applications. Includes platform abstraction, common utilities, core \
location engine, batching, geofencing, and GNSS interface components, \
along with the runtime accounts and groups required to access location \
sockets and configuration."

LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2998c54c288b081076c9af987bdf4838"

SRC_URI = "git://github.com/qualcomm-linux/location-hal-qcom.git;protocol=https;branch=location.lnx.0.0;tag=v${PV}"
SRCREV  = "cecac263bde2574fa5376782d2d7e056fea8cf42"

inherit autotools pkgconfig useradd

DEPENDS = "glib-2.0 sqlite3 libxml2"

EXTRA_OECONF = "--with-glib"

GROUPADD_PACKAGES = "${PN}"
# locclient group gates access to the location HAL daemon's IPC socket;
# any process that needs to talk to the daemon must belong to this group.
GROUPADD_PARAM:${PN} = "--system locclient"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "--system --shell /sbin/nologin --groups locclient --no-create-home --user-group gps"

do_install:append() {
    install -m 0644 -D ${S}/etc/gps.conf ${D}${sysconfdir}/gps.conf
}
