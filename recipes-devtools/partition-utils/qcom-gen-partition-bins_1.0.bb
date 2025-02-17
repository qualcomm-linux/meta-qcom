SUMMARY = "Generate and deploy the machine specific partition binaries"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=7a434440b651f4a472ca93716d01033a"

DEPENDS += "qcom-ptool-native"

inherit deploy python3native

INHIBIT_DEFAULT_DEPS = "1"

do_configure[noexec] = "1"
do_install[noexec] = "1"
do_compile[depends] += "virtual/partconf:do_deploy"

do_compile() {
    ptool.py -x ${DEPLOY_DIR_IMAGE}/partition.xml
}

do_deploy() {
    install -m 0644 ${B}/gpt_backup*.bin -D ${DEPLOYDIR}
    install -m 0644 ${B}/gpt_both*.bin -D ${DEPLOYDIR}
    install -m 0644 ${B}/gpt_empty*.bin -D ${DEPLOYDIR}
    install -m 0644 ${B}/gpt_main*.bin -D ${DEPLOYDIR}
    install -m 0644 ${B}/patch*.xml -D ${DEPLOYDIR}
    install -m 0644 ${B}/rawprogram*.xml -D ${DEPLOYDIR}
    install -m 0644 ${B}/zeros_*.bin -D ${DEPLOYDIR}
    install -m 0644 ${B}/wipe_rawprogram_PHY*.xml -D ${DEPLOYDIR}
}
addtask deploy before do_build after do_compile

COMPATIBLE_MACHINE = "(qcm6490|qcs9100|qrb2210)"
PACKAGE_ARCH = "${MACHINE_ARCH}"
