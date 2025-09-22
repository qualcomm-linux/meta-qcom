SUMMARY = "Qualcomm MinkIPC applications and library"
DESCRIPTION = " \
MINK ('Mink is Not a Kernel') is a capability-based security framework, \
which is a synchronous message passing facility based on the Object-Capability model, \
designed to facilitate secure communication between different domains. \
qteesupplicant service is designed for invocation dispatch and handling callbacks. \
"
HOMEPAGE = "https://github.com/qualcomm/minkipc.git"
SECTION = "devel"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2b1366ebba1ebd9ae25ad19626bbca93"

inherit cmake systemd lib_package

SRC_URI += "\
    git://github.com/qualcomm/minkipc.git;branch=main;protocol=https \
    file://qteesupplicant.service \
"
SRCREV = "18cbaabfb339c0751e239f2b2037bad52a0715b8"
PV = "0.0+git"
DEPENDS = "pkgconfig-native qcbor qcomtee minkidlc-native glib-2.0"

PACKAGES += "${PN}-systemd"
RRECOMMENDS:${PN} += "${PN}-systemd"
SYSTEMD_PACKAGES = "${PN} ${PN}-systemd"
SYSTEMD_SERVICE:${PN}-systemd = "qteesupplicant.service"
SYSTEMD_AUTO_ENABLE:${PN}-systemd = "disable"

EXTRA_OECMAKE = " -DBUILD_UNITTEST=ON -DMINKIDLC_BIN_DIR=${STAGING_BINDIR_NATIVE}"
do_install:append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/qteesupplicant.service ${D}${systemd_unitdir}/system/
}

FILES:${PN} += " ${bindir}/qtee_supplicant"
FILES:${PN}-bin += " \
    ${bindir}/gp_test_client \
    ${bindir}/smcinvoke_client \
"
FILES:${PN}-lib = "${libdir}/*.so.*"
FILES:${PN}-dev += "${libdir}/*.so"
