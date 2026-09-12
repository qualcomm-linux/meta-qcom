PLATFORM = "talos"
PBT_BUILD_DATE = "260910"

require common.inc

SRC_URI[camxlib.sha256sum] = "3f6b90a805a2c16d155cd8e19779d93a1a579e38793ef17841a37623acf0bd2d"
SRC_URI[camx.sha256sum] = "9a9b87a3117ac0ad6d38062e4d3f2772c8675399bb882216dc0fe25860b58777"
SRC_URI[chicdk.sha256sum] = "8c8f13e72bcece296a751b07d8a119a804a9b3769432672d95084b33bec3c384"
SRC_URI[camxcommon.sha256sum] = "adfdcba5f52d42b70223a9bb9d32b9ed497d35210657c5c52701b75a47f23288"
SRC_URI[camxtest.sha256sum] = "e038ed09189db4ee7bb593a586c26965cd7edb30493bf1d2c364377bcd165105"

DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'opencl', 'virtual/libopencl1', '', d)}"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'virtual/egl virtual/libgles2', '', d)}"

do_install:append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'opengl opencl', 'false', 'true', d)}; then
        rm -f ${D}${libdir}/camx/${PLATFORM}/camera/components/libiwarp*
        rm -f ${D}${libdir}/camx/${PLATFORM}/camera/components/libhidrx*
    fi
}
