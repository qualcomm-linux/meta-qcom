HOMEPAGE = "https://github.com/qualcomm/fastrpc"
SUMMARY = "Qualcomm FastRPC applications and library"
SECTION = "devel"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=b67986b6880754696d418dbaa2cf51d1"

SRCREV = "27235935b19fad234cb561b1e9d4984a51ee9e9f"
SRC_URI = "\
    git://github.com/qualcomm/fastrpc.git;branch=main;protocol=https \
    file://adsprpcd.service \
    file://cdsprpcd.service \
    file://sdsprpcd.service \
    file://guess-dsp.sh \
    file://run-ptest \
"

inherit autotools systemd ptest

PACKAGES += "${PN}-systemd"
RRECOMMENDS:${PN} += "${PN}-systemd"

SYSTEMD_PACKAGES = "${PN} ${PN}-systemd"

SYSTEMD_SERVICE:${PN}-systemd = "adsprpcd.service cdsprpcd.service sdsprpcd.service"
SYSTEMD_AUTO_ENABLE:${PN}-systemd = "disable"

do_install:append() {
    install -d ${D}${libdir}/rfsa

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/adsprpcd.service ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/cdsprpcd.service ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/sdsprpcd.service ${D}${systemd_unitdir}/system

    install -d ${D}${sbindir}
    install -m 0755 ${UNPACKDIR}/guess-dsp.sh ${D}${sbindir}

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
