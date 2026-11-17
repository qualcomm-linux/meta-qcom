PLATFORM = "hamoa"
PBT_BUILD_DATE = "261113"

require common.inc

SRC_URI[camxlib.sha256sum] = "561f305ff9e30d01d5843df7aaed7a904299e688c98e1bb83232e90e876280e3"
SRC_URI[camx.sha256sum] = "bd371a18cf30087d0e592c3419d9c745632973d2c983b640e238bee326052bd6"
SRC_URI[chicdk.sha256sum] = "24f474ef8b62bece0b736feff4df3318799787e6828025ae93c8e36dae5e4046"
SRC_URI[camxcommon.sha256sum] = "c4979c30e20cdabeaea9fc8ceae09695a32f2ab0a39b7f382730bf28ea468c4b"
SRC_URI[camxtest.sha256sum] = "155cb1745e9608b42088aaf95f1b57d07d19110b986b9fc495d1b2d184a5712d"

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
