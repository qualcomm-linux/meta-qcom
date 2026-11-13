PLATFORM = "hamoa"
PBT_BUILD_DATE = "261104"

require common.inc

SRC_URI[camxlib.sha256sum] = "c5e2d0298562ebf55b1654aed31f595a1d3c2b2d60af768c02a8c3f1755cce93"
SRC_URI[camx.sha256sum] = "fe18196202b6f0ffeed4843cc6abcc842afc5ee405beb594948c7e30ea8ce497"
SRC_URI[chicdk.sha256sum] = "8f37effb25425495aabf671367aefc440a6ee7af2b4d86f5d1ee565348a41a80"
SRC_URI[camxcommon.sha256sum] = "6697bbb4692c3290b6cabda1442d1917319b70448da406cac02250b59c9c1f65"
SRC_URI[camxtest.sha256sum] = "bc13ee5daa994d54509fe05d759c08c955fb1e7103240fd1ccb6780bf4ea858f"

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
