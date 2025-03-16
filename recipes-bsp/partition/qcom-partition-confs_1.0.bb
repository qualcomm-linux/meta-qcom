SUMMARY = "Qualcomm machine specific partition configuration"
DESCRIPTION = "GPT partition binaries and QDL scripts for supported machines."
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

DEPENDS += "qcom-gen-partitions-tool-native qcom-ptool-native"

SRC_URI = " \
    file://qcm6490-partitions.conf \
    file://qcs9100-partitions.conf \
    file://qcm2290-partitions.conf \
"

S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"
B = "${WORKDIR}/build"

inherit python3native deploy

INHIBIT_DEFAULT_DEPS = "1"

do_configure[noexec] = "1"
do_install[noexec] = "1"

QCOM_PARTCONF_SUBDIR ?= ""
QCOM_PARTCONF_SUBDIR:qcm6490 ?= "qcm6490"
QCOM_PARTCONF_SUBDIR:qcs9100 ?= "qcs9100"

PARTCONF ?= ""
PARTCONF:qcm6490 ?= "qcm6490-partitions.conf"
PARTCONF:qcs9100 ?= "qcs9100-partitions.conf"
PARTCONF:qcm2290 ?= "qcm2290-partitions.conf"

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

COMPATIBLE_MACHINE = "(qcm6490|qcs9100|qcm2290)"
