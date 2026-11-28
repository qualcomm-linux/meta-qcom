PLATFORM = "hamoa"
PBT_BUILD_DATE = "261127"

require common.inc

SRC_URI[camxlib.sha256sum] = "16e0c77a6febd05e7ed5d70888ba9799bd44f2a63e3c1024449e9641e517c226"
SRC_URI[camx.sha256sum] = "32a2185a83c71006f9ba43ff4fe52bf755655b0fa9e59b237628d40559c5557b"
SRC_URI[chicdk.sha256sum] = "2ae917de1e4c51f7a9ef9adcb7327cf85e5bdb96b9ab0f98afc4869342e18fb8"
SRC_URI[camxcommon.sha256sum] = "eb898e1c8a583c1de375f7b1c6875467b568d34a767e6ce051b54a2bfb7c01cf"
SRC_URI[camxtest.sha256sum] = "357bba070e90fd054199343f2ff1688b2cb14854b1d06e2a4db6957b8c82b590"

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
