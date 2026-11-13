PLATFORM = "talos"
PBT_BUILD_DATE = "261104"

require common.inc

SRC_URI[camxlib.sha256sum] = "90e972a5ba020d41ee647018063bb338851f2deb0c8586006d7cdf2663c35285"
SRC_URI[camx.sha256sum] = "2202f4077f92ae68a6ffc990bc46e183b6c1593cd4b847b5b6da2882c864294e"
SRC_URI[chicdk.sha256sum] = "f133181ed2c14dbef88b8411bf44d8dbf09f173ec0399c7835514d2feb3f945c"
SRC_URI[camxcommon.sha256sum] = "3d2176d3b88c25238c2fa3a0bd69d8b45e7e65c6e1c462b91ffb7bd2025a11ef"
SRC_URI[camxtest.sha256sum] = "ac0a00cf6efbfac4955965068c86ebe23a11a5827e14ae54ab92c70f97c61081"

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
