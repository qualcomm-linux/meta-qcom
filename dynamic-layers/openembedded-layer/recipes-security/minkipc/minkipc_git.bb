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

SRC_URI = "git://github.com/qualcomm/minkipc.git;branch=main;protocol=https"
SRCREV = "337b2e0b86b4ad9ab76d5f18b02bac08c14b10a6"
PV = "0.0+git"

DEPENDS += "qcbor qcomtee mink-idl-compiler-native glib-2.0"

EXTRA_OECMAKE = " \
    -DMINKIDLC_BIN_DIR=${STAGING_BINDIR_NATIVE} \
    -DSYSTEMD_UNIT_DIR=${systemd_unitdir}/system \
    -DUDEV_DIR=${nonarch_libdir}/udev/rules.d \
"

PACKAGE_BEFORE_PN += "${PN}-qteesupplicant"

SYSTEMD_SERVICE:${PN}-qteesupplicant = "qteesupplicant.service"
FILES:${PN}-qteesupplicant = "${bindir}/qtee_supplicant \
                              ${systemd_unitdir}/system/qteesupplicant.service \
                              ${nonarch_libdir}/udev/rules.d/99-qcomtee-udev.rules \
"

# Currently, this recipe only builds and installs for ARMv8 (aarch64) machine.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"
