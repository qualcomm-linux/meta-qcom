SUMMARY = "Prebuilt Qualcomm diagnostic library and utilities"
DESCRIPTION = "Prebuilt library and utility applications for diagnostic traffic"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/usr/share/doc/${PN}/NOLOGINBINARYLICENSEQTI.pdf;md5=4ceffe94cb40cdce6d2f4fb93cc063d1 \
                    file://${UNPACKDIR}/usr/share/doc/${PN}/NOTICE;md5=4b722aa0574e24873e07b94e40b92e4d"

PBT_BUILD_DATE = "251015"
ARTIFACTORY_URL = "https://softwarecenter.qualcomm.com/nexus/generic/software/chip/component/core-technologies.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto"
PBT_ARCH = "armv8a"

SRC_URI = "${ARTIFACTORY_URL}/diag_15.0.qcom_${PBT_ARCH}.tar.gz"
SRC_URI[sha256sum] = "36e301ac869f2e88e49bc0e0fd4a0c71e544ebad5b4070b47ce879d4ec00fd97"

S = "${UNPACKDIR}"

DEPENDS = "glib-2.0 "
RDEPENDS:${PN} += "glib-2.0"

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

inherit systemd

do_install() {
    # Install the prebuilt files maintaining the directory structure
    cp -r ${S}/usr ${D}/
    cp -r ${S}/etc ${D}/

    # Create any additional directories that might be needed
    install -d ${D}${sysconfdir}
    install -d ${D}${libdir}
    install -d ${D}${bindir}
    install -d ${D}${includedir}
}


INSANE_SKIP:${PN} = "already-stripped"
FILES:${PN} += "${systemd_unitdir}/system/"
