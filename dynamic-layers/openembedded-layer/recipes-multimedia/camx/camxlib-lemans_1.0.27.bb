PLATFORM = "lemans"
PBT_BUILD_DATE = "260616"

require common.inc

SRC_URI[camxlib.sha256sum] = "97d494798573681192147acdd87411dbe0413157d820ddaec81d51a9a2c6dadf"
SRC_URI[camx.sha256sum] = "b84903c48932462e4373934c7c10d311b8ad80f5ff822375de3008a3553245fd"
SRC_URI[chicdk.sha256sum] = "e563a96bc45f4685d0b7514400a1a81d07cb1b48c3dc2bef19438a48f2d09986"
SRC_URI[camxcommon.sha256sum] = "91d79a5530f926571e6921bd38b8eb22d7b223867cd2d375d28441e2282c88c1"

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
    install -d ${D}${datadir}/qcom/qcs8300/Qualcomm/QCS8300-RIDE/dsp/cdsp
    ln -sr ${D}${datadir}/qcom/sa8775p/Qualcomm/SA8775P-RIDE/dsp/cdsp/libbitml_nsp_73nb_skel.so \
        ${D}${datadir}/qcom/qcs8300/Qualcomm/QCS8300-RIDE/dsp/cdsp/libbitml_nsp_73nb_skel.so

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
    ${bindir}/nhx.sh \
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
