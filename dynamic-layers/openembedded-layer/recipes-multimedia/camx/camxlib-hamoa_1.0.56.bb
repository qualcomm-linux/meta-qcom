PLATFORM = "hamoa"
PBT_BUILD_DATE = "261026"

require common.inc

SRC_URI[camxlib.sha256sum] = "69cc0eb533e0c496b354a9ca5569cf9645fa72d914145af2a1b08e6aaa48592f"
SRC_URI[camx.sha256sum] = "3d52d35a389c3bde2b5303da646e0df9af9dab8252ad40bf46bababa814e43dd"
SRC_URI[chicdk.sha256sum] = "9517bd512242d731fc1a79b1be047658edb31cc6c653c72386732923447e8dd1"
SRC_URI[camxcommon.sha256sum] = "689f8103629afc8b16259216d5d069e21d5b4f995fa1773e3a316484cbd39323"
SRC_URI[camxtest.sha256sum] = "8c274e1d9f6f6db58a1cdc7bddfc6c886f6ae4d5cc61c91af1fa9710b4992c8e"

DEPENDS += "opencv userspace-resource-manager"

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
