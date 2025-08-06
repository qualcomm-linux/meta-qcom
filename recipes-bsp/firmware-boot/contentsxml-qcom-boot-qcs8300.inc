DESCRIPTION = "Contents file for invoking Axiom on Qualcomm QCS8300 platforms"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://contents-qcs8300-LICENSE.txt;md5=6e1bae7ef13289c332a27b917fb49764"

FW_ARTIFACTORY = "softwarecenter.qualcomm.com/download/software/chip/qualcomm_linux-spf-1-0/qualcomm-linux-spf-1-0_test_device_public"
FW_BUILD_ID = "r1.0_${PV}/qcs8300-le-1-0"

SRC_URI = " \
    https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/contents.xml;downloadfilename=contents-qcs8300_${PV}.xml;name=cxml-qcs8300 \
    https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/LICENSE.txt;downloadfilename=contents-qcs8300-LICENSE.txt;name=cxml-license-qcs8300 \
    "

QCOM_BOOT_IMG_SUBDIR = "qcs8300"
CONTENTS_XML_FILE = "contents-qcs8300_${PV}.xml"

include contentsxml-qcom-boot-common.inc
