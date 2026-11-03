PLATFORM = "shikra"
PBT_BUILD_DATE = "261010"

require common.inc

SRC_URI[camxlib.sha256sum] = "f7f5e7c78918b1d97d83ce349480a57a77383e7cf710b8ef8d8a4f4bc215d68f"
SRC_URI[camx.sha256sum] = "1903643f6f2391eeb2e9fa8c6209dc4477c30d238452a3182fc602e4b30c8057"
SRC_URI[chicdk.sha256sum] = "082c6d3a34bf11d9461e4630f677bfdb7d9f0296b4dd731ecc5c1684d65ec419"
SRC_URI[camxcommon.sha256sum] = "8f88b42706875274f955d7183b06199783d195f2f4fed43f7c9b4ccb626b9754"
SRC_URI[camxtest.sha256sum] = "3b9dcc4fb12fa88915cd9785d278c603dcdc08225b02fd098c09a28973047e94"

DEPENDS += " \
    sensinghub \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opencl', 'virtual/libopencl1', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'virtual/egl virtual/libgles2', '', d)} \
"

do_install:append() {
    # Remove OpenCL-dependent libraries when opencl is not enabled.
    if ${@bb.utils.contains('DISTRO_FEATURES', 'opencl', 'false', 'true', d)}; then
        rm -f ${D}${libdir}/camx/${PLATFORM}/camera/components/com.qti.node.gpu*
    fi
}
