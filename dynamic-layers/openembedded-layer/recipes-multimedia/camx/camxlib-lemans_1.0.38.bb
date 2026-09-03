PLATFORM = "lemans"
PBT_BUILD_DATE = "260825"

require common.inc

SRC_URI[camxlib.sha256sum] = "d786afdae167643ad5feaad745ed14b741001060cf41e6174fe4f37cee8077f5"
SRC_URI[camx.sha256sum] = "fd57a206aa1ae4dd6ac95f71ceb2812f995b5ebbf32322fe693186c7359b9770"
SRC_URI[chicdk.sha256sum] = "31b7582c5c8f2f8a3412e9b99f3b689afda6b53efd1504d8864097ab73fd9c31"
SRC_URI[camxcommon.sha256sum] = "e2a8645a8ae22182cd62e612ecbb1d098b7fcee4e75e42778b79a88b2587bcfb"
SRC_URI[camxtest.sha256sum] = "628a94a198b5269812169e3bfbedf285b68cd3190e7102d9f076654fef3b9915"

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
