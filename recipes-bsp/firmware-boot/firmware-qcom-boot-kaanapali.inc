DESCRIPTION = "QCOM NHLOS Firmware for Qualcomm Kaanapali-MTP platform"
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/LICENSE.${BPN};md5=6e1bae7ef13289c332a27b917fb49764"

FW_ARTIFACTORY = "softwarecenter.qualcomm.com/nexus/generic/product/chip/software-product-family/Qualcomm_Linux.SPF.0.0/qualcomm_linux.spf.0.0-test-device-public"
BOOTBINARIES = "kaanapali_bootbinaries"

SRC_URI = " \
    https://${FW_ARTIFACTORY}/r0.0_${PV}/KAANAPALI.LE.0.0/common/build/ufs/bin/${BOOTBINARIES}.zip;downloadfilename=${BOOTBINARIES}_r0.0_${PV}.zip;name=bootbinaries \
    https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/LICENSE.txt;downloadfilename=LICENSE.${BPN};name=license \
    "
SRC_URI[license.sha256sum] = "3ad8f1fd82f2918c858cec2d0887b7df6f71a06416beecfdb3efe7d62874d863"

QCOM_BOOT_IMG_SUBDIR = "kaanapali"

include firmware-qcom-boot-common.inc
