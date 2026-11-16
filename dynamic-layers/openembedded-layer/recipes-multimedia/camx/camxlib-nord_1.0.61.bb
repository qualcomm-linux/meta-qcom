PLATFORM = "nord"
PBT_BUILD_DATE = "261113"

require common.inc

SRC_URI[camxlib.sha256sum] = "87f23602de34b54288b38e4eb0e848c914151e65e96a95c18410e5fe59ace3c2"
SRC_URI[camx.sha256sum] = "62f747649d9b5fd5a565c4d227674965f299191a4ed660c77fd5ee30b54a8f7d"
SRC_URI[chicdk.sha256sum] = "23bb6cb505b054c935ba0c35fb4763ddc4db816852363f2d05297694b18a9a11"
SRC_URI[camxcommon.sha256sum] = "44550eac089e44ef43dd0db985a3b4fae012e45ddab16267d8d10d16cc444105"
SRC_URI[camxtest.sha256sum] = "bb407a99c59a10aae9e163f435aabec69f9a2f81c161dfa6b237e499e4b62b42"

DEPENDS += "opencv userspace-resource-manager"

do_install:append() {
    # copy skel file
    install -d ${D}${datadir}/qcom
    cp -r ${S}/usr/share/qcom/nord ${D}${datadir}/qcom/
}
PACKAGE_BEFORE_PN += "${PN}-skel"
RDEPENDS:${PN} += "${PN}-skel"
FILES:${PN}-skel = "${datadir}/qcom"
# Algo librarires are pre-compiled, pre-stripped.
# Skipping QA checks: 'already-stripped', 'arch', 'libdir' because:
# - Library files are Pre-stripped  (already-stripped)
# - skel binaries/library are not AArch64 (arch mismatch)      (arch)
# - Files are installed under /usr/share (non-libdir path) (libdir)
# Skipping QA checks:'ldflags file-rdeps' because:
# - Libraries are prebuilt dsp binary 
INSANE_SKIP:${PN}-skel += " arch libdir already-stripped ldflags file-rdeps"

# Preserve ${PN}-skel naming to avoid ambiguity in package identification.
DEBIAN_NOAUTONAME:${PN}-skel = "1"
# skel is dsp binary not AArch64, so skip file dependency scanning
SKIP_FILEDEPS:${PN}-skel = "1"
