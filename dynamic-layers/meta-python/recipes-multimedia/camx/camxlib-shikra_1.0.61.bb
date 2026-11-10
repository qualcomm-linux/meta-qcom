PLATFORM = "shikra"
PBT_BUILD_DATE = "261113"

require common.inc

SRC_URI[camxlib.sha256sum] = "8c3510b9748b3faa1978f0a4c9f932d5ce045999cf4cb79848ee0a77a7dc1f31"
SRC_URI[camx.sha256sum] = "c9d16ee40339457af53dbe399189d56c731fa9eb3d3e3e74a9ef1f270a07dd17"
SRC_URI[chicdk.sha256sum] = "db6fc2610d8f550ede4a6795038e48b353a6ee0294c76c28f2c4181ce63a62f4"
SRC_URI[camxcommon.sha256sum] = "94977bd1ea98e0b66ae45395ade8220896f1ee8e1cd0907eb5b98ed6f826a6a8"
SRC_URI[camxtest.sha256sum] = "42c27ed929bbb81c4eb5087f864217a78ffee3c9ff4fe3cd2847a9a3c07ebb15"

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
