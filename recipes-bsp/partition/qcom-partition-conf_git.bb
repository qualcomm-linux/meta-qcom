SUMMARY = "Partition configuration for Qualcomm devices"
DESCRIPTION = "GPT partition binaries and QDL scripts for Qualcomm reference devices"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b0a8acd90d872086b279ead88af03369"

SRC_URI = "git://github.com/qualcomm-linux/qcom-ptool.git;branch=main;protocol=https"
SRCREV = "7a53ec22da88e953586519833c24e6b677e8e5b9"

INHIBIT_DEFAULT_DEPS = "1"

inherit deploy allarch

do_install[noexec] = "1"

do_deploy() {
    cd platforms
    for gpt in `find . -name gpt_main0.bin` ; do
        QCOM_PLATFORM_SUBDIR=${gpt%%/gpt_main0.bin}
        install -d ${DEPLOYDIR}/partitions/${QCOM_PLATFORM_SUBDIR}
        install -m 0644 ${QCOM_PLATFORM_SUBDIR}/gpt_backup*.bin -D ${DEPLOYDIR}/partitions/${QCOM_PLATFORM_SUBDIR}
        install -m 0644 ${QCOM_PLATFORM_SUBDIR}/gpt_both*.bin -D ${DEPLOYDIR}/partitions/${QCOM_PLATFORM_SUBDIR}
        install -m 0644 ${QCOM_PLATFORM_SUBDIR}/gpt_empty*.bin -D ${DEPLOYDIR}/partitions/${QCOM_PLATFORM_SUBDIR}
        install -m 0644 ${QCOM_PLATFORM_SUBDIR}/gpt_main*.bin -D ${DEPLOYDIR}/partitions/${QCOM_PLATFORM_SUBDIR}
        install -m 0644 ${QCOM_PLATFORM_SUBDIR}/patch*.xml -D ${DEPLOYDIR}/partitions/${QCOM_PLATFORM_SUBDIR}
        install -m 0644 ${QCOM_PLATFORM_SUBDIR}/rawprogram*.xml -D ${DEPLOYDIR}/partitions/${QCOM_PLATFORM_SUBDIR}
        install -m 0644 ${QCOM_PLATFORM_SUBDIR}/zeros_*.bin -D ${DEPLOYDIR}/partitions/${QCOM_PLATFORM_SUBDIR}
        install -m 0644 ${QCOM_PLATFORM_SUBDIR}/wipe_rawprogram_PHY*.xml -D ${DEPLOYDIR}/partitions/${QCOM_PLATFORM_SUBDIR}
    done
}
addtask deploy before do_build after do_compile

python __anonymous() {
    import bb
    bbnote("SRC_URI_rishi: %s" % d.getVar('SRC_URI'))
    bbnote("SRCREV_rishi: %s" % d.getVar('SRCREV'))
}
