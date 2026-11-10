SUMMARY = "Qualcomm Sensing hub library"
DESCRIPTION = "Userspace libraries that provides interface to interact with Qualcomm Sensing Hub"
HOMEPAGE = "https://github.com/qualcomm/sensinghub"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=9701d0ef17353f1d05d7b74c8712ebbd"

SRCREV = "3091b6a5445c0caed820e5018ece39e4f00466ab"

SRC_URI = "git://github.com/qualcomm/sensinghub.git;protocol=https;branch=main;tag=v${PV}"

DEPENDS = "protobuf protobuf-native glib-2.0 nanopb-runtime nanopb-generator-native qmi-framework fastrpc"

inherit autotools pkgconfig systemd

EXTRA_OECONF = " \
    --enable-versioned-lib \
    --with-qmi-oss \
	--with-systemd-unitdir=${systemd_system_unitdir} \
    --with-qmi=${STAGING_INCDIR}/qmi_framework \
    --with-fastrpc-includes=${STAGING_INCDIR}/fastrpc \
"

SYSTEMD_SERVICE:${PN} = "sensors.qti.service"

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"
