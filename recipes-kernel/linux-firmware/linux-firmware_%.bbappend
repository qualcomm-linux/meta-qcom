FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
# To make the layer pass yocto-check-layer only inherit update-alternatives when building for qualcomm
ALTERNATIVES_CLASS = ""
ALTERNATIVES_CLASS:qcom = "update-alternatives"

WHENCE_CHKSUM:qcom = "ae141e453a0d546bdea8d2514d8b063e"
PATCHTOOL:qcom = "git"

SRC_URI:append:qcom = " \
    file://0001-qcom-sa8775p-update-signature-on-cdsp1-firmware.patch \
    file://0001-qcom-add-NSP-firmware-for-nord-platform.patch \
    file://0002-qcom-add-HPASS-firmware-for-nord-platform.patch \
    file://0003-qcom-add-QUPv3-firmware-for-nord.patch \
"

PACKAGES:append:qcom = " \
   ${PN}-qcom-nord-audio \
   ${PN}-qcom-nord-compute \
   ${PN}-qcom-nord-qupv3fw \
"

LICENSE:${PN}-qcom-nord-audio:qcom = "LicenseRef-Firmware-qcom-2"
LICENSE:${PN}-qcom-nord-compute:qcom = "LicenseRef-Firmware-qcom-2"
LICENSE:${PN}-qcom-nord-qupv3fw:qcom = "LicenseRef-Firmware-qcom"

FILES:${PN}-qcom-nord-compute:qcom = "${firmwaredir}/qcom/nord/cdsp*.*"
FILES:${PN}-qcom-nord-audio:qcom = "${firmwaredir}/qcom/nord/adsp*.*"
FILES:${PN}-qcom-nord-qupv3fw:qcom = "${firmwaredir}/qcom/nord/qupv3fw.elf*"

inherit_defer ${ALTERNATIVES_CLASS}

# firmware-ath6kl provides updated bdata.bin, which can not be accepted into main linux-firmware repo
ALTERNATIVE:${PN}-ath6k:qcom = "ar6004-hw13-bdata"
ALTERNATIVE_LINK_NAME[ar6004-hw13-bdata] = "${nonarch_base_libdir}/firmware/ath6k/AR6004/hw1.3/bdata.bin${@fw_compr_file_suffix(d)}"
