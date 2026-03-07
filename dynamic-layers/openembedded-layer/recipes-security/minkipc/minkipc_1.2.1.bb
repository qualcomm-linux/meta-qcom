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

inherit cmake systemd pkgconfig

SRC_URI = "gitsm://github.com/qualcomm/minkipc.git;branch=main;protocol=https;tag=v${PV}"
SRCREV = "cca7562ccec486c4e7626f862424af0391641ca6"

DEPENDS += "qcbor qcomtee mink-idl-compiler-native glib-2.0"

EXTRA_OECMAKE = " \
    -DBUILD_UNITTEST=ON \
    -DMINKIDLC_BIN_DIR=${STAGING_BINDIR_NATIVE} \
    -DSYSTEMD_UNIT_DIR=${systemd_unitdir}/system \
    -DUDEV_DIR=${nonarch_libdir}/udev/rules.d \
    -DMINKIPC_LIBEXEC_DIR=${base_bindir} \
"

PACKAGE_BEFORE_PN += "${PN}-qteesupplicant"

SYSTEMD_PACKAGES = "${PN}-qteesupplicant"
SYSTEMD_SERVICE:${PN}-qteesupplicant = "qteesupplicant.service sfsconfig.service"

FILES:${PN}-qteesupplicant = "${bindir}/qtee_supplicant \
                              ${nonarch_libdir}/udev/rules.d/99-qcomtee-udev.rules \
                              ${base_bindir}/sfs_config \
"

RDEPENDS:${PN}-qteesupplicant = "${PN}"

