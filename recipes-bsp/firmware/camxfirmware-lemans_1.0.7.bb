SUMMARY = "Qualcomm camera firmware for lemans"
DESCRIPTION = "Qualcomm camera firmware to support camera functionality on lemans"
LICENSE = "LicenseRef-LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://usr/share/doc/${BPN}/LICENSE.QCOM-2.txt;md5=165287851294f2fb8ac8cbc5e24b02b0"

PBT_BUILD_DATE = "260209.1"
SRC_URI = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/${BPN}_${PV}_armv8-2a.tar.gz"
SRC_URI[sha256sum] = "62dd76046c1ae0e34f843285524be02e9fe96093ff68096cb998e9844f729f9d"

S = "${UNPACKDIR}"

FW_QCOM_NAME = "sa8775p"
require recipes-bsp/firmware/firmware-qcom.inc

# Disable configure and compile steps since this recipe uses prebuilt binaries.
do_configure[noexec] = "1"
do_compile[noexec] = "1"

def fw_compr_file_suffix(d):
    compr = d.getVar('FIRMWARE_COMPRESSION')
    if compr == '':
        return ''
    if compr == 'zstd':
        compr = 'zst'
    return '.' + compr

do_install() {
    install -d ${D}${FW_QCOM_PATH}
    install -m 0644 ${S}/usr/lib/firmware/qcom/sa8775p/CAMERA_ICP.mbn ${D}${FW_QCOM_PATH}
    install -d ${D}${datadir}/doc/${BPN}
    install -m 0644 ${S}/usr/share/doc/${BPN}/LICENSE.QCOM-2.txt ${D}${datadir}/doc/${BPN}

    # Monaco and Lemans platforms use same CAMX firmware.
    # Create symlinks under qcs8300 to satisfy platform-specific
    # lookup paths and avoid binary duplication for Monaco.
    install -d ${D}${FW_QCOM_BASE_PATH}/qcs8300
    ln -sf ../${FW_QCOM_NAME}/CAMERA_ICP.mbn${@fw_compr_file_suffix(d)} ${D}${FW_QCOM_BASE_PATH}/qcs8300/
}

PACKAGE_BEFORE_PN += "camxfirmware-monaco"
FILES:camxfirmware-monaco = "${FW_QCOM_BASE_PATH}/qcs8300"
