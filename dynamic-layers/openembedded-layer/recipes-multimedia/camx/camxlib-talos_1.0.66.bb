PLATFORM = "talos"
PBT_BUILD_DATE = "261127"

require common.inc

SRC_URI[camxlib.sha256sum] = "e4fa41406bc2f0ec667cb5abf1ccbb42abed1f408d7ab0c65a8b20ba80a6d9eb"
SRC_URI[camx.sha256sum] = "015996f3ccf2f83e4dfb99b888536b98c9e8efb8e5b4f8b3773b937b1204f64f"
SRC_URI[chicdk.sha256sum] = "2644a726529f9a3f2999e0703d2f4cab0d6be77d74ab09101860e84e8363f510"
SRC_URI[camxcommon.sha256sum] = "dcd5eab517bf655b3a67d2b52970f4c9f082caf91e8004605fb20af4cbd92717"
SRC_URI[camxtest.sha256sum] = "ad426da30e24efc9c0a27ca767c19921ee0d8089ad3c0142d81dcb2c643b7607"

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
