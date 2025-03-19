# Install NHLOS boot binaries in DEPLOY_DIR

S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"

QCOM_BOOT_IMG_SUBDIR ?= ""

INHIBIT_DEFAULT_DEPS = "1"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

inherit deploy

do_deploy() {
    install -d ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}
    find "${UNPACKDIR}/${BOOTBINARIES}" -name 'gpt_*.bin' -delete
    find "${UNPACKDIR}/${BOOTBINARIES}" -name '*.bin' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;
    find "${UNPACKDIR}/${BOOTBINARIES}" -name '*.elf' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;
    find "${UNPACKDIR}/${BOOTBINARIES}" -name '*.fv' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;
    find "${UNPACKDIR}/${BOOTBINARIES}" -name '*.mbn' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;
    find "${UNPACKDIR}/${BOOTBINARIES}" -name '*.melf' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;

    find "${UNPACKDIR}" -name 'cdt*bin' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;
}
addtask deploy before do_build after do_install

inherit allarch
