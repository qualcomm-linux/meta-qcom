SUMMARY = "Qualcomm Sensing hub library"
DESCRIPTION = "Userspace libraries that provides interface to interact with Qualcomm Sensing Hub"
HOMEPAGE = "https://github.com/qualcomm/sensinghub"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=9701d0ef17353f1d05d7b74c8712ebbd"

SRCREV = "5421270944d1fda9055d0efc593614f8b0ee51e0"

SRC_URI = "git://github.com/qualcomm/sensinghub.git;protocol=https;branch=main;tag=v${PV}"

DEPENDS = "protobuf protobuf-native glib-2.0 nanopb-runtime nanopb-generator-native qmi-framework fastrpc"

inherit autotools pkgconfig systemd

EXTRA_OECONF = " \
    --enable-versioned-lib \
    --with-qmi-oss \
	--with-systemd \
	--with-systemd-unitdir=${systemd_unitdir}/system \
    --with-qmi=${STAGING_INCDIR}/qmi_framework \
    --with-base-libdir=${nonarch_base_libdir} \
    --with-fastrpc-includes=${STAGING_INCDIR}/fastrpc \
"

FILES:${PN} += " \
    ${nonarch_base_libdir}/firmware/qcom/shikra/sensors \
    ${localstatedir}/lib/tqftpserv/sensors/registry \
    ${systemd_unitdir}/system/sensors.qti.service \
"

SYSTEMD_SERVICE:${PN} = "sensors.qti.service"
