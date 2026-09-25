SUMMARY = "udev rules for Qualcomm raw partitions"
DESCRIPTION = "udev rules that skip filesystem probing for known Qualcomm raw GPT partitions"

require qcom-ptool.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

DEPENDS = "qcom-ptool-native"

inherit allarch

QCOM_RAW_PARTITIONS_RULES = "${B}/55-qcom-raw-partitions-noblkid.rules"

SRC_URI += "file://56-qcom-raw-partitions-systemd-unready.rules"

do_compile() {
    cd ${S}
    ${STAGING_BINDIR_NATIVE}/qcom-ptool gen_udev_rules \
        --output ${QCOM_RAW_PARTITIONS_RULES}
}

do_install() {
    install -Dm 0644 ${QCOM_RAW_PARTITIONS_RULES} \
        ${D}${nonarch_libdir}/udev/rules.d/55-qcom-raw-partitions-noblkid.rules
    install -Dm 0644 ${UNPACKDIR}/56-qcom-raw-partitions-systemd-unready.rules \
        ${D}${nonarch_libdir}/udev/rules.d/56-qcom-raw-partitions-systemd-unready.rules
}

FILES:${PN} = " \
    ${nonarch_libdir}/udev/rules.d/55-qcom-raw-partitions-noblkid.rules \
    ${nonarch_libdir}/udev/rules.d/56-qcom-raw-partitions-systemd-unready.rules \
"
