DESCRIPTION = "Non-HLOS boot firmware for Qualcomm RB5Gen2 platform"
LICENSE = "LicenseRef-LICENSE.qcom-2"
LIC_FILES_CHKSUM = "\
    file://${UNPACKDIR}/LICENSE.${BPN};md5=6e1bae7ef13289c332a27b917fb49764 \
    file://${UNPACKDIR}/Notice.${BPN};md5=412afc27ddceb9cf8f743f61c3d6c401 \
"

FW_ARTIFACTORY = "softwarecenter.qualcomm.com/nexus/generic/product/chip/software-product/qcs8550-le-1-0/qcs8550-le-1-0_test_device_public"

BOOTBINARIES = "QCS8550_bootbinaries"

SRC_URI = " \
    https://${FW_ARTIFACTORY}/r${PV}/qcs8550-le-1-0-r1/common/build/common/bin/${BOOTBINARIES}.zip;downloadfilename=${BOOTBINARIES}_r${PV}.zip;name=bootbinaries \
    https://${FW_ARTIFACTORY}/r${PV}/qcs8550-le-1-0-r1/LICENSE.txt;downloadfilename=LICENSE.${BPN};name=license \
    https://${FW_ARTIFACTORY}/r${PV}/qcs8550-le-1-0-r1/Notice.txt;downloadfilename=Notice.${BPN};name=notice \
"
SRC_URI[license.sha256sum] = "3ad8f1fd82f2918c858cec2d0887b7df6f71a06416beecfdb3efe7d62874d863"
SRC_URI[notice.sha256sum] = "2357c75ab073d521836b7c7a90e01baadbf06170419b38b0eb5a5c4e00add9ee"

S = "${UNPACKDIR}/${BOOTBINARIES}/"

QCOM_BOOT_IMG_SUBDIR = "qcs8550"

include firmware-qcom-boot-common.inc
