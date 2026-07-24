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
    find "${S}" -maxdepth 1 -name 'qsahara_*.xml' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;
    find "${S}" -maxdepth 1 -name '*.xz' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;

}
addtask deploy before do_build after do_install

# Optional secure-boot signing of pre-built MBN/ELF/MELF images.
# When QCOM_FIRMWARE_SIGN_ENABLE is "0" (default) this is a no-op
inherit qcom-firmware-sign

do_qcom_firmware_sign() {
    if ! qcom_check_signing_enabled ; then
        return 0
    fi

    bbnote "qcom-firmware-sign: walking ${S} for MBN/ELF/MELF blobs"
    find "${S}" -maxdepth 1 -type f \
        \( -iname '*.mbn' -o -iname '*.elf' -o -iname '*.melf' \) \
        | while read -r f; do
            qcom_sign_verify_file "${f}" || bbfatal "Failed to sign: ${f}"
        done
}

inherit allarch
