HOMEPAGE = "https://github.com/qualcomm/userspace-resource-manager"
SUMMARY = "Userspace daemon for dynamic System resource management"
DESCRIPTION = "Userspace Resource Manager(URM) is a lightweight userspace \
daemon that monitors system resources and enforces policies using \
Linux kernel interfaces such as cgroups and sysfs."

LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2998c54c288b081076c9af987bdf4838"

SRC_URI = "git://github.com/qualcomm/userspace-resource-manager.git;protocol=https;branch=main;tag=v${PV}"
SRCREV = "3722df2fcb7b7cf85f148de945e68efc8dea9319"

inherit cmake pkgconfig systemd

DEPENDS += "libyaml"

PACKAGECONFIG ??= "\
    classifier \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'state-detector systemd', '', d)} \
    tests \
"

PACKAGECONFIG[classifier] = "-DBUILD_CLASSIFIER=ON,-DBUILD_CLASSIFIER=OFF"
PACKAGECONFIG[state-detector] = "-DBUILD_STATE_DETECTOR=ON,-DBUILD_STATE_DETECTOR=OFF"
PACKAGECONFIG[systemd] = ",,systemd"
PACKAGECONFIG[tests] = "-DBUILD_TESTS=ON,-DBUILD_TESTS=OFF"

FILES:${PN}-dev += "${libdir}/urm/libUrmTestPlugin.so"
FILES:${PN} += "${sysconfdir}/urm/*"

SYSTEMD_SERVICE:${PN} = "urm.service"

PACKAGE_BEFORE_PN += "${PN}-tests"
FILES:${PN}-tests += " \
    ${sysconfdir}/urm/tests/* \
    ${bindir}/UrmComponentTests \
    ${bindir}/UrmIntegrationTests \
    ${libdir}/libRestuneTestUtils.so* \
    ${libdir}/urm/libUrmTestPlugin.so* \
"
