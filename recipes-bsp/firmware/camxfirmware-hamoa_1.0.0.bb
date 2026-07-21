SUMMARY = "Qualcomm camera firmware for Hamoa"
DESCRIPTION = "Qualcomm camera firmware to support camera functionality on Hamoa"
LICENSE = "LicenseRef-LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://usr/share/doc/${BPN}/LICENSE.QCOM-2.txt;md5=165287851294f2fb8ac8cbc5e24b02b0"

PBT_BUILD_DATE = "260514"
SRC_URI = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/${BPN}_${PV}_armv8-2a.tar.gz"
SRC_URI[sha256sum] = "7201797be68d34ca826f6b2530590ab5f1bb3fb5c347a63259bee61a9b16036d"

S = "${UNPACKDIR}"

FW_QCOM_NAME = "x1e80100"
require recipes-bsp/firmware/firmware-qcom.inc

# Disable configure and compile steps since this recipe uses prebuilt binaries.
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${FW_QCOM_PATH}
    install -m 0644 ${S}/usr/lib/firmware/qcom/x1e80100/CAMERA_ICP.mbn ${D}${FW_QCOM_PATH}
    install -d ${D}${datadir}/doc/${BPN}
    install -m 0644 ${S}/usr/share/doc/${BPN}/LICENSE.QCOM-2.txt ${D}${datadir}/doc/${BPN}
}
