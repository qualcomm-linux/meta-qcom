PLATFORM = "shikra"
PBT_BUILD_DATE = "261104"

require common.inc

SRC_URI[camxlib.sha256sum] = "97ab7d63f0601148e83bf444ddff4d68ecdd9c17a09affb8fbfe176752385485"
SRC_URI[camx.sha256sum] = "993f1f962bc20a0b69d3c2b42eb64e11e4b32916616efb6334dbc2f6d5cede0c"
SRC_URI[chicdk.sha256sum] = "538b605fd025f7310133bb2384e24b64430316cd12f6bd592e75bd040b9ac0d9"
SRC_URI[camxcommon.sha256sum] = "f576b98a213911f091081780fc3b3030cb5641a7d2d99b80e4687c380421004e"
SRC_URI[camxtest.sha256sum] = "5e99bd3c8aba44c8125d568cb2f1063ef92822bf454b1e73657b24dcbd3cc1e7"

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
