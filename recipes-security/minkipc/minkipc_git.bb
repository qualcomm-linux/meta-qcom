HOMEPAGE = "https://github.com/qualcomm/minkipc.git"
SUMMARY = "Qualcomm MinkIPC applications and library"
SECTION = "libs"
PACKAGE_ARCH = "${MACHINE_ARCH}"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2b1366ebba1ebd9ae25ad19626bbca93"

inherit cmake pkgconfig update-rc.d systemd

SRC_URI += "\
    git://github.com/qualcomm/minkipc.git;branch=main;protocol=https \
    file://0001-minkipc-compilation-Fix-compilation-issue.patch \
    file://minkidlc \
    file://qteesupplicant.service \
"
SRCREV = "${AUTOREV}"
#PACKAGE_ARCH ?= "${MACHINE_ARCH}"
PV = "git${SRCREV}"
PN = "minkipc-git"

DEPENDS = " qcbor-git qcomtee-git"

FILESPATH += "${WORKSPACE}:"
EXTRA_OECMAKE = " -DBUILD_UNITTEST=ON -DMINKIDLC_BIN_DIR=${UNPACKDIR}"

INITSCRIPT_NAME = "qteesupplicant"
INITSCRIPT_PARAMS = "start 28 S ."

do_install:append() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/build/qtee_supplicant/qtee_supplicant ${D}${bindir}/
    install -m 0755 ${WORKDIR}/build/libminkteec/tests/gp_test_client/gp_test_client ${D}${bindir}/
    install -m 0755 ${WORKDIR}/build/libminkadaptor/tests/smcinvoke_client/smcinvoke_client ${D}${bindir}/

    install -d ${D}${libdir}
    install -m 755 ${WORKDIR}/build/libminkadaptor/*.so* ${D}${libdir}/
    install -m 755 ${WORKDIR}/build/libminkteec/*.so* ${D}${libdir}/
    install -m 755 ${WORKDIR}/build/listeners/libtaautoload/*.so* ${D}${libdir}/
    install -m 755 ${WORKDIR}/build/listeners/libtimeservice/*.so* ${D}${libdir}/
    install -m 755 ${WORKDIR}/build/listeners/libfsservice/fs/*.so* ${D}${libdir}/
    install -m 755 ${WORKDIR}/build/listeners/libfsservice/gpfs/*.so* ${D}${libdir}/

    install -d ${D}${includedir}
    install ${S}/libminkadaptor/include/*.h ${D}${includedir}
    install ${S}/libminkteec/include/*.h ${D}${includedir}/qcbor

    install -m 0644 ${UNPACKDIR}/qteesupplicant.service -D ${D}${systemd_unitdir}/system/qteesupplicant.service
}

FILES:${PN} += " ${libdir}/* \
    ${includedir}/* \
    ${bindir}/qtee_supplicant \
    ${bindir}/gp_test_client \
    ${bindir}/qtee_supplicant \
    ${systemd_unitdir}/system/* \
"

SYSTEMD_SERVICE:${PN} = "qteesupplicant.service"

INSANE_SKIP:${PN} = "dev-elf"
INSANE_SKIP:${PN}-dev = "dev-elf"
