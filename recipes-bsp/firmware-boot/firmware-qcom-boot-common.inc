# Install NHLOS boot binaries in DEPLOY_DIR

S = "${UNPACKDIR}/${BOOTBINARIES}"
BOOTBINARIES_IMMUTABLE ?= "${BOOTBINARIES}_immutable"

# Some firmware releases split boot files across the main archive and an
# immutable companion archive. Deploy the recipe's source directory first, so
# recipes that override S to a subdirectory keep their existing layout, then
# deploy the optional immutable archive when it exists.
QCOM_BOOT_BINARY_DIRS ?= "${S} ${UNPACKDIR}/${BOOTBINARIES_IMMUTABLE}"

QCOM_BOOT_IMG_SUBDIR ?= ""
QCOM_BOOT_SAIL_SUBDIR ?= ""

INHIBIT_DEFAULT_DEPS = "1"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

inherit deploy

do_deploy() {
    install -d ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}
    for bootdir in ${QCOM_BOOT_BINARY_DIRS}; do
        [ -d "${bootdir}" ] || continue

        find "${bootdir}" -maxdepth 1 -name 'gpt_*.bin' -delete
        find "${bootdir}" -maxdepth 1 -name 'zeros_*.bin' -delete
        find "${bootdir}" -maxdepth 1 -name '*.bin' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;
        find "${bootdir}" -maxdepth 1 -name '*.elf' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;
        find "${bootdir}" -maxdepth 1 -name '*.fv' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;
        find "${bootdir}" -maxdepth 1 -name '*.lzma' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;
        find "${bootdir}" -maxdepth 1 -name '*.mbn' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;
        find "${bootdir}" -maxdepth 1 -name '*.melf' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;
        find "${bootdir}" -maxdepth 1 -name 'qsahara_*.xml' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;
        find "${bootdir}" -maxdepth 1 -name '*.xz' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;

        # Some firmware releases ship a separate SAIL image, used to flash external NOR flash, in
        # its own subdirectory. When present Deploy it under a matching subdirectory of DEPLOYDIR.
        if [ -n "${QCOM_BOOT_SAIL_SUBDIR}" ] && [ -d "${bootdir}/${QCOM_BOOT_SAIL_SUBDIR}" ]; then
            SAIL_FILES="gpt_backup0.bin gpt_main0.bin prog_firehose_ddr.elf patch0.xml rawprogram0.xml sailhyp.elf \
                        sailsw1.elf rtsshyp.elf rtssjtagprogrammer.elf rtsssw1.elf rtsssw2.elf rtsssw3.elf sail_nor_dlp.bin \
            "

            install -d ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}/${QCOM_BOOT_SAIL_SUBDIR}
            for sail_file in ${SAIL_FILES}; do
                [ -f "${bootdir}/${QCOM_BOOT_SAIL_SUBDIR}/${sail_file}" ] || continue
                install -m 0644 ${bootdir}/${QCOM_BOOT_SAIL_SUBDIR}/${sail_file} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}/${QCOM_BOOT_SAIL_SUBDIR}
            done
        fi
    done
}
addtask deploy before do_build after do_install

# Optional secure-boot signing of the boot binaries, in place, before
# do_deploy (QCOM_FIRMWARE_SIGN_ENABLE).
inherit qcom-firmware-sign

QCOM_FIRMWARE_SIGN_DIRS = "${QCOM_BOOT_BINARY_DIRS}"
QCOM_FIRMWARE_SIGN_SUFFIXES = "mbn elf melf"
