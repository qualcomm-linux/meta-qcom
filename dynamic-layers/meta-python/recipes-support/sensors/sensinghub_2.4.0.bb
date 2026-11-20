SUMMARY = "Qualcomm Sensing hub library"
DESCRIPTION = "Userspace libraries that provides interface to interact with Qualcomm Sensing Hub"
HOMEPAGE = "https://github.com/qualcomm/sensinghub"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=9701d0ef17353f1d05d7b74c8712ebbd"

SRCREV = "6dccf9b815fc95e842563a88c1515d5f6dadc9da"

SRC_URI = "git://github.com/qualcomm/sensinghub.git;protocol=https;branch=main;tag=v${PV}"

DEPENDS = "protobuf protobuf-native glib-2.0 nanopb-runtime nanopb-generator-native qmi-framework fastrpc"

inherit autotools pkgconfig systemd

EXTRA_OECONF = " \
    --enable-versioned-lib \
    --with-qmi-oss \
	--with-systemd \
	--with-systemd-unitdir=${systemd_unitdir}/system \
    --with-qmi=${STAGING_INCDIR}/qmi_framework \
    --with-fastrpc-includes=${STAGING_INCDIR}/fastrpc \
"

SYSTEMD_SERVICE:${PN} = "sensors.qti.service"