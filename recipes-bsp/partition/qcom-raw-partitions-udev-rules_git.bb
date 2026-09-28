SUMMARY = "udev rules for Qualcomm raw partitions"
DESCRIPTION = "udev rules that skip filesystem probing for known Qualcomm raw GPT partitions"

require qcom-ptool.inc

DEPENDS = "qcom-ptool-native"

inherit allarch

QCOM_RAW_PARTITIONS_RULES = "${B}/55-qcom-raw-partitions-noblkid.rules"

do_compile() {
    cd ${S}
    ${STAGING_BINDIR_NATIVE}/qcom-ptool gen_udev_rules \
        --output ${QCOM_RAW_PARTITIONS_RULES}
}

do_install() {
    install -Dm 0644 ${QCOM_RAW_PARTITIONS_RULES} \
        ${D}${nonarch_libdir}/udev/rules.d/55-qcom-raw-partitions-noblkid.rules
}

FILES:${PN} = " \
    ${nonarch_libdir}/udev/rules.d/55-qcom-raw-partitions-noblkid.rules \
"
