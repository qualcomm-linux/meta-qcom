SUMMARY = "Prebuilt Qualcomm Wireless Edge Services binaries, setup scripts and utility application"
DESCRIPTION = "Qualcomm Wireless Edge Services provide a suite of features Platform feature management, \
device attestation and secure provisioning. This recipe includes the daemon and scripts which setup \
the store and optionally load QcWES TA to provide these features."

LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://usr/share/doc/${BPN}/LICENSE.qcom-2;md5=165287851294f2fb8ac8cbc5e24b02b0"

PBT_BUILD_DATE = "260409"

SRC_URI = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/sec-userspace.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/qwes_${PV}_armv8a.tar.gz"
SRC_URI[sha256sum] = "2fc38e033aa32e59a1749017526d29e9ad71f38f168ff652383eb06a05be3985"

S = "${UNPACKDIR}"

inherit systemd

DEPENDS += "curl minkipc qmi-framework glibc"

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

SYSTEMD_SERVICE:${PN} = "qwesd.service"

do_install() {
    install -d ${D}${bindir}
    install -d ${D}${systemd_system_unitdir}
    install -d ${D}${docdir}/${BPN}

    # Install binaries
    install -m 0755 ${S}/usr/bin/* ${D}${bindir}/
    install -m 0644 ${S}/usr/lib/systemd/system/qwesd.service ${D}${systemd_system_unitdir}/qwesd.service
    install -m 0644 ${S}/usr/share/doc/${BPN}/NOTICE.txt ${D}${docdir}/${BPN}
    install -m 0644 ${S}/usr/share/doc/${BPN}/LICENSE.qcom-2 ${D}${docdir}/${BPN}
}
