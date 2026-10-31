PLATFORM = "shikra"
PBT_BUILD_DATE = "261026"

require common.inc

SRC_URI[camxlib.sha256sum] = "9ca3f5995151fa61c24b33389119bc94f3129160f5ce35695aa1b66f1b56a25c"
SRC_URI[camx.sha256sum] = "ff0735c80ea3e749cefff96f1aac33f98072d9573f32a95b9ee22176007ef4ff"
SRC_URI[chicdk.sha256sum] = "233d51877fc43750b86d6c04c17b6c24d4b7dcb03c4065f585f1a0e37a720ae7"
SRC_URI[camxcommon.sha256sum] = "876e55c3aa47e99a8dc88ddcbee71085f8ace6e8300199a34dbe436fe2f4c496"
SRC_URI[camxtest.sha256sum] = "c952066aafbbe95c548ec3d0fd75b9c0854373afaf0febcb5e2503d13bb84150"

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
