SUMMARY = "Qualcomm camera firmware for Talos"
DESCRIPTION = "Qualcomm camera firmware to support camera functionality on Talos"
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://usr/share/doc/${BPN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf;md5=7a5da794b857d786888bbf2b7b7529c8"

SRC_URI = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/${BPN}_${PV}_armv8-2a.tar.gz"
SRC_URI[sha256sum] = "b80202d60c1b0eb54023692f65c042a9d28e75df1ee41093856f82fbe651a0c3"
PBT_BUILD_DATE = "260203"

S = "${UNPACKDIR}"

inherit allarch

# Disable configure and compile steps since this recipe uses prebuilt binaries.
do_configure[noexec] = "1"
do_compile[noexec] = "1"

# Possible values are "xz" and "zst".
FIRMWARE_COMPRESSION ?= ""

do_install() {
    install -d ${D}${nonarch_base_libdir}/firmware/qcom/qcs615
    install -d ${D}${datadir}/doc/${BPN}

    cp -r ${S}/usr/lib/firmware/qcom/qcs615/CAMERA_ICP.elf ${D}${nonarch_base_libdir}/firmware/qcom/qcs615/

    case "${FIRMWARE_COMPRESSION}" in
        zst | zstd)
            zstd --compress --rm ${D}${nonarch_base_libdir}/firmware/qcom/qcs615/CAMERA_ICP.elf
            ;;
        xz)
            xz --compress --check=crc32 ${D}${nonarch_base_libdir}/firmware/qcom/qcs615/CAMERA_ICP.elf
            ;;
    esac

    install -m 0644 ${S}/usr/share/doc/${BPN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf ${D}${datadir}/doc/${BPN}
}

FILES:${PN} = "${nonarch_base_libdir}/firmware/qcom/qcs615"

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
