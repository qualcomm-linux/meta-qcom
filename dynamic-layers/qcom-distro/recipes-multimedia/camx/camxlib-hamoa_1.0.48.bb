PLATFORM = "hamoa"
PBT_BUILD_DATE = "260923.1"

require common.inc

SRC_URI[camxlib.sha256sum] = "04bd4401dce3471badba3756f7d72c442c70e5ea81a48c5c9e042202f2575149"
SRC_URI[camx.sha256sum] = "cba9487e3ada7f9649685b81bf6ea17c8c40d7ce169284b2dae3fcf3cdbeec0d"
SRC_URI[chicdk.sha256sum] = "25142ac658c6d1eedb78c451c65a5b6fa94da2bae68e5b5c13c3a6c600b3e65c"
SRC_URI[camxcommon.sha256sum] = "8d7e0bac9f6050451d5c280af38067fff2a0cfe444f9b67074e30a9f0f1a378b"
SRC_URI[camxtest.sha256sum] = "3e25601ae086f050902cb6a231141fc3d7adb54c61d0d57ffecd745e308c177b"

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
