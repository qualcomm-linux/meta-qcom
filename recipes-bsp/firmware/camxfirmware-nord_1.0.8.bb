SUMMARY = "Qualcomm camera firmware for Nord"
DESCRIPTION = "Qualcomm camera firmware to support camera functionality on Nord"
LICENSE = "LicenseRef-LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://usr/share/doc/${BPN}/LICENSE.QCOM-2.txt;md5=165287851294f2fb8ac8cbc5e24b02b0"

PBT_BUILD_DATE = "261010"
PBT_BRANCH = "master"
SRC_URI = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto_${PBT_BRANCH}/${BPN}_${PV}_armv8-2a.tar.gz"
SRC_URI[sha256sum] = "6474371fffa7019f69e5547b4008fcc4164cdae88dcedc4a59f10a698357e3e4"

S = "${UNPACKDIR}"

FW_QCOM_NAME = "nord"
require recipes-bsp/firmware/firmware-qcom.inc

# Disable configure and compile steps since this recipe uses prebuilt binaries.
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${FW_QCOM_PATH}
    install -m 0644 ${S}/usr/lib/firmware/qcom/nord/CAMERA_ICP.mbn ${D}${FW_QCOM_PATH}
    install -m 0644 ${S}/usr/lib/firmware/qcom/nord/CAMERA_ICP1.mbn ${D}${FW_QCOM_PATH}
    install -d ${D}${datadir}/doc/${BPN}
    install -m 0644 ${S}/usr/share/doc/${BPN}/LICENSE.QCOM-2.txt ${D}${datadir}/doc/${BPN}
}
