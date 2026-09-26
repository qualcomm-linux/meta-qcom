PLATFORM = "shikra"
PBT_BUILD_DATE = "260923.1"

require common.inc

SRC_URI[camxlib.sha256sum] = "76779bcb6fb15031c21cbbbcd450672b9653541e932fe21175760fede869d836"
SRC_URI[camx.sha256sum] = "732e17ed2f584921e7fb27c2650535bc74e53ab560868b3cbc140988e0c0226d"
SRC_URI[chicdk.sha256sum] = "692419dc326c5c3aebe14691a077ae3fd9fcc9c3c4e208ebb0a4722cd8431555"
SRC_URI[camxcommon.sha256sum] = "8e13a8ecc860893c2dafb563d2a4cff6fc5f1312b168f997e487cd25e5ed5ffb"
SRC_URI[camxtest.sha256sum] = "c7a458508ce1655d68e448996560ed9449e7c019ab6253a97889a13671a7e1d6"

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
