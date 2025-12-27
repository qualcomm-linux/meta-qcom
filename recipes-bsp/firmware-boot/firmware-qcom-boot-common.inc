# Install NHLOS boot binaries in DEPLOY_DIR

S = "${UNPACKDIR}/${BOOTBINARIES}"

QCOM_BOOT_IMG_SUBDIR ?= ""

INHIBIT_DEFAULT_DEPS = "1"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

inherit deploy

do_deploy() {
    install -d ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}
    find "${S}" -maxdepth 1 -name 'gpt_*.bin' -delete
    find "${S}" -maxdepth 1 -name 'zeros_*.bin' -delete
    find "${S}" -maxdepth 1 -name '*.bin' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;
    find "${S}" -maxdepth 1 -name '*.elf' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;
    find "${S}" -maxdepth 1 -name '*.fv' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;
    find "${S}" -maxdepth 1 -name '*.lzma' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;
    find "${S}" -maxdepth 1 -name '*.mbn' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;
    find "${S}" -maxdepth 1 -name '*.melf' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;
    find "${S}" -maxdepth 1 -name '*.xz' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;

    find "${UNPACKDIR}" -iname '*cdt*.bin' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;
}
addtask deploy before do_build after do_install

inherit allarch
