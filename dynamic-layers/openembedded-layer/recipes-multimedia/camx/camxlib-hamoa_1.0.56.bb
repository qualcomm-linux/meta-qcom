PLATFORM = "hamoa"
PBT_BUILD_DATE = "261026"

require common.inc

SRC_URI[camxlib.sha256sum] = "f9531e54b0f2dda5d6dcef23c605b4eb69ed686f58cac0fbcb22b92bdb03f3a0"
SRC_URI[camx.sha256sum] = "f383daf8c708615805d08eabfa6ee30f86b0ac5edccf4f3218eec47b50ff4ceb"
SRC_URI[chicdk.sha256sum] = "cb254523eb933475b04a168293c24c2613c50411933d8be27728aba6a7b1a635"
SRC_URI[camxcommon.sha256sum] = "8517c9736bbd545f4aba7ac4924e4212c90c40c4d6d5a9fffd6b955f56242659"
SRC_URI[camxtest.sha256sum] = "361b3fb9e5de6ced7c0d3f4beed34b016e436a36317c7b47fb89d7f3827551f4"

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
