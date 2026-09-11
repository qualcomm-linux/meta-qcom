PLATFORM = "lemans"
PBT_BUILD_DATE = "260910"

require common.inc

SRC_URI[camxlib.sha256sum] = "0888f786d032ed6cf91e097d761d8e6f49e6a0a960bbf57111a981705e321985"
SRC_URI[camx.sha256sum] = "e8a6c72e65e2449558100d291e672956da83816dc865493876d1a7955f03b71f"
SRC_URI[chicdk.sha256sum] = "0ea775319b58b93e7ecf392b1663bed1dc81b056b09a66dc00a7da5fb5a077ad"
SRC_URI[camxcommon.sha256sum] = "6cde038f80e4005ad03e530f498fb025125682c39a095c4dd01927256776c51e"
SRC_URI[camxtest.sha256sum] = "d49ebcf823d9ba600659080661950a504ed7ec7319d718fd7db85e0440bd5ce0"

DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'opencl', 'virtual/libopencl1', '', d)}"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'virtual/egl virtual/libgles2', '', d)}"

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
RRECOMMENDS:${PN} += "${@bb.utils.contains('DISTRO_FEATURES', 'opencl', 'virtual-opencl-icd', '', d)}"

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
