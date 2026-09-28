SUMMARY = "Qualcomm Sectools v2"
DESCRIPTION = "Qualcomm Security Tools v2: the host \
binary used to sign, verify and inspect Qualcomm firmware images (XBL, \
TZ, modem, etc.) according to a per-chipset Security Profile XML"
HOMEPAGE = "https://softwarecenter.qualcomm.com/catalog/item/Qualcomm_Security_Tools"
LICENSE = "LicenseRef-Proprietary"
LIC_FILES_CHKSUM = "file://License.pdf;md5=b04bc87bdc900cc24ab2ce3447a53bb2"

# The download path carries the full version, the archive and its
# top-level directory only the first two parts (1.50.zip -> 1.50/).
ZIP_TOPDIR = "${@oe.utils.trim_version('${PV}', 2)}"

SRC_URI = "https://softwarecenter.qualcomm.com/api/download/software/tools/Qualcomm_Security_Tools/All/${PV}/${ZIP_TOPDIR}.zip;downloadfilename=qcom-sectools-${PV}.zip"
SRC_URI[sha256sum] = "8d6694f1d184e7c204d1e1ea50447a946d7f21838cfddb48b06f0c733d700cbe"

S = "${UNPACKDIR}/${ZIP_TOPDIR}"

# Pre-built binary; the archive ships Linux builds for x86_64 and aarch64
# hosts only (plus macOS and Windows, which are not installed).
COMPATIBLE_HOST = "^(x86_64|aarch64)-linux$"
SECTOOLS_PLATFORM_DIR = "${@'Linux_aarch64' if d.getVar('BUILD_ARCH') == 'aarch64' else 'Linux'}"

INHIBIT_DEFAULT_DEPS = "1"

inherit native

do_configure[noexec] = "1"
do_compile[noexec] = "1"

# The binary ships pre-stripped.
INHIBIT_SYSROOT_STRIP = "1"

do_install() {
    install -d "${D}${bindir}"
    install -m 0755 "${S}/${SECTOOLS_PLATFORM_DIR}/sectools" "${D}${bindir}/sectools"
}
