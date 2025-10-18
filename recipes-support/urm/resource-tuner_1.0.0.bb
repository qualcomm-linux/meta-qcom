SUMMARY = "resource-tuner"
DESCRIPTION = "A lightweight userspace daemon for dynamic resource monitoring \
               and policy enforcement using kernel interfaces like cgroups and sysfs."

SRC_URI = "git://github.com/qualcomm/resource-tuner.git;protocol=https;branch=main"
SRCREV = "26ff78ec039431538c5fa0c6b6fd30d9883fe486"
S = "${UNPACKDIR}/${PN}-${PV}/Build"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://../LICENSE.txt;md5=2998c54c288b081076c9af987bdf4838"

DEPENDS += "libyaml"
DEPENDS += "systemd"

inherit cmake
inherit pkgconfig
inherit systemd

EXTRA_OECMAKE += "-DCMAKE_C_FLAGS='-fdebug-prefix-map=${WORKDIR}=/usr/src/debug/resource-tuner'"
EXTRA_OECMAKE += "-DCMAKE_CXX_FLAGS='-fdebug-prefix-map=${WORKDIR}=/usr/src/debug/resource-tuner'"

EXTRA_OECMAKE += "-DBUILD_SIGNALS=ON -DBUILD_CLI=ON -DBUILD_TESTS=ON -DBUILD_STATE_DETECTOR=ON"

EXTRA_OECMAKE += "-DCMAKE_VERBOSE_MAKEFILE=ON"
EXTRA_OEMAKE += "VERBOSE=1"

EXTRA_OEMAKE += "CFLAGS='-g -O0 --coverage -fprofile-arcs -ftest-coverage'"
EXTRA_OEMAKE += "LDFLAGS='-lgcov'"

EXTRA_OECMAKE += "-DCMAKE_INSTALL_SYSCONFDIR=/etc"

do_install:append() {
    install -m 0644 ${S}/../Services/resource-tuner.service -D ${D}${systemd_system_unitdir}/resource-tuner.service

    install -d ${D}/etc/resource-tuner/common
    install -m 0644 ${S}/../Core/Configs/*.yaml ${D}/etc/resource-tuner/common/
    install -m 0644 ${S}/../Signals/Configs/*.yaml ${D}/etc/resource-tuner/common/

    install -d ${D}/etc/resource-tuner/custom
    install -d ${D}/etc/resource-tuner/tests/Configs/ResourceSysFsNodes
    install -m 0644 ${S}/../Tests/Configs/*.yaml ${D}/etc/resource-tuner/custom/
    install -m 0644 ${S}/../Tests/Configs/ResourceSysFsNodes/*.txt ${D}/etc/resource-tuner/tests/Configs/ResourceSysFsNodes/
}

INSANE_SKIP:${PN} += "buildpaths dev-so dev"
FILES:${PN}-dev = ""
FILES:${PN} += "${libdir}"
FILES:${PN} += "/usr/include/**"
FILES:${PN} += "${systemd_system_unitdir}"
FILES:${PN} += "/etc/resource-tuner/**"
SOLIBS = ".so"
FILES_SOLIBSDEV = ""
SYSTEMD_AUTO_ENABLE:${PN} = "enable"
SYSTEMD_SERVICE:${PN} = "resource-tuner.service"
