PLATFORM = "shikra"
PBT_BUILD_DATE = "261113"

require common.inc

SRC_URI[camxlib.sha256sum] = "3f3cf746b98ec150e2f597e38fd0cc2c2fc05e3d5a3834f5dc82a0009da2664a"
SRC_URI[camx.sha256sum] = "08f8b83c980c281809ee9477ea522687e5d916eccba58e8cbe34d594d15c5d35"
SRC_URI[chicdk.sha256sum] = "4afe11187a1182d6ca07b793695ded3c4f42814aff65060ec5653c2b2851ab54"
SRC_URI[camxcommon.sha256sum] = "4bde76a5c5aa424f87e236f14ad125057199fd28f583ad47c9c1afae6c0ae937"
SRC_URI[camxtest.sha256sum] = "b62c77e889a0a9f5c83fc893965a5544a73a395fcd3a466494cedca245119d5d"

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
