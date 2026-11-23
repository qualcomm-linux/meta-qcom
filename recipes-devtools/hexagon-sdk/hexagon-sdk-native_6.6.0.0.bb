SUMMARY = "Qualcomm Hexagon SDK (DSP cross-toolchain and headers)"
DESCRIPTION = "Native Hexagon SDK providing the hexagon-clang cross-toolchain, \
CMake helpers (hexagon_fun.cmake), and headers needed to build Hexagon DSP/HTP \
skel binaries as part of other recipes' builds. Build-time only -- nothing \
from this SDK ships to the target device."
HOMEPAGE = "https://docs.qualcomm.com/bundle/publicresource/topics/80-62010-1/introduction.html"

LICENSE = "CLOSED"

inherit native

# The zip file is quite large (~3 GB); increase tries/timeout like qairt-sdk does.
FETCHCMD_wget = "wget --tries=5 --timeout=1000"

SRC_URI = "https://softwarecenter.qualcomm.com/api/download/software/sdks/Hexagon_SDK/Linux/Debian/${PV}/Hexagon_SDK_Linux.zip"
SRC_URI[sha256sum] = "db9148d92f75a19faf425c8eda9e4bddcd9967e44aaf7d4218e04e6dee85b640"

S = "${UNPACKDIR}/Hexagon_SDK/${PV}"

COMPATIBLE_HOST = "(i.86|x86_64).*-linux"

# Stages the prebuilt qaic binary for this host OS into ipc/fastrpc/qaic/bin/,
# same as the SDK's own setup_sdk_env.source does; ggml-hexagon's build invokes
# it from that fixed path.
do_compile() {
    oe_runmake -C ${S}/ipc/fastrpc/qaic clean
    oe_runmake -C ${S}/ipc/fastrpc/qaic
}

do_install() {
    install -d ${D}${datadir}/hexagon-sdk
    cp -r ${S}/. ${D}${datadir}/hexagon-sdk/
}

# Ships a prebuilt proprietary toolchain plus Hexagon-ISA object files
# (crt*.o, debugmon.o, ...) that the host's strip/QA tooling can't parse -- skip it.
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_SYSROOT_STRIP = "1"
INSANE_SKIP:${PN} += "already-stripped arch ldflags"
