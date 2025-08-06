DESCRIPTION = "Contents file for invoking Axiom on Qualcomm QCS6490 platforms"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://contents-qcm6490-LICENSE.txt;md5=6e1bae7ef13289c332a27b917fb49764"

FW_ARTIFACTORY = "softwarecenter.qualcomm.com/download/software/chip/qualcomm_linux-spf-1-0/qualcomm-linux-spf-1-0_test_device_public"
FW_BUILD_ID = "r1.0_${PV}/qcm6490-le-1-0"

SRC_URI = " \
    https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/contents.xml;downloadfilename=contents-qcm6490_${PV}.xml;name=cxml-qcm6490 \
    https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/LICENSE.txt;downloadfilename=contents-qcm6490-LICENSE.txt;name=cxml-license-qcm6490 \
    "

QCOM_BOOT_IMG_SUBDIR = "qcm6490"
CONTENTS_XML_FILE = "contents-qcm6490_${PV}.xml"

include contentsxml-qcom-boot-common.inc
