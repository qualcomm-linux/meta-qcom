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

SRC_URI = "git://github.com/qualcomm/minkipc.git;branch=main;protocol=https"
SRCREV = "ca81bb6728465301abd85a004248f285f7810ab0"
PV = "0.0+git"

DEPENDS = "pkgconfig-native qcbor qcomtee minkidlc-native glib-2.0 systemd"

EXTRA_OECMAKE = " \
    -DBUILD_UNITTEST=ON \
    -DCFG_ENABLE_SYSTEMD=ON \
    -DCFG_USE_PKGCONFIG=ON \
    -DMINKIDLC_BIN_DIR=${STAGING_BINDIR_NATIVE} \
"

SYSTEMD_SERVICE:${PN} = "qteesupplicant.service"
