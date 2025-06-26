SUMMARY = "Partition configuration for ${MACHINE_DESC}"
DESCRIPTION = "GPT partition binaries and QDL scripts for ${MACHINE_DESC}."
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

DEPENDS += "qcom-ptool-native"

SRC_URI = "file://${PARTCONF}"

S = "${UNPACKDIR}"
B = "${WORKDIR}/build"

inherit python3native deploy

inherit allarch

INHIBIT_DEFAULT_DEPS = "1"

do_configure[noexec] = "1"
do_install[noexec] = "1"

do_compile() {
    gen_partition.py -i ${S}/${PARTCONF} -o ${B}/partition.xml
    ptool.py -x ${B}/partition.xml
}

do_deploy() {
    install -d ${DEPLOYDIR}/${QCOM_PARTCONF_SUBDIR}
    install -m 0644 ${B}/gpt_backup*.bin -D ${DEPLOYDIR}/${QCOM_PARTCONF_SUBDIR}
    install -m 0644 ${B}/gpt_both*.bin -D ${DEPLOYDIR}/${QCOM_PARTCONF_SUBDIR}
    install -m 0644 ${B}/gpt_empty*.bin -D ${DEPLOYDIR}/${QCOM_PARTCONF_SUBDIR}
    install -m 0644 ${B}/gpt_main*.bin -D ${DEPLOYDIR}/${QCOM_PARTCONF_SUBDIR}
    install -m 0644 ${B}/patch*.xml -D ${DEPLOYDIR}/${QCOM_PARTCONF_SUBDIR}
    install -m 0644 ${B}/rawprogram*.xml -D ${DEPLOYDIR}/${QCOM_PARTCONF_SUBDIR}
    install -m 0644 ${B}/zeros_*.bin -D ${DEPLOYDIR}/${QCOM_PARTCONF_SUBDIR}
    install -m 0644 ${B}/wipe_rawprogram_PHY*.xml -D ${DEPLOYDIR}/${QCOM_PARTCONF_SUBDIR}
}
addtask deploy before do_build after do_compile
