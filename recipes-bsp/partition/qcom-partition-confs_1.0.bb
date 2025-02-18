SUMMARY = "Qualcomm machine specific partition configuration"
DESCRIPTION = "Machine partition configuration used by qcom-gen-partitions-tool \
for generating the machine specific partition.xml file."
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

DEPENDS += "qcom-gen-partitions-tool-native"

SRC_URI = " \
    file://qcm6490-partitions.conf \
    file://qcs9100-partitions.conf \
    file://qrb2210-partitions.conf \
"

PROVIDES += "virtual/partconf"

S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"
B = "${WORKDIR}/build"

inherit python3native deploy

INHIBIT_DEFAULT_DEPS = "1"

do_configure[noexec] = "1"

PARTCONF ?= ""
PARTCONF:qcm6490 ?= "qcm6490-partitions.conf"
PARTCONF:qcs9100 ?= "qcs9100-partitions.conf"
PARTCONF:qrb2210 ?= "qrb2210-partitions.conf"

# For machines with a published cdt file, let's make sure we flash it
fixup_cdt() {
    sed -i '/name=cdt/ s/$/ --filename=cdt.bin/' ${S}/${PARTCONF}
}

do_compile:prepend:qcs6490-rb3gen2-core-kit() {
    fixup_cdt
}

do_compile:prepend:qcs9100-ride-sx() {
    fixup_cdt
}

do_compile() {
    gen_partition.py -i ${S}/${PARTCONF} -o ${B}/${MACHINE}-partition.xml
}

do_install() {
    install -D -m0644 ${B}/${MACHINE}-partition.xml ${D}${datadir}/qcom-partition-confs/${MACHINE}-partition.xml
}

do_deploy() {
    install -m 0644 ${B}/${MACHINE}-partition.xml -D ${DEPLOYDIR}/partition.xml
}
addtask deploy after do_install

COMPATIBLE_MACHINE = "(qcm6490|qcs9100|qrb2210)"
PACKAGE_ARCH = "${MACHINE_ARCH}"
