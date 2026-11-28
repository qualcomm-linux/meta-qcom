PLATFORM = "lemans"
PBT_BUILD_DATE = "261127"

require common.inc

SRC_URI[camxlib.sha256sum] = "e2c875799308b220e3cd40495702380429ff482a85048b51b5b5180a6fc2e960"
SRC_URI[camx.sha256sum] = "3f0b1fda5e796af28dc5260ca51d6b66f536844488fe7a10c12763d2d09c8c57"
SRC_URI[chicdk.sha256sum] = "a7ae429951a8066ae9720f943374dc4bae790ca8f4c59c54735b805bc8812430"
SRC_URI[camxcommon.sha256sum] = "cef3e0fb2c03547dd42a5999829eda0dd7e470e59a5fa72c4cf18c8c2d8f7281"
SRC_URI[camxtest.sha256sum] = "69faab4260b4d214e0e52f01c808276cdfbec83eac1a059ba7b7c27b3a4d8248"

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
