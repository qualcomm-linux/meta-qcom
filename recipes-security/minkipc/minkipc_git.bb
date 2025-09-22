SUMMARY = "Qualcomm MinkIPC applications and library"
DESCRIPTION = " \
MINK ('Mink is Not a Kernel') is a capability-based security framework, \
which is a synchronous message passing facility based on the Object-Capability model, \
designed to facilitate secure communication between different domains. \
"
HOMEPAGE = "https://github.com/qualcomm/minkipc.git"
SECTION = "libs"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2b1366ebba1ebd9ae25ad19626bbca93"

inherit cmake pkgconfig update-rc.d systemd

SRC_URI += "\
    git://github.com/qualcomm/minkipc.git;branch=main;protocol=https \
    file://0001-minkipc-compilation-Fix-compilation-issue.patch \
    file://qteesupplicant.service \
"
SRCREV = "cd0355987595a0619e7689aec50dc4c91153b169"
PV = "1.0+git"
DEPENDS = " qcbor qcomtee minkidlc-native"

EXTRA_OECMAKE = " -DBUILD_UNITTEST=ON -DMINKIDLC_BIN_DIR=${STAGING_BINDIR_NATIVE}"
INITSCRIPT_NAME = "qteesupplicant"
SYSTEMD_SERVICE:${PN} = "qteesupplicant.service"

do_install:append() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/build/qtee_supplicant/qtee_supplicant ${D}${bindir}/
    install -m 0755 ${WORKDIR}/build/libminkteec/tests/gp_test_client/gp_test_client ${D}${bindir}/
    install -m 0755 ${WORKDIR}/build/libminkadaptor/tests/smcinvoke_client/smcinvoke_client ${D}${bindir}/

    install -m 0644 ${UNPACKDIR}/qteesupplicant.service -D ${D}${systemd_unitdir}/system/qteesupplicant.service
}

FILES:${PN} += " \
    ${bindir} \
    ${libdir} \
    ${systemd_unitdir}/system/qteesupplicant.service \
"

# Fix QA Issue: -dev package contains non-symlink *.so
INSANE_SKIP:${PN}-dev = "dev-elf"