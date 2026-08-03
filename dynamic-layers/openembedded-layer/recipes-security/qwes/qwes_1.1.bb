SUMMARY = "Prebuilt Qualcomm Wireless Edge Services binaries, setup scripts and utility application"
DESCRIPTION = "Qualcomm Wireless Edge Services provide a suite of features Platform feature management, \
device attestation and secure provisioning. This recipe includes the daemon and scripts which setup \
the store and optionally load QcWES TA to provide these features."

LICENSE = "LicenseRef-LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://usr/share/doc/${BPN}/LICENSE.qcom-2;md5=165287851294f2fb8ac8cbc5e24b02b0"

PBT_BUILD_DATE = "260620"

SRC_URI = "https://softwarecenter.qualcomm.com/nexus/generic/software/chip/component/sec-userspace.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/qwes_1.0_armv8a.tar.gz"
SRC_URI[sha256sum] = "7bb20daa916e4b13e54948ab90af92e9c2e6129cc22da1a23b924adb3f0610d4"

S = "${UNPACKDIR}"

inherit systemd

DEPENDS += "curl minkipc qmi-framework glibc"

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

PACKAGES += "${PN}-ta"

SYSTEMD_SERVICE:${PN} = "qwesd.service"

do_install() {
    install -d ${D}${bindir}
    install -d ${D}${systemd_system_unitdir}
    install -d ${D}${docdir}/${BPN}
    install -d ${D}${nonarch_base_libdir}/qtee-tas

    # Install binaries
    install -m 0755 ${S}/usr/bin/* ${D}${bindir}/
    install -m 0644 ${S}/usr/lib/systemd/system/qwesd.service ${D}${systemd_system_unitdir}/qwesd.service
    install -m 0644 ${S}/usr/share/doc/${BPN}/NOTICE.txt ${D}${docdir}/${BPN}
    install -m 0644 ${S}/usr/share/doc/${BPN}/LICENSE.qcom-2 ${D}${docdir}/${BPN}
    cp -R ${S}/lib/qtee-tas/* ${D}${nonarch_base_libdir}/qtee-tas/
}

FILES:${PN}-ta += "${nonarch_base_libdir}/qtee-tas"
RDEPENDS:${PN} = "${PN}-ta"
INSANE_SKIP:${PN}-ta += "arch"