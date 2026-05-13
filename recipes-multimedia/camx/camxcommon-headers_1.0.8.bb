SUMMARY = "CamX common headers"

DESCRIPTION = "This recipe provides headers for all Qualcomm CamX stacks"

LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://usr/share/doc/${BPN}/LICENSE.QCOM-2.txt;md5=165287851294f2fb8ac8cbc5e24b02b0"

PBT_BUILD_DATE = "260512.2"
SRC_URI = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/${BPN}_${PV}_armv8-2a.tar.gz"
SRC_URI[sha256sum] = "186203a3e9d3497b04c00bf8e55a1f368f206ec1bfb94a7a59581e897d0f47dd"

S = "${UNPACKDIR}"

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

# Disable configure and compile steps since this recipe uses prebuilt binaries.
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${includedir}/camx/kodiak
    install -d ${D}${datadir}/doc/${BPN}

    cp -r ${S}/usr/include/*  ${D}${includedir}/

    install -m 0644 ${S}/usr/share/doc/${BPN}/LICENSE.QCOM-2.txt ${D}${datadir}/doc/${BPN}
}
