PLATFORM = "lemans"
PBT_BUILD_DATE = "260814"

require common.inc

SRC_URI[camxlib.sha256sum] = "1ba0a4d7553ed0bb61cf303e713af5e4b33cf75712a0b744f40eaccd2fb2c39e"
SRC_URI[camx.sha256sum] = "88957ccd0c21e7465eb671498366d2bfcc50913b76c3f5088ae6319a53bab3f1"
SRC_URI[chicdk.sha256sum] = "bd0e12e2310aa038ef11c321a8ada241148afb56552d645987bdadcf75cf7936"
SRC_URI[camxcommon.sha256sum] = "23427ce018cb0c0ca058bfb2149aecd34f6abebf1134b424e40baf167b3597f2"

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
}

RPROVIDES:${PN} = "camxlib-monaco"
PACKAGE_BEFORE_PN += "camx-nhx ${PN}-skel"
RDEPENDS:${PN} += "${PN}-skel"
RRECOMMENDS:${PN} += "${@bb.utils.contains('DISTRO_FEATURES', 'opencl', 'virtual-opencl-icd', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'libglvnd', '', d)}"

FILES:camx-nhx = "\
    ${bindir}/nhx.sh \
    ${sysconfdir}/camera/test/NHX/ \
"
FILES:${PN}-skel = "\
    ${datadir}/camx \
    ${datadir}/qcom \
"
FILES:${PN} += "${libdir}/camx/${PLATFORM}/libmctf_cl_program.bin"

# Algo librarires are pre-compiled, pre-stripped.
# Skipping QA checks: 'already-stripped', 'arch', 'libdir' because:
# - Library files are Pre-stripped  (already-stripped)
# - skel binaries/library are not AArch64 (arch mismatch)      (arch)
# - Files are installed under /usr/share (non-libdir path) (libdir)
# - .so symlink is used for runtime DSP usage, not a dev artifact (dev-so)
INSANE_SKIP:${PN}-skel += " arch libdir already-stripped dev-so"

# Preserve ${PN}-skel naming to avoid ambiguity in package identification.
DEBIAN_NOAUTONAME:${PN}-skel = "1"
