DESCRIPTION = "QCOM NHLOS Firmware for Qualcomm QRB2210 platform"
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/LICENSE.${BPN};md5=6e1bae7ef13289c332a27b917fb49764"

FW_ARTIFACTORY = "softwarecenter.qualcomm.com/nexus/generic/product/chip/tech-package/QRB2210_bootbinaries.1.0/qrb2210_bootbinaries.1.0-test-device-public"
BOOTBINARIES = "Agatti_bootbinaries"

SRC_URI = " \
    https://${FW_ARTIFACTORY}/${PV}/${BOOTBINARIES}.zip;downloadfilename=${BOOTBINARIES}_r1.0_${PV}.zip;name=bootbinaries \
    https://${FW_ARTIFACTORY}/${PV}/LICENSE.txt;downloadfilename=LICENSE.${BPN};name=license \
    https://artifacts.codelinaro.org/artifactory/clo-549-96boards-backup/96boards/rb1/linaro/rescue/23.12/rb1-bootloader-emmc-linux-47528.zip;downloadfilename=fw-cdt-uno-q_${PV}.zip;name=cdt-rb1 \
    "
SRC_URI[cdt-rb1.sha256sum] = "c75b6c63eb24c8ca36dad08ba4d4e93f3f4cd7dce60cf1b6dfb5790dc181cc3d"
SRC_URI[license.sha256sum] = "3ad8f1fd82f2918c858cec2d0887b7df6f71a06416beecfdb3efe7d62874d863"

QCOM_BOOT_IMG_SUBDIR = "qrb2210"

include firmware-qcom-boot-common.inc
