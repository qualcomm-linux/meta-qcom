HOMEPAGE = "https://github.com/qualcomm/resource-tuner"
SUMMARY = "Userspace daemon for dynamic System resource management"
DESCRIPTION = "Resource Tuner is a lightweight userspace daemon that monitors system \
resources and enforces policies using Linux kernel interfaces such as cgroups and sysfs."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2998c54c288b081076c9af987bdf4838"

SRC_URI = "git://github.com/qualcomm/resource-tuner.git;protocol=https;branch=main"
SRCREV = "681a19ba3f6731f3acf1e1022fcd94298eecff82"

inherit cmake pkgconfig systemd

DEPENDS += "libyaml systemd"

PACKAGECONFIG ??= "cli signals state-detector tests"
PACKAGECONFIG[cli] = "-DBUILD_CLI=ON,-DBUILD_CLI=OFF"
PACKAGECONFIG[signals] = "-DBUILD_SIGNALS=ON,-DBUILD_SIGNALS=OFF"
PACKAGECONFIG[state-detector] = "-DBUILD_STATE_DETECTOR=ON,-DBUILD_STATE_DETECTOR=OFF"
PACKAGECONFIG[tests] = "-DBUILD_TESTS=ON,-DBUILD_TESTS=OFF"

FILES:${PN} += "${sysconfdir}/resource-tuner/*"
SYSTEMD_SERVICE:${PN} = "resource-tuner.service"

PACKAGE_BEFORE_PN += "${PN}-tests"
FILES:${PN}-tests += "${sysconfdir}/resource-tuner/tests/* \
                      ${bindir}/RestuneComponentTests \
                      ${bindir}/RestuneIntegrationTests \
                      ${libdir}/libRestuneTestUtils.so* \
                      ${libdir}/libRestunePlugin.so* "
