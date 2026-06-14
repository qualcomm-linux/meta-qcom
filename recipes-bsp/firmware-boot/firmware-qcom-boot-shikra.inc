DESCRIPTION = "QCOM NHLOS Firmware for Qualcomm Shikra-EVK platform"
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/LICENSE.${BPN};md5=6e1bae7ef13289c332a27b917fb49764"

FW_ARTIFACTORY = "softwarecenter.qualcomm.com/nexus/generic/product/chip/tech-package/SHIKRA_bootbinaries.1.0/shikra_bootbinaries.1.0-test-device-public"

BOOTBINARIES = "SHIKRA_bootbinaries"

SRC_URI = " \
    https://${FW_ARTIFACTORY}/${PV}/${BOOTBINARIES}_${PV}.zip;name=bootbinaries \
    https://${FW_ARTIFACTORY}/${PV}/LICENSE.txt;downloadfilename=LICENSE.${BPN};name=license \
    https://artifacts.codelinaro.org/artifactory/codelinaro-le/Qualcomm_Linux/CQ2390/cdt/cq2390-itp.zip;downloadfilename=cq2390-itp_${PV}.zip;name=cq2390-itp \
    "
SRC_URI[license.sha256sum] = "3ad8f1fd82f2918c858cec2d0887b7df6f71a06416beecfdb3efe7d62874d863"
SRC_URI[cq2390-itp.sha256sum] = "acc8b39a9a8ccde5e04b762e9e4c7bbddf66f0420a5d32ed69a5a74761699f7b"

QCOM_BOOT_IMG_SUBDIR = "shikra"

include firmware-qcom-boot-common.inc
