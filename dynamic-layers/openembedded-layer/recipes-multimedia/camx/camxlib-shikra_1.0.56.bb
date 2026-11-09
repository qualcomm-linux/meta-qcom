PLATFORM = "shikra"
PBT_BUILD_DATE = "261026"

require common.inc

SRC_URI[camxlib.sha256sum] = "0a80cdac6fcc71380f9db87e273e3123760913d8be8f941ad97e6c01eb054489"
SRC_URI[camx.sha256sum] = "ffd928199e48ea90843c2671160f193beabfcf2400a38a89f1bfbbc3478d1921"
SRC_URI[chicdk.sha256sum] = "4d5273c9558f5b5e0b65d5a1aac8a9eba4050f3c0a136bbfee99197882dfcb00"
SRC_URI[camxcommon.sha256sum] = "8ab3ffeb241a4e4aa86fa7aeeb4f6f5c18d14639e5545dd861b0344e31cfa02d"
SRC_URI[camxtest.sha256sum] = "416dfd37ac4184284388b0e7bd292a3e323ba02eefbad124d65446bfe2f3fc18"

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
