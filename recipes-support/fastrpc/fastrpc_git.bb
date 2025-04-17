HOMEPAGE = "https://github.com/quic/fastrpc"
SUMMARY = "Qualcomm FastRPC applications and library"
SECTION = "devel"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://src/fastrpc_apps_user.c;beginline=1;endline=2;md5=a5b0aa365758f6917baf7a2d81f5d29e"

SRCREV = "abbd72146ab3f44a9421363d5d70eab4b5ba4f99"
SRC_URI = "\
    git://github.com/quic/fastrpc.git;branch=main;protocol=https \
    file://adsprpcd.service \
    file://cdsprpcd.service \
    file://sdsprpcd.service \
    file://usr-lib-rfsa.service \
    file://mount-dsp.sh \
"

PV = "0.1+"

S = "${WORKDIR}/git"

inherit autotools systemd

PACKAGES += "${PN}-systemd"
RRECOMMENDS:${PN} += "${PN}-systemd"

SYSTEMD_PACKAGES = "${PN} ${PN}-systemd"

SYSTEMD_SERVICE:${PN} = "usr-lib-rfsa.service"

SYSTEMD_SERVICE:${PN}-systemd = "adsprpcd.service cdsprpcd.service sdsprpcd.service"
SYSTEMD_AUTO_ENABLE:${PN}-systemd = "enable"

HEADER_FILES = "\
    adsp_default_listener1.h AEEatomic.h AEEQList.h AEEstd.h AEEStdDef.h AEEStdErr.h  AEEVaList.h \
    dspqueue.h HAP_debug.h HAP_farf.h HAP_pls.h remote.h remote64.h rpcmem.h verify.h \
"

do_install:append() {
    install -d ${D}${libdir}/rfsa

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/usr-lib-rfsa.service ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/adsprpcd.service ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/cdsprpcd.service ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/sdsprpcd.service ${D}${systemd_unitdir}/system

    install -d ${D}${sbindir}
    install -m 0755 ${UNPACKDIR}/mount-dsp.sh ${D}${sbindir}

    install -d ${D}${includedir}/
    for header in ${HEADER_FILES}; do
        install -m 0644 ${S}/inc/${header} ${D}${includedir}/
    done
}

FILES:${PN} += " \
    ${libdir}/rfsa \
    ${libdir}/libadsp_default_listener.so \
    ${libdir}/libcdsp_default_listener.so \
    ${libdir}/libsdsp_default_listener.so \
    ${libdir}/libadsprpc.so \
    ${libdir}/libcdsprpc.so \
    ${libdir}/libsdsprpc.so \
"
FILES:${PN}-dev:append = "${includedir}/*.h"

FILES:${PN}-dev:remove = "${FILES_SOLIBSDEV}"

# We need to include lib*dsprpc.so into fastrpc for compatibility with Hexagon SDK
ERROR_QA:remove = "dev-so"
