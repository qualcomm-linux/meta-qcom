SUMMARY = "udev rules for Qualcomm raw partitions"
DESCRIPTION = "Machine-specific udev rules that skip filesystem probing for reviewed Qualcomm raw GPT partitions"

require qcom-ptool.inc

DEPENDS = "qcom-ptool-native"

QCOM_PARTITION_FILES_SUBDIR ??= ""
QCOM_PARTITION_FILES_SUBDIR_SPINOR ??= ""

QCOM_RAW_PARTITIONS_RULES = "${B}/55-qcom-raw-partitions-noblkid.rules"
QCOM_RAW_PARTITION_LAYOUTS = " \
    ${QCOM_PARTITION_FILES_SUBDIR} \
    ${QCOM_PARTITION_FILES_SUBDIR_SPINOR} \
"

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_compile() {
    rm -f ${QCOM_RAW_PARTITIONS_RULES}

    set --
    for layout in ${QCOM_RAW_PARTITION_LAYOUTS}; do
        layout=${layout#partitions/}
        set -- "$@" --input "${S}/platforms/$layout/partitions.conf"
    done

    if [ "$#" -gt 0 ]; then
        if ${STAGING_BINDIR_NATIVE}/qcom-ptool \
                gen_udev_rules --help >/dev/null 2>&1; then
            ${STAGING_BINDIR_NATIVE}/qcom-ptool gen_udev_rules \
                --output ${QCOM_RAW_PARTITIONS_RULES} \
                "$@"
        else
            bbwarn "qcom-ptool ${SRCREV} has no gen_udev_rules support; skipping raw partition rules"
        fi
    fi
}

do_install() {
    if [ -f ${QCOM_RAW_PARTITIONS_RULES} ]; then
        install -Dm 0644 ${QCOM_RAW_PARTITIONS_RULES} \
            ${D}${nonarch_libdir}/udev/rules.d/55-qcom-raw-partitions-noblkid.rules
    fi
}

FILES:${PN} = " \
    ${nonarch_libdir}/udev/rules.d/55-qcom-raw-partitions-noblkid.rules \
"

ALLOW_EMPTY:${PN} = "1"
