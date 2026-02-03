SUMMARY = "Time-services Daemon to sync time from Modem to APPS"
DESCRIPTION = "Updates system time from modem when network-camped. Operates independently \
and does not synchronize with systemd-timesyncd, NTP, or chrony. If multiple time sources \
are active, they may conflict. Future enhancement: integrate as an NTP/chrony time source."
HOMEPAGE = "https://github.com/quic/time-services"
SECTION = "devel"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=65b8cd575e75211d9d4ca8603167da1c"

SRC_URI = "git://github.com/quic/time-services.git;branch=main;protocol=https;tag=v${PV}"

DEPENDS = "glib-2.0 qmi-framework"

# NOTE: This daemon operates as a standalone time source and does not interact with
# systemd-timesyncd, NTP, chrony, or other time synchronization services. It updates
# the system time only when the modem reports a time update while network-camped.

inherit autotools pkgconfig systemd

SYSTEMD_SERVICE:${PN} = "time-daemon.service"

EXTRA_OECONF = "--with-qmif-prefix=${STAGING_DIR_TARGET}${prefix}"
