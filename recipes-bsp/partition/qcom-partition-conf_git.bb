SUMMARY = "Partition configuration for Qualcomm devices"
DESCRIPTION = "GPT partition binaries and QDL scripts for Qualcomm reference devices"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b0a8acd90d872086b279ead88af03369"

#SRC_URI = "git://github.com/qualcomm-linux/qcom-ptool.git;branch=main;protocol=https"
#SRCREV = "509133553f095292f2d9b691a29239d75d083392"
SRC_URI = "git://github.com/xinlon-z/qcom-ptool.git;branch=purwa-evk;protocol=https"
SRCREV = "d05ddd79b65b395f84d7ba8c8a7c7f5feacdd35a"

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
        if [ -e "${QCOM_PLATFORM_SUBDIR}/contents.xml" ]; then
            install -m 0644 ${QCOM_PLATFORM_SUBDIR}/contents.xml ${DEPLOYDIR}/partitions/${QCOM_PLATFORM_SUBDIR}
        fi
    done
}
addtask deploy before do_build after do_compile
