SUMMARY = "Qualcomm camera firmware for kodiak"
DESCRIPTION = "Qualcomm camera firmware to support camera functionality on Kodiak"
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://usr/share/doc/${BPN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf;md5=7a5da794b857d786888bbf2b7b7529c8"

SRC_URI = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/${BPN}_${PV}_armv8-2a.tar.gz"
SRC_URI[sha256sum] = "c99789ded4510ef11b81dbb1d596fa2ca13e7815a3666a15dbd07675ce03e23d"
PBT_BUILD_DATE = "260102"

S = "${UNPACKDIR}"

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

# Disable configure and compile steps since this recipe uses prebuilt binaries.
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${nonarch_base_libdir}/firmware/qcom/qcm6490
    install -d ${D}${datadir}/doc/${BPN}

    cp -r ${S}/usr/lib/firmware/CAMERA_ICP_170.elf ${D}${nonarch_base_libdir}/firmware/qcom/qcm6490/
    install -m 0644 ${S}/usr/share/doc/${BPN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf ${D}${datadir}/doc/${BPN}
}

FILES:${PN} = "${nonarch_base_libdir}/firmware/qcom/qcm6490"

# Disable default build deps
INHIBIT_DEFAULT_DEPS = "1"

# Inhibit stripping and debug-split for the package
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

# Firmware file are pre-compiled, pre-stripped, and not target architecture executables.
# Skipping QA checks: 'already-stripped', 'arch' because:
# - Firmware is not AArch64 ELF (arch check fails)
# - file is Pre-stripped  (already-stripped)
INSANE_SKIP:${PN} += "already-stripped arch"
