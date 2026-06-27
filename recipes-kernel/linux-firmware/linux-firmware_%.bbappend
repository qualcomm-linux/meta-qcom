FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
# To make the layer pass yocto-check-layer only inherit update-alternatives when building for qualcomm
ALTERNATIVES_CLASS = ""
ALTERNATIVES_CLASS:qcom = "update-alternatives"

WHENCE_CHKSUM:qcom = "e9cc60f5df04120c652b2a7d460c938c"
PATCHTOOL:qcom = "git"

SRC_URI:append:qcom = " \
    file://0001-qcom-Add-gpu-firmwares-for-Shikra-chipset.patch \
    file://0002-QCA-Update-Bluetooth-WCN3950-firmware-1.3.0-00108-to.patch \
    file://0003-qcom-Add-qdsp6sw-firmware-for-shikra-platform.patch \
    file://0004-qcom-add-LPAICP-firmware-for-shikra-platform.patch \
    file://0005-qcom-update-CDSP-firmware-for-shikra-platform.patch \
"

PACKAGES:append:qcom = " \
    ${PN}-qcom-shikra-adreno \
    ${PN}-qcom-shikra-audio \
    ${PN}-qcom-shikra-modem \
"

LICENSE:${PN}-qcom-shikra-adreno:qcom = "Firmware-qcom"
LICENSE:${PN}-qcom-shikra-audio:qcom = "Firmware-qcom-2"
LICENSE:${PN}-qcom-shikra-modem:qcom = "Firmware-qcom-2"

FILES:${PN}-qcom-shikra-adreno:append:qcom = " \
    ${nonarch_base_libdir}/firmware/qcom/shikra/a704_zap.mbn* \
"

FILES:${PN}-qcom-shikra-audio:append:qcom = " \
    ${nonarch_base_libdir}/firmware/qcom/shikra/lpaicp*.* \
"

FILES:${PN}-qcom-shikra-modem:append:qcom = " \
    ${nonarch_base_libdir}/firmware/qcom/shikra/cqs/qdsp6sw.mbn* \
"

RDEPENDS:${PN}-qcom-shikra-adreno:qcom = "${PN}-qcom-license"
RDEPENDS:${PN}-qcom-shikra-audio:qcom = "${PN}-qcom-2-license"
RDEPENDS:${PN}-qcom-shikra-modem:qcom = "${PN}-qcom-2-license"

inherit_defer ${ALTERNATIVES_CLASS}

# firmware-ath6kl provides updated bdata.bin, which can not be accepted into main linux-firmware repo
ALTERNATIVE:${PN}-ath6k:qcom = "ar6004-hw13-bdata"
ALTERNATIVE_LINK_NAME[ar6004-hw13-bdata] = "${nonarch_base_libdir}/firmware/ath6k/AR6004/hw1.3/bdata.bin${@fw_compr_file_suffix(d)}"
