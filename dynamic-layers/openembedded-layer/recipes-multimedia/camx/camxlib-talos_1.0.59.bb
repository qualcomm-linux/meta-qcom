PLATFORM = "talos"
PBT_BUILD_DATE = "261104"

require common.inc

SRC_URI[camxlib.sha256sum] = "04f47c17d6ab3334201d87d907cf74cdfabfeda395a9cca47a1cfb13936557f5"
SRC_URI[camx.sha256sum] = "2c8f92aa1e9f53e9202ac35f56c609eec9953f8774188cd34ee2747cee0d47b7"
SRC_URI[chicdk.sha256sum] = "b3913cdc4a57da5f99d54c4c39c960ef0d8999c3f27b4a38bc556d21847de3f2"
SRC_URI[camxcommon.sha256sum] = "617c7c3344d58d5481adfc8a0549cc01702871a1c748f34bf1efb04a2dcf8f32"
SRC_URI[camxtest.sha256sum] = "60e785228dd0817feb3dd9c1962b8dbd6e6b4ed7e1f3f80d665f323e22b18d18"

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
