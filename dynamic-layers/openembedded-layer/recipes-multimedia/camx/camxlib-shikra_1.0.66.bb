PLATFORM = "shikra"
PBT_BUILD_DATE = "261127"

require common.inc

SRC_URI[camxlib.sha256sum] = "e758ca0d02efe0f69a7b13937cd5d447a18328eef7d9a7f0042c468ca274445c"
SRC_URI[camx.sha256sum] = "886bd3dfb583662988e5dad903697f2f9757e348f78979f433563b7653ea3b7a"
SRC_URI[chicdk.sha256sum] = "b56e613140369dd397f61876b7b98375f41722792ea75a5b8feb4c2ed09ed682"
SRC_URI[camxcommon.sha256sum] = "15ace725cf7239071e211099b58ce5ac3a936f07e6c727941a6ae57de2a20f28"
SRC_URI[camxtest.sha256sum] = "c0039cc06d5e79b5973537a8b31d3cf05324bbb3ba871188d92c5318834f09a7"

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
