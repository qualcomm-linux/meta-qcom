PLATFORM = "shikra"
PBT_BUILD_DATE = "261104"

require common.inc

SRC_URI[camxlib.sha256sum] = "7ad5498c802d13d2720c412302f2212a6652a1d31d697d52a1da12651dd8a174"
SRC_URI[camx.sha256sum] = "6c9bd69140feaac0bbe53c9286ad92672aa184771ab17a76cd0a569a8e634e74"
SRC_URI[chicdk.sha256sum] = "34f3b70961a3283d245b5e5a757e1183b066ec16aa9f9444326eb7759fa20e00"
SRC_URI[camxcommon.sha256sum] = "fe681e0025aa454f3413b2fd90d2d87d5d95e7d3ceb0e9d17db9a387f5435f42"
SRC_URI[camxtest.sha256sum] = "5c73163a99c4d7a519fed65b80976628477914c763b09224bfac9a359e45afa7"

DEPENDS += " \
    opencv \
    sensinghub \
    userspace-resource-manager \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opencl', 'virtual/libopencl1', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'virtual/egl virtual/libgles2', '', d)} \
"

do_install:append() {
    # Remove OpenCL-dependent libraries when opencl is not enabled.
    if ${@bb.utils.contains('DISTRO_FEATURES', 'opencl', 'false', 'true', d)}; then
        rm -f ${D}${libdir}/camx/${PLATFORM}/camera/components/com.qti.node.gpu*
    fi
}
