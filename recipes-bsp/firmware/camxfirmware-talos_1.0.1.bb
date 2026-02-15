SUMMARY = "Qualcomm camera firmware for Talos"
DESCRIPTION = "Qualcomm camera firmware to support camera functionality on Talos"
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://usr/share/doc/${BPN}/LICENSE.QCOM-2.txt;md5=165287851294f2fb8ac8cbc5e24b02b0"

PBT_BUILD_DATE = "260209.1"
SRC_URI = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/${BPN}_${PV}_armv8-2a.tar.gz"
SRC_URI[sha256sum] = "b9f9e36ab2a81cc4b5583110776b45a281f63acb9974a09bee19735b6daaa0df"

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

    install -m 0644 ${S}/usr/share/doc/${BPN}/LICENSE.QCOM-2.txt ${D}${datadir}/doc/${BPN}
}

FILES:${PN} = "${nonarch_base_libdir}/firmware/qcom/qcs615"

# Firmware file are pre-compiled, pre-stripped, and not target architecture executables.
# Skipping QA checks: 'already-stripped', 'arch' because:
# - Firmware is not AArch64 ELF (arch check fails)
# - file is Pre-stripped  (already-stripped)
INSANE_SKIP:${PN} += "already-stripped arch"
