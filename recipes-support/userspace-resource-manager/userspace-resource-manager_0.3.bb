HOMEPAGE = "https://github.com/qualcomm/userspace-resource-manager"
SUMMARY = "userspace daemon for dynamic System resource management"
DESCRIPTION = "Userspace Resource Manager(URM) is a lightweight userspace \
daemon that monitors system resources and enforces policies using \
Linux kernel interfaces such as cgroups and sysfs."

LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2998c54c288b081076c9af987bdf4838"

SRC_URI = "\
    git://github.com/qualcomm/userspace-resource-manager.git;protocol=https;branch=main;tag=v${PV} \
    file://urm.service \
"
SRCREV = "d6d559bf824ef57888a58bf6a3311dfa488b537e"

inherit cmake pkgconfig systemd

DEPENDS += "\
    libyaml \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)} \
"

PACKAGECONFIG ??= "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'state-detector', '', d)} \
    tests \
"

PACKAGECONFIG[classifier] = "-DBUILD_CLASSIFIER=ON,-DBUILD_CLASSIFIER=OFF"
PACKAGECONFIG[state-detector] = "-DBUILD_STATE_DETECTOR=ON,-DBUILD_STATE_DETECTOR=OFF"
PACKAGECONFIG[tests] = "-DBUILD_TESTS=ON,-DBUILD_TESTS=OFF"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/urm.service ${D}${systemd_unitdir}/system
}

SYSTEMD_SERVICE:${PN} = "urm.service"
FILES:${PN} += "${sysconfdir}/resource-tuner/*"

PACKAGE_BEFORE_PN += "${PN}-tests"
FILES:${PN}-tests += " \
    ${sysconfdir}/resource-tuner/tests/* \
    ${bindir}/RestuneComponentTests \
    ${bindir}/RestuneIntegrationTests \
    ${libdir}/libRestuneTestUtils.so* \
    ${libdir}/libRestunePlugin.so* \
"
