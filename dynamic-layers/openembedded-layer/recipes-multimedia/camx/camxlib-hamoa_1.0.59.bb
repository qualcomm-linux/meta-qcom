PLATFORM = "hamoa"
PBT_BUILD_DATE = "261104"

require common.inc

SRC_URI[camxlib.sha256sum] = "765296aa5cea48b9a440ff04e4a8c3c617a2ef9adb59663741b7c5c8083f82a1"
SRC_URI[camx.sha256sum] = "ea45ccecfc6394b2c22f5cc91b43d9915e6f0910a98aaee133bcf6496f003c34"
SRC_URI[chicdk.sha256sum] = "1874a6f33e112fc310eddcf1edd4641f4815ef03c2a280a41828014d8d287aa0"
SRC_URI[camxcommon.sha256sum] = "f94abd94d913904003004d62011b3d22df09c606b3d2e203db8a95f6dec7c6c0"
SRC_URI[camxtest.sha256sum] = "6ab0d5b87558d338a7fa5d06a9e24d56aabd7d64be4ca3338f7e9f43fa2fe893"

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
