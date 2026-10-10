PLATFORM = "talos"
PBT_BUILD_DATE = "261010"

require common.inc

SRC_URI[camxlib.sha256sum] = "bdf9239c92fe58bd5859d6ea63a1e97b884b27ac3f6ac1acd8b636292762757f"
SRC_URI[camx.sha256sum] = "313595c46ca7415d7b18bc1220aad250c0e3d00641e0b67920b2ad3dd73aa946"
SRC_URI[chicdk.sha256sum] = "95e103a66a13e3d63387c80c1c6020381f8aaf52b295d514c7db3524b048f7db"
SRC_URI[camxcommon.sha256sum] = "879503be951bfc30e30275d6f28204a5a2f82814a28447ad7f0fabde78f6bc02"
SRC_URI[camxtest.sha256sum] = "1351c413606b5fd9a4b54e56a5c3ec9e483c1f6ba858e5b09abeaf93fa4745b9"

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
