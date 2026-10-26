PLATFORM = "talos"
PBT_BUILD_DATE = "261026"

require common.inc

SRC_URI[camxlib.sha256sum] = "092c5ec88495c61c4afbe585f1e60b0d06e965471a5112ff403fa0d7b9088174"
SRC_URI[camx.sha256sum] = "994af723115a6ee9d0da546fc51cdc95caa4a3ef5951056eaa0ba40413e3d768"
SRC_URI[chicdk.sha256sum] = "19d9986f0319340721676d871034e89814c989953f9ba16c1c4e8dfa4dcc89e4"
SRC_URI[camxcommon.sha256sum] = "013fa88456820592e4916ccdd9273fd5fadae1173ba514040c0ba12c2e9fe064"
SRC_URI[camxtest.sha256sum] = "704554b0cee426bca79e5e5c709c65bd201859139d441e1460c5e096d67a7663"

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
