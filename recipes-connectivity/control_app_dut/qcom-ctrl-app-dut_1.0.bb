SUMMARY = "Prebuilt Qualcomm Control Application DUT utilities"
DESCRIPTION = "Qualcomm Technologies Control Application for Device Under Test"
LICENSE = "CLOSED"

PBT_BUILD_DATE = "251028"
ARTIFACTORY_URL = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/wlan-service.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto"
SOC_ARCH = "armv8-2a"

SRC_URI = "${ARTIFACTORY_URL}/qcom-ctrl-app-dut_1.0_${SOC_ARCH}.tar.gz"
SRC_URI[sha256sum] = "c639b71a3df65ec87a93c96e3bc68eb736fc5d9e9fafcaa6487df97a0e9638f1"

S = "${UNPACKDIR}"

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

do_install() {
    # Create any additional directories that might be needed
    install -d ${D}${sbindir}

    # Install the prebuilt files maintaining the directory structure
    install -m 0755 ${S}/usr/sbin/* ${D}${sbindir}/
}

INSANE_SKIP:${PN} = "already-stripped"
