PLATFORM = "talos"
PBT_BUILD_DATE = "261113"

require common.inc

SRC_URI[camxlib.sha256sum] = "0eee1db49b470504b073e9dec73c8c031be86855e1896886040dabe6f7ef0476"
SRC_URI[camx.sha256sum] = "82fe0c30d6274f08ada81ec00f1af37eab306a6e1f3085341c40ed0ee21c7e73"
SRC_URI[chicdk.sha256sum] = "5ffb78bbf09aaec59e09d9119937d526a791a9260d417ddef47a77ec9db9b565"
SRC_URI[camxcommon.sha256sum] = "934d76dcc23b8b03cfbda15d8336ed6bd4d2219f518b5062550e95122033f579"
SRC_URI[camxtest.sha256sum] = "63fb1a03ef7b4bbdf66937e63b5bb152d59cc1b38234288df29ad50f386d65fc"

DEPENDS += " \
    opencv \
    sensinghub \
    userspace-resource-manager \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opencl', 'virtual/libopencl1', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'virtual/egl virtual/libgles2', '', d)} \
"

do_install:append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'opengl opencl', 'false', 'true', d)}; then
        rm -f ${D}${libdir}/camx/${PLATFORM}/camera/components/libiwarp*
        rm -f ${D}${libdir}/camx/${PLATFORM}/camera/components/libhidrx*
    fi
}
