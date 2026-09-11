SUMMARY = "Qualcomm RTSS Mailbox userspace middleware and tools"
DESCRIPTION = "Userspace middleware libraries and utilities for RTSS mailbox \
IPC communication between APSS and RTSS."
HOMEPAGE = "https://github.com/qualcomm-linux/rtss-mailbox-umd"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=223037c4be0bfc6cf757035432adf983"

SRC_URI = "git://github.com/qualcomm-linux/rtss-mailbox-umd.git;branch=rtss-mailbox-usr.le.0.0;protocol=https;tag=v${PV}"
SRCREV = "667fbda29be9e4d09ff5dd1b35e8dfda17a6ed63"

inherit cmake

DEPENDS = "qcom-rtss-mailbox-uapi-headers glib-2.0"

PACKAGE_BEFORE_PN += "${PN}-utils"

RRECOMMENDS:${PN} += "kernel-module-rtss-mailbox"

FILES:${PN} = "${libdir}/librtss_mailbox.so.* ${libdir}/librtss_safemlib.so.* ${libdir}/librtss_update.so.* ${libdir}/librtss_gpt.so.*"
FILES:${PN}-utils = "${bindir}/rtss_dbg ${bindir}/rtss_console ${bindir}/rtss_mbdemo ${bindir}/rtss_updater"

COMPATIBLE_MACHINE = "qcs9100-ride-sx|qcs8300-ride-sx|iq-9075-evk|iq-8275-evk"
