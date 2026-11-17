PLATFORM = "lemans"
PBT_BUILD_DATE = "261104"

require common.inc

SRC_URI[camxlib.sha256sum] = "4295615208fa8f7d2f155c938d8bc87fe58551813d009fd6c9abd4293c7a4015"
SRC_URI[camx.sha256sum] = "66e3c39087cf879698578ac8b8a879e3636219ed2d467fe59ad14f14acc3a3b7"
SRC_URI[chicdk.sha256sum] = "4683478e8fcec589fa718d9b685a5d47b7a1e44026d6ecbb1beac97325bca11a"
SRC_URI[camxcommon.sha256sum] = "725a955e88e2dd90002819f982d6d1a9fa76266b4eb180b932b50a4ec1d35493"
SRC_URI[camxtest.sha256sum] = "0b75bd23e981da16a5442e24e7e7acf7a82f147a4e0cb9d578a935ce32b75fcc"

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
