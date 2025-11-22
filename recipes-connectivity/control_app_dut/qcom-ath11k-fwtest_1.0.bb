SUMMARY = "Prebuilt Qualcomm ath11k firmware test utilities"
DESCRIPTION = "Prebuilt ath11k firmware test and validation utility"
LICENSE = "CLOSED"

PBT_BUILD_DATE = "251113.1"
ARTIFACTORY_URL = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/wlan-service.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto"
SOC_ARCH = "armv8-2a"

SRC_URI = "${ARTIFACTORY_URL}/qcom-ath11k-fwtest_1.0_${SOC_ARCH}.tar.gz"
SRC_URI[sha256sum] = "40ce6dbbda86e95406bc5a74ebe62c99eb63242305f74b7234998dfdffb2fbc5"

S = "${UNPACKDIR}"

RDEPENDS:${PN} += "libnl libnl-genl"

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
