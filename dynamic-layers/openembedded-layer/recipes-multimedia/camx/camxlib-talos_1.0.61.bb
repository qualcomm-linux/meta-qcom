PLATFORM = "talos"
PBT_BUILD_DATE = "261113"

require common.inc

SRC_URI[camxlib.sha256sum] = "080a6fbd5fd440340e9135e8280e47a3ca80e68025412c04e3e40e7b8f679ca1"
SRC_URI[camx.sha256sum] = "7ae1bff9ad2840c528eebadfb3c8acbb9a1b12fdaf30a75f6eda651d78d69629"
SRC_URI[chicdk.sha256sum] = "aa90b59a5c0702c95dd4cc7f33cd14393b3767341ad1ed7aef740e49f979cd2c"
SRC_URI[camxcommon.sha256sum] = "890c7ac30af9be5d4139f40e8121bacc91ca965f89e74920a2cbed2761528271"
SRC_URI[camxtest.sha256sum] = "1331316dbf91044ccb5a6da8e8d94a5c7440039bfcdea5e44476842f645dc2fa"

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
