HOMEPAGE = "https://github.com/qualcomm/resource-tuner"
SUMMARY = "Userspace daemon for dynamic System resource management"
DESCRIPTION = "Resource Tuner is a lightweight userspace daemon that monitors system \
resources and enforces policies using Linux kernel interfaces such as cgroups and sysfs."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2998c54c288b081076c9af987bdf4838"

SRC_URI = "\
    git://github.com/qualcomm/resource-tuner.git;protocol=https;branch=main;tag=v${PV} \
    file://resource-tuner.service \
"
SRCREV = "71a941f3f0490970b15edcdd8e9dc01baa1ab225"

inherit cmake pkgconfig systemd

DEPENDS += "\
    libyaml \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)} \
"

PACKAGECONFIG ??= "\
    cli \
    signals \
    tests \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'state-detector', '', d)} \
"

PACKAGECONFIG[cli] = "-DBUILD_CLI=ON,-DBUILD_CLI=OFF"
PACKAGECONFIG[signals] = "-DBUILD_SIGNALS=ON,-DBUILD_SIGNALS=OFF"
PACKAGECONFIG[state-detector] = "-DBUILD_STATE_DETECTOR=ON,-DBUILD_STATE_DETECTOR=OFF"
PACKAGECONFIG[tests] = "-DBUILD_TESTS=ON,-DBUILD_TESTS=OFF"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/resource-tuner.service ${D}${systemd_unitdir}/system
}

SYSTEMD_SERVICE:${PN} = "resource-tuner.service"
FILES:${PN} += "${sysconfdir}/resource-tuner/*"

PACKAGE_BEFORE_PN += "${PN}-tests"
FILES:${PN}-tests += "${sysconfdir}/resource-tuner/tests/* \
                      ${bindir}/RestuneComponentTests \
                      ${bindir}/RestuneIntegrationTests \
                      ${libdir}/libRestuneTestUtils.so* \
                      ${libdir}/libRestunePlugin.so* "
