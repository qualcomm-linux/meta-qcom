PLATFORM = "nord"
PBT_BUILD_DATE = "261127"

require common.inc

SRC_URI[camxlib.sha256sum] = "26cd5b4b4700b2bfb71f9007a6b09082bd89bfe07be9cf5e75c7ee1839de36a3"
SRC_URI[camx.sha256sum] = "fb16a37e8c37104f0edfed7575833116bacf6ce98009e162f7e9f753256e25d1"
SRC_URI[chicdk.sha256sum] = "49941d252270e11c0e6622ed822d29ef3d9ea167b22a1aff6666ec1a0dbfe415"
SRC_URI[camxcommon.sha256sum] = "793cd3706afa11ebb44959eb9c5b7daa8694a78027cb02ca257985037ad16665"
SRC_URI[camxtest.sha256sum] = "9420528ee30d1177855311df26cc349b015355cf1ecdb613da8078b1d64e20bd"

DEPENDS += "opencv userspace-resource-manager"

do_install:append() {
    # copy skel file
    install -d ${D}${datadir}/qcom
    cp -r ${S}/usr/share/qcom/nord ${D}${datadir}/qcom/
}
PACKAGE_BEFORE_PN += "${PN}-skel"
RDEPENDS:${PN} += "${PN}-skel"
FILES:${PN}-skel = "${datadir}/qcom"
# - skel binaries/library are not AArch64 (arch mismatch)      (arch)
# - Files are installed under /usr/share (non-libdir path) (libdir)
# Skipping QA checks:'ldflags' because:
# - Libraries are prebuilt dsp binary 
INSANE_SKIP:${PN}-skel += " arch libdir ldflags"

# Preserve ${PN}-skel naming to avoid ambiguity in package identification.
DEBIAN_NOAUTONAME:${PN}-skel = "1"
# skel is dsp binary not AArch64, so skip file dependency scanning
SKIP_FILEDEPS:${PN}-skel = "1"
