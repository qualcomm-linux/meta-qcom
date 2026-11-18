PLATFORM = "lemans"
PBT_BUILD_DATE = "261113"

require common.inc

SRC_URI[camxlib.sha256sum] = "a25166c4eb4e0e8f2f0c78502a778997a052b2f83581eabe3b2bce2ffa3eb3b2"
SRC_URI[camx.sha256sum] = "f32f4ca35ed6539b9da1dc83fe8dc36cceae0f5e93edf45482f15d073d18dcd0"
SRC_URI[chicdk.sha256sum] = "2580ab15d68fab666f16d6d4e0571c07af817307cfbce1a6882e43fa8c6154f3"
SRC_URI[camxcommon.sha256sum] = "e5de2d564b353db1e8fe22705ce4d48f57217f3be9dc3c2933b9fbf1497758e6"
SRC_URI[camxtest.sha256sum] = "cff63c8485b4896e5aff2c463aec695f746c0ed743b9e9ba0c6f7cbc80901649"

DEPENDS += " \
    opencv \
    userspace-resource-manager \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opencl', 'virtual/libopencl1', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'virtual/egl virtual/libgles2', '', d)} \
"

do_install:append() {
    # Copy json only when /etc folder exists in ${S}
    if [ -d "${S}/etc" ]; then
        install -d ${D}${sysconfdir}/camera/test/NHX/
        cp -r ${S}/etc/camera/test/NHX/*.json ${D}${sysconfdir}/camera/test/NHX/
    fi
    # copy Deep Learning based binary
    cp -r ${S}/usr/share/camx ${D}${datadir}
    # copy skel file
    cp -r ${S}/usr/share/qcom ${D}${datadir}

    # Remove OpenCL-dependent libraries when opencl is not enabled.
    if ${@bb.utils.contains('DISTRO_FEATURES', 'opencl', 'false', 'true', d)}; then
        rm -f ${D}${libdir}/camx/${PLATFORM}/*.cl
        rm -f ${D}${libdir}/camx/${PLATFORM}/libmctf_cl_program.bin
        rm -f ${D}${libdir}/camx/${PLATFORM}/libmctfengine_stub*
    fi
}

RPROVIDES:${PN} = "camxlib-monaco"
PACKAGE_BEFORE_PN += "camx-nhx ${PN}-skel"
RDEPENDS:${PN} += "${PN}-skel"

FILES:camx-nhx = "\
    ${bindir}/camera-nhx \
    ${sysconfdir}/camera/test/NHX/ \
"
FILES:${PN}-skel = "\
    ${datadir}/camx \
    ${datadir}/qcom \
"
# OpenCL-related camx files
CAMX_OPENCL_FILES = " \
    ${libdir}/camx/${PLATFORM}/*.cl \
    ${libdir}/camx/${PLATFORM}/libmctf_cl_program.bin \
"
FILES:${PN} += "${@bb.utils.contains('DISTRO_FEATURES', 'opencl', '${CAMX_OPENCL_FILES}', '', d)}"

# Algo librarires are pre-compiled, pre-stripped.
# Skipping QA checks: 'already-stripped', 'arch', 'libdir' because:
# - Library files are Pre-stripped  (already-stripped)
# - skel binaries/library are not AArch64 (arch mismatch)      (arch)
# - Files are installed under /usr/share (non-libdir path) (libdir)
# - .so symlink is used for runtime DSP usage, not a dev artifact (dev-so)
INSANE_SKIP:${PN}-skel += " arch libdir already-stripped dev-so"

# Preserve ${PN}-skel naming to avoid ambiguity in package identification.
DEBIAN_NOAUTONAME:${PN}-skel = "1"
