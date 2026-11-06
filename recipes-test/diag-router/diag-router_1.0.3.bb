SUMMARY = "Prebuilt Qualcomm diagnostic router application"
DESCRIPTION = "Prebuilt routing application for diagnostic traffic"
LICENSE = "LicenseRef-LICENSE.qcom-2 & MIT"
LIC_FILES_CHKSUM = "file://usr/share/doc/diag-router/LICENSE.QCOM-2.txt;md5=165287851294f2fb8ac8cbc5e24b02b0 \
                    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "https://softwarecenter.qualcomm.com/nexus/generic/software/chip/component/core-technologies.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/diag-router_15.0+really${PV}_armv8a.tar.gz \
           file://diag-router.service \
"

PBT_BUILD_DATE = "260616"
SRC_URI[sha256sum] = "88277ae47fb6febd04e2531b1e46027a3416ce540ba78c9e92d0abdbadbc9c94"

S = "${UNPACKDIR}"

inherit systemd

DEPENDS += "glib-2.0 qrtr"
RPROVIDES:${PN} = "virtual-diag-router"
RCONFLICTS:${PN} = "diag"

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

do_install() {
    install -d ${D}${bindir}

    # Install binaries
    install -m 0755 ${S}/usr/bin/* ${D}${bindir}/

    install -Dm 0644 ${UNPACKDIR}/diag-router.service \
            ${D}${systemd_system_unitdir}/diag-router.service
    sed -i -e "s,@bindir@,${bindir},g" \
            ${D}${systemd_system_unitdir}/diag-router.service
}

SYSTEMD_SERVICE:${PN} = "diag-router.service"
