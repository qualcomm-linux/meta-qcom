SUMMARY = "Qualcomm Sectools v2 -- image signing / verification tool"
DESCRIPTION = "Qualcomm Security Tools v2: the host \
binary used to sign, verify and inspect Qualcomm firmware images (XBL, \
TZ, modem, etc.) according to a per-chipset Security Profile XML"
HOMEPAGE = "https://softwarecenter.qualcomm.com/catalog/item/Qualcomm_Security_Tools"
LICENSE = "LICENSE.qcom-2"

LIC_FILES_CHKSUM = "file://${UNPACKDIR}/${ZIP_TOPDIR}/CHANGES.txt;md5=d2a0bb01dcd8befe660b832fbbe05900"
ZIP_TOPDIR = "1.48"

SRC_URI = "https://softwarecenter.qualcomm.com/api/download/software/tools/Qualcomm_Security_Tools/All/${PV}/${ZIP_TOPDIR}.zip;name=sectools-zip;downloadfilename=qcom-sectools-${PV}.zip"
SRC_URI[sectools-zip.sha256sum] = "d89773bbfcc9c80c871b628bd2e766460a876277661ed6634d57590b7fd80fba"

S = "${UNPACKDIR}/${ZIP_TOPDIR}"

INHIBIT_DEFAULT_DEPS = "1"

inherit native

do_configure[noexec] = "1"
do_compile[noexec]   = "1"

# Pick the per-platform sectools binary that matches the build host.
SECTOOLS_PLATFORM_DIR = "${@'Linux_aarch64' if d.getVar('BUILD_ARCH') == 'aarch64' else 'Linux'}"

# Stage the per-platform sectools binary under ${datadir}/sectools/ and
# drop a thin symlink into ${bindir} so consumers can invoke `sectools`
# from PATH regardless of which subdir was selected.
do_install() {
    install -d "${D}${datadir}/sectools/${SECTOOLS_PLATFORM_DIR}"
    install -m 0755 "${S}/${SECTOOLS_PLATFORM_DIR}/sectools" \
        "${D}${datadir}/sectools/${SECTOOLS_PLATFORM_DIR}/sectools"

    install -d "${D}${bindir}"
    ln -sf "../share/sectools/${SECTOOLS_PLATFORM_DIR}/sectools" \
        "${D}${bindir}/sectools"

    install -m 0644 "${S}/CHANGES.txt" "${D}${datadir}/sectools/CHANGES.txt"
}

do_unpack[postfuncs] += "sectools_chmod_unpacked"
sectools_chmod_unpacked() {
    chmod -R u+w "${UNPACKDIR}"
}

FILES:${PN} += "${datadir}/sectools"
INSANE_SKIP:${PN} += "already-stripped"