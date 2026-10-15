PLATFORM = "talos"
PBT_BUILD_DATE = "260923.1"

require common.inc

SRC_URI[camxlib.sha256sum] = "4b2918d43ba1583c9e59c2b311841743608f506406007fba5f1491dc3ebb5261"
SRC_URI[camx.sha256sum] = "66ce826be59bddd9d4f143a731d1cc399d7b76a8ee06b53c2f0afcda46eb894a"
SRC_URI[chicdk.sha256sum] = "b3742fe620108f4a11ee493089b2bd23b27a35be105b4b50a665fa8a753e219d"
SRC_URI[camxcommon.sha256sum] = "013b7a8d87d74517b1b9b6f80c8dee45dd3bf24f4ab399014ce935a1165f3dd1"
SRC_URI[camxtest.sha256sum] = "e3943beebc1064271c24235a9b8044c13293b9a8ed5c94082a3ee8d31bfe4023"

DEPENDS += " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opencl', 'virtual/libopencl1', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'virtual/egl virtual/libgles2', '', d)} \
"

do_install:append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'opengl opencl', 'false', 'true', d)}; then
        rm -f ${D}${libdir}/camx/${PLATFORM}/camera/components/libiwarp*
        rm -f ${D}${libdir}/camx/${PLATFORM}/camera/components/libhidrx*
    fi
}
