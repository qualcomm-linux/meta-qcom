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
SRCREV  = "96b21c90da387747155debb9f482fa11eed9bf74"
S = "${UNPACKDIR}/${BP}"

inherit autotools pkgconfig useradd

DEPENDS = "glib-2.0 sqlite3 libxml2"

EXTRA_OECONF = " --with-glib"
CPPFLAGS:append = " -DLOC_QCLINUX_TARGET "

GROUPADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "--system locclient"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "--system --groups locclient --no-create-home --user-group gps"

do_install:append() {
    #Install default gps.conf file
    install -m 0644 -D ${S}/etc/gps.conf ${D}${sysconfdir}/gps.conf
    ## Ownership to gps:gps for gps.conf
    chown gps:gps ${D}${sysconfdir}/gps.conf
}
