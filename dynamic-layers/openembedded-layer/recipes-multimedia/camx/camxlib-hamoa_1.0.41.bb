PLATFORM = "hamoa"
PBT_BUILD_DATE = "260826"

require common.inc

SRC_URI[camxlib.sha256sum] = "70208241d530536878c202ac512f4e39019827b9feb2ba9177d904416dbab198"
SRC_URI[camx.sha256sum] = "7d2d59c06788a57487b43a0c6792b62822c8992718c15ab5811cdd03b169fc79"
SRC_URI[chicdk.sha256sum] = "5896081c3200da9c497079e217cae2c616cfd610ea2f2c80a6c5a636052de6bf"
SRC_URI[camxcommon.sha256sum] = "76b94b2af3ad33cf6ffcb768dfe42e85352adf231488e42b99de5575d191100e"
SRC_URI[camxtest.sha256sum] = "ccf1b6430bb494b34a0db616c7f1f7940fed925531c76f1a8ed2a7211381609c"

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
