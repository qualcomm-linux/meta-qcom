PLATFORM = "talos"
PBT_BUILD_DATE = "261026"

require common.inc

SRC_URI[camxlib.sha256sum] = "bf3b9e24ec3d57597230932a852713f7ea1cbb6dd0c8c1f069267dee8953e804"
SRC_URI[camx.sha256sum] = "d54866b12ccb1c7f099ae8c9735673251988664f6698814564a404e9b06bc7a4"
SRC_URI[chicdk.sha256sum] = "e967bca51b497a740cc423139b95f9cd0337e5b0d1ac0f289b3bc53f3baeaed1"
SRC_URI[camxcommon.sha256sum] = "2e581b1cf9f584e8cec0a5976b45ea21844ea02993ce7a8207c0fca4ff416657"
SRC_URI[camxtest.sha256sum] = "f70b1a04b64e2ab64f9032bad25047aad6f12040b2e411b078184f10c710374d"

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
