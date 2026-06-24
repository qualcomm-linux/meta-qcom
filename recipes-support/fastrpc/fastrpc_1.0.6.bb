HOMEPAGE = "https://github.com/qualcomm/fastrpc"
SUMMARY = "Qualcomm FastRPC applications and library"
SECTION = "devel"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=b67986b6880754696d418dbaa2cf51d1"
DEPENDS = "libbsd libyaml"

SRCREV = "5f0b6a33a19f3f7f0c06709846752a2caf64e413" 
SRC_URI = "\
    git://github.com/qualcomm/fastrpc.git;branch=main;protocol=https;tag=v${PV} \
    file://run-ptest \
"

inherit autotools systemd ptest pkgconfig

EXTRA_OECONF += "\
    --with-systemdsystemunitdir=${systemd_system_unitdir} \
    --with-udevrulesdir=${nonarch_base_libdir}/udev/rules.d \
    --with-sysusersdir=${nonarch_libdir}/sysusers.d \
"

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
    ${nonarch_base_libdir}/udev/rules.d/60-fastrpc.rules \
    ${nonarch_libdir}/sysusers.d/fastrpc.conf \
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
