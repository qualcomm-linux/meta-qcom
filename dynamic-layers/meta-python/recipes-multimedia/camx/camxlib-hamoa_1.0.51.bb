PLATFORM = "hamoa"
PBT_BUILD_DATE = "261010"

require common.inc

SRC_URI[camxlib.sha256sum] = "63a80f6631a96c5cd58fb3f8f1dcd34fa260e96bbcf7ff2ed0e4f820edc7a305"
SRC_URI[camx.sha256sum] = "8f44b99bfd7b51ee8690a37a54c10b27573c4a67d77e4ec3685493a954c14dd0"
SRC_URI[chicdk.sha256sum] = "d642a7ef27f991cacb928471c809706636f5daad54f98a391af915aaecfa50be"
SRC_URI[camxcommon.sha256sum] = "36f6ca585a6dc169bb4bb601213a65981dd0607ca637a9af7d4260f6c816a0e3"
SRC_URI[camxtest.sha256sum] = "d15dcdbaab12caa0a8b852f4bfc49a4d4d1fc62bfb99450079d71ccaa82893cb"

do_install:append() {
    # copy skel file
    install -d ${D}${datadir}/qcom
    cp -r ${S}/usr/share/qcom/x1e80100 ${D}${datadir}/qcom/
}
PACKAGE_BEFORE_PN += "${PN}-skel"
RDEPENDS:${PN} += "${PN}-skel"
FILES:${PN}-skel = "${datadir}/qcom"
# Algo librarires are pre-compiled, pre-stripped.
# Skipping QA checks: 'already-stripped', 'arch', 'libdir' because:
# - Library files are Pre-stripped  (already-stripped)
# - skel binaries/library are not AArch64 (arch mismatch)      (arch)
# - Files are installed under /usr/share (non-libdir path) (libdir)
# - .so symlink is used for runtime DSP usage, not a dev artifact (dev-so)
INSANE_SKIP:${PN}-skel += " arch libdir already-stripped dev-so"

# Preserve ${PN}-skel naming to avoid ambiguity in package identification.
DEBIAN_NOAUTONAME:${PN}-skel = "1"
