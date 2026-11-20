PLATFORM = "hamoa"
PBT_BUILD_DATE = "261113"

require common.inc

SRC_URI[camxlib.sha256sum] = "5a561ecd813aed456417167c2dc03eaf82a3cd5b3e252f3e8fe40863ab71f15d"
SRC_URI[camx.sha256sum] = "df35a5691af4c7c4a1b939c3a61749990998dd7202b614325077bf3603d560b8"
SRC_URI[chicdk.sha256sum] = "c2d6b28e0202a59a66ae873c31ded7e7dc35b6c1ebbfbb1f6453ba08781912ad"
SRC_URI[camxcommon.sha256sum] = "2e6d3adcc1e24a5e755f17952ebf1abe7bb195eee59b564a443fce3b5b7263b2"
SRC_URI[camxtest.sha256sum] = "79eae0fc8f3f8a35e5682de37a1c669ad3b4a0afb84a1f9bac38563654c6dbc5"

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
