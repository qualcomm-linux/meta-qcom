HOMEPAGE = "https://github.com/qualcomm/fastrpc"
SUMMARY = "Qualcomm FastRPC applications and library"
SECTION = "devel"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=b67986b6880754696d418dbaa2cf51d1"
DEPENDS = "libbsd libyaml"

SRCREV = "3ac74f68dfcc5aa3a9524d1700a4c0d6a92316a8"
SRC_URI = "\
    git://github.com/qualcomm/fastrpc.git;branch=main;protocol=https;tag=v${PV} \
    file://run-ptest \
"

inherit autotools systemd ptest pkgconfig

EXTRA_OECONF += "--with-systemdsystemunitdir=${systemd_system_unitdir}"

SYSTEMD_SERVICE:${PN} = " \
    adsprpcd.service \
    adsprpcd_audiopd.service \
    cdsprpcd.service \
    cdsp1rpcd.service \
    gdsp0rpcd.service \
    gdsp1rpcd.service \
    sdsprpcd.service \
"

do_install:append() {
    install -d ${D}${datadir}/qcom/

    sed -i -e 's:/usr/bin/:${bindir}/:g' \
        ${D}${systemd_system_unitdir}/*.service
}

FILES:${PN} += " \
    ${libdir}/rfsa \
    ${libdir}/libadsp_default_listener.so \
    ${libdir}/libcdsp_default_listener.so \
    ${libdir}/libsdsp_default_listener.so \
    ${libdir}/libadsprpc.so \
    ${libdir}/libcdsprpc.so \
    ${libdir}/libsdsprpc.so \
    ${datadir}/qcom/ \
"

FILES:${PN}-dev:remove = "${FILES_SOLIBSDEV}"

RDEPENDS:${PN}-ptest += "${PN}-tests"

# We need to include lib*dsprpc.so into fastrpc for compatibility with Hexagon SDK
INSANE_SKIP:${PN} = "dev-so"

PACKAGE_BEFORE_PN += "${PN}-tests"

FILES:${PN}-tests += " \
    ${bindir}/fastrpc_test \
    ${libdir}/fastrpc_test/*.so \
    ${datadir}/fastrpc_test \
"

# Tests specific packages are including prebuilt test libraries
INSANE_SKIP:${PN}-tests += "arch libdir ldflags"

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"
