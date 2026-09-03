# Install NHLOS boot binaries in DEPLOY_DIR

S = "${UNPACKDIR}/${BOOTBINARIES}"
BOOTBINARIES_IMMUTABLE ?= "${BOOTBINARIES}_immutable"

# Some firmware releases split boot files across the main archive and an
# immutable companion archive. Deploy the recipe's source directory first, so
# recipes that override S to a subdirectory keep their existing layout, then
# deploy the optional immutable archive when it exists.
QCOM_BOOT_BINARY_DIRS ?= "${S} ${UNPACKDIR}/${BOOTBINARIES_IMMUTABLE}"

QCOM_BOOT_IMG_SUBDIR ?= ""

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
    done
}
addtask deploy before do_build after do_install

inherit allarch
