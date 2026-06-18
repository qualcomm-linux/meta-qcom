# SPDX-License-Identifier: BSD-3-Clause

SUMMARY = "Qualcomm RTSS mailbox userspace middleware"
DESCRIPTION = "Userspace middleware libraries and utilities for RTSS mailbox \
IPC communication between APSS and RTSS. Provides librtss_mailbox, \
librtss_safemlib, and the librtss_ota OTA SDK library."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

# ---------------------------------------------------------------------------
# Version tracks
#
# Track 1 — Mailbox middleware  (librtss_mailbox + librtss_safemlib)
#   Tied to RTSS_MB_UAPI_VERSION_MAJOR in rtss_mailbox_uapi.h.
#   Bump SOVERSION when KMD IOCTL struct or numbers change.
#   Bump minor for UMD-only additive API changes.
#   Bump patch for bug fixes with no API/ABI change.
#   Bump PR (r0->r1) for recipe/packaging fix with no source change.
#
# Track 2 — OTA SDK  (librtss_ota)
#   Tied to RTSS_UPD_VER_MAJ/MIN in rtss_ota_lib.h.
#   Changes independently and more frequently than middleware.
#   Version carried by SONAME (librtss_ota.so.1) and package name (qcom-rtss-ota).
# ---------------------------------------------------------------------------
PV = "1.0.0"
PR = "r0"

# qcom-rtss-ota sub-packages carry OTA version independently of middleware PV.
PKGV:qcom-rtss-ota           = "${PV}"
PKGV:qcom-rtss-ota-staticdev = "${PV}"
PKGV:qcom-rtss-ota-dev       = "${PV}"

# Suppress Debian auto-renaming for single-soname OTA sub-packages.
# qcom-rtss-ota installs exactly one soname (librtss_ota.so.1) so
# Yocto debian.bbclass would rename it, breaking RDEPENDS and opkg queries.
DEBIAN_NOAUTONAME:qcom-rtss-ota           = "1"
DEBIAN_NOAUTONAME:qcom-rtss-ota-staticdev = "1"
DEBIAN_NOAUTONAME:qcom-rtss-ota-dev       = "1"

# qcom-rtss-mailbox-kmd installs rtss_mailbox_uapi.h into sysroot via
# do_install:append — must be staged before UMD builds.
DEPENDS = "glib-2.0 qcom-rtss-mailbox-kmd"

SRC_URI = "git://github.com/qualcomm-linux/rtss-mailbox-umd.git;branch=rtss-mailbox-usr.le.0.0;protocol=https;tag=v${PV}"
SRCREV  = "5fb8e0d5e234ab4ab8b704b7128c98e7dd7b4f83"

inherit cmake

EXTRA_OECMAKE += "-DSYSROOTINC_PATH=${STAGING_INCDIR} \
                  -DSYSROOT_INCLUDEDIR=${STAGING_INCDIR}"

# ---------------------------------------------------------------------------
# Package layout
#
#   qcom-rtss-mailbox-umd           librtss_mailbox.so.1, librtss_safemlib.so.1
#   qcom-rtss-mailbox-umd-staticdev librtss_mailbox.a, librtss_safemlib.a
#   qcom-rtss-ota                   librtss_ota.so.1, rtss_ota
#   qcom-rtss-ota-staticdev         librtss_ota.a
#   qcom-rtss-mailbox-umd-utils     rtssdbg, rtss_console, rtss_mbdemo
#   qcom-rtss-mailbox-umd-dev       middleware headers + unversioned .so symlinks
#   qcom-rtss-ota-dev               OTA SDK header + unversioned .so symlink
#
# Internal utils (rtssdbg, rtss_console, rtss_ota) link dynamic — independent
# of static lib availability. End users choose shared or static per their need.
# ---------------------------------------------------------------------------
PACKAGES =+ "qcom-rtss-ota qcom-rtss-ota-staticdev qcom-rtss-ota-dev qcom-rtss-mailbox-umd-utils"

# --- qcom-rtss-mailbox-umd ---
FILES:${PN} += "${libdir}/librtss_mailbox.so.1"
FILES:${PN} += "${libdir}/librtss_mailbox.so.1.*"
FILES:${PN} += "${libdir}/librtss_safemlib.so.1"
FILES:${PN} += "${libdir}/librtss_safemlib.so.1.*"

# --- qcom-rtss-ota ---
FILES:qcom-rtss-ota += "${libdir}/librtss_ota.so.1"
FILES:qcom-rtss-ota += "${libdir}/librtss_ota.so.1.*"
FILES:qcom-rtss-ota += "${bindir}/rtss_ota"

# --- qcom-rtss-mailbox-umd-utils ---
FILES:qcom-rtss-mailbox-umd-utils += "${bindir}/rtssdbg"
FILES:qcom-rtss-mailbox-umd-utils += "${bindir}/rtss_console"
FILES:qcom-rtss-mailbox-umd-utils += "${bindir}/rtss_mbdemo"

# --- qcom-rtss-mailbox-umd-staticdev ---
# Yocto auto-populates ${PN}-staticdev with ${libdir}/*.a — explicit here for clarity.
FILES:${PN}-staticdev += "${libdir}/librtss_mailbox.a"
FILES:${PN}-staticdev += "${libdir}/librtss_safemlib.a"

# --- qcom-rtss-ota-staticdev ---
FILES:qcom-rtss-ota-staticdev += "${libdir}/librtss_ota.a"

# --- qcom-rtss-mailbox-umd-dev ---
FILES:${PN}-dev += "${includedir}/rtss_mailbox_api.h"
FILES:${PN}-dev += "${libdir}/librtss_mailbox.so"
FILES:${PN}-dev += "${libdir}/librtss_safemlib.so"

# --- qcom-rtss-ota-dev ---
FILES:qcom-rtss-ota-dev += "${includedir}/rtss_ota_lib.h"
FILES:qcom-rtss-ota-dev += "${libdir}/librtss_ota.so"

# Runtime deps
RDEPENDS:qcom-rtss-mailbox-umd-utils += "${PN}"
RDEPENDS:qcom-rtss-ota               += "${PN}"
RDEPENDS:qcom-rtss-ota-staticdev     += "qcom-rtss-ota"
RDEPENDS:qcom-rtss-ota-dev           += "qcom-rtss-ota"
RDEPENDS:${PN}-staticdev             += "${PN}"
RDEPENDS:${PN}-dev                   += "${PN}"

do_install:append() {
    install -d ${D}${bindir}
    install -d ${D}${libdir}
    install -d ${D}${includedir}
}

# Prevent unversioned .so symlinks landing in the runtime package.
SOLIBS = ".so"
FILES_SOLIBSDEV = ""

