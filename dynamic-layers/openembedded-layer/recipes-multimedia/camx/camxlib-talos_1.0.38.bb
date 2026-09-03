PLATFORM = "talos"
PBT_BUILD_DATE = "260825"

require common.inc

SRC_URI[camxlib.sha256sum] = "bad5bb9ca3a99dda9665d0ddbe2f5ade10fb291ef7b954f279b378da9c56c63d"
SRC_URI[camx.sha256sum] = "f2e356e96b84bf77a764b16fcb1d947854ea6feec8f04682bb3202b165aaa76d"
SRC_URI[chicdk.sha256sum] = "4e6942bd426bb0dd9d5719674740c97f77e8ba4a17255e48a5ae9b554118082a"
SRC_URI[camxcommon.sha256sum] = "3400a71accf286d0539d05f7b55e9f7269a77c31c833e83742163d613be45ed3"
SRC_URI[camxtest.sha256sum] = "af51977b0cbd79e7d50088ac64ae643b98e1d7c11b0a419bd3319c88e185d258"

DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'opencl', 'virtual/libopencl1', '', d)}"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'virtual/egl virtual/libgles2', '', d)}"

do_install:append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'opengl opencl', 'false', 'true', d)}; then
        rm -f ${D}${libdir}/camx/${PLATFORM}/camera/components/libiwarp*
        rm -f ${D}${libdir}/camx/${PLATFORM}/camera/components/libhidrx*
    fi
}
