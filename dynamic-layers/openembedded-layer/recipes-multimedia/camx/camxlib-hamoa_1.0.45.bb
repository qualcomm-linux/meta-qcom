PLATFORM = "hamoa"
PBT_BUILD_DATE = "260910"

require common.inc

SRC_URI[camxlib.sha256sum] = "599f14eeb3cecd9d58d3009b45882ee04dbcbfee5319c3f46936828966ee883f"
SRC_URI[camx.sha256sum] = "60304da488b187331a74a268244d6346589d4ca39e7661845b9dbcc5078a1597"
SRC_URI[chicdk.sha256sum] = "8b38b92e6e03396508d306412933d962d5a200a7ee2f436e4ba48b70aff0f8b9"
SRC_URI[camxcommon.sha256sum] = "ac9bba4563019444f1f9460b6412f0041addaaf22e2925ea49a01f2b6e33327d"
SRC_URI[camxtest.sha256sum] = "d6ecd8d9a43d395fdb611ef904a879e194181d6208ee72df65712e70f2a5ae9f"

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
