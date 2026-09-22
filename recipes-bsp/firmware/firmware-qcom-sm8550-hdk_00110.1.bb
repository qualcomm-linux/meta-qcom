DESCRIPTION = "QCOM Firmware for QCS8550 based rb5gen2 board"

FW_ARTIFACTORY ?= "softwarecenter.qualcomm.com/nexus/generic/product/chip/software-product/qcs8550-le-1-0/qcs8550-le-1-0_test_device_public/"
SRC_URI = "\
    https://${FW_ARTIFACTORY}/r${PV}/qcs8550-le-1-0-r1/common/build/common/bin/QCS8550_fw.zip \
    https://${FW_ARTIFACTORY}/r${PV}/qcs8550-le-1-0-r1/LICENSE.txt;downloadfilename=LICENSE.${BPN};name=license \
    https://${FW_ARTIFACTORY}/r${PV}/qcs8550-le-1-0-r1/Notice.txt;downloadfilename=Notice.${BPN};name=notice \
"

SRC_URI[sha256sum] = "2f80545bbc842776d3ea5f0fab0be5a72f7e6451b35ee57d50cf7bcd1e0cfef9"
SRC_URI[license.sha256sum] = "3ad8f1fd82f2918c858cec2d0887b7df6f71a06416beecfdb3efe7d62874d863"
SRC_URI[notice.sha256sum] = "2357c75ab073d521836b7c7a90e01baadbf06170419b38b0eb5a5c4e00add9ee"

LICENSE = "LicenseRef-LICENSE.qcom-2"
LIC_FILES_CHKSUM = "\
    file://${UNPACKDIR}/LICENSE.${BPN};md5=6e1bae7ef13289c332a27b917fb49764 \
    file://${UNPACKDIR}/Notice.${BPN};md5=412afc27ddceb9cf8f743f61c3d6c401 \
"

FW_QCOM_NAME = "sm8550"

S = "${UNPACKDIR}/QCS8550_fw"

require recipes-bsp/firmware/firmware-qcom.inc

SPLIT_FIRMWARE_PACKAGES = "\
    linux-firmware-qcom-${FW_QCOM_NAME}-audio \
    linux-firmware-qcom-${FW_QCOM_NAME}-compute \
    linux-firmware-qcom-${FW_QCOM_NAME}-ipa \
    linux-firmware-qcom-${FW_QCOM_NAME}-modem \
    linux-firmware-qcom-${FW_QCOM_NAME}-venus \
"

do_install() {
    # for upstream kernel devicetree compatibility
    ln -sf adsp_dtbs.elf adsp/adsp_dtb.mbn
    ln -sf cdsp_dtbs.elf cdsp/cdsp_dtb.mbn
    ln -sf fw_ipa_gsi_5.5_w.elf fw/ipa_fws.mbn

    install -d ${D}${FW_QCOM_PATH}
    install -m 0644 ${S}/adsp/* ${D}${FW_QCOM_PATH}/
    install -m 0644 ${S}/cdsp/* ${D}${FW_QCOM_PATH}/
    install -m 0644 ${S}/fw/* ${D}${FW_QCOM_PATH}/

    if [ -r "${S}/proprietary/vpu30_4v.mbn" ] ; then
        install -d ${D}${FW_QCOM_BASE_PATH}/vpu
        install -m 0644 ${S}/proprietary/vpu30_4v.mbn ${D}${FW_QCOM_BASE_PATH}/vpu/vpu30_4v_sm8550.mbn
    fi
}
