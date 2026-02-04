# Modify partition configuration for U-Boot bootloader

do_deploy:append() {
    # Check if U-Boot is the bootloader
    BOOTLOADER="${PREFERRED_PROVIDER_virtual/bootloader}"

    if [ "$BOOTLOADER" = "u-boot-qcom-next" ] || [ "$BOOTLOADER" = "u-boot-qcom-next-upstream" ]; then
        bbnote "U-Boot detected as bootloader, modifying partition XML files"

        # Modify all rawprogram XML files in DEPLOYDIR (per-machine, safe)
        for xml in ${DEPLOYDIR}/${QCOM_PARTITION_FILES_SUBDIR}/rawprogram*.xml; do
            if [ -f "$xml" ]; then
                sed -i 's/uefi\.elf/u-boot.mbn/g' "$xml"
                bbnote "Modified $(basename $xml): uefi.elf -> u-boot.mbn"
            fi
        done

        # Modify contents.xml file in DEPLOYDIR
        if [ -f "${DEPLOYDIR}/${QCOM_PARTITION_FILES_SUBDIR}/contents.xml" ]; then
            sed -i 's/<file_name>uefi\.elf<\/file_name>/<file_name>u-boot.mbn<\/file_name>/g' \
                "${DEPLOYDIR}/${QCOM_PARTITION_FILES_SUBDIR}/contents.xml"
            bbnote "Modified contents.xml: uefi.elf -> u-boot.mbn"
        fi
    else
        bbnote "Using default UEFI bootloader (uefi.elf)"
    fi
}
