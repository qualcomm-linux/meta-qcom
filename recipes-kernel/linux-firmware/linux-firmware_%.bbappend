FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
# To make the layer pass yocto-check-layer only inherit update-alternatives when building for qualcomm
ALTERNATIVES_CLASS = ""
ALTERNATIVES_CLASS:qcom = "update-alternatives"

WHENCE_CHKSUM:qcom = "2479ba3a89ec32f3c8b23698b0a75ee5"
PATCHTOOL:qcom = "git"

SRC_URI:append:qcom = " \
    file://0001-qcom-sa8775p-update-signature-on-cdsp1-firmware.patch \
    file://0001-ath10k-WCN3990-hw1.0-add-shikra-firmware-files.patch \
"

inherit_defer ${ALTERNATIVES_CLASS}

# firmware-ath6kl provides updated bdata.bin, which can not be accepted into main linux-firmware repo
ALTERNATIVE:${PN}-ath6k:qcom = "ar6004-hw13-bdata"
ALTERNATIVE_LINK_NAME[ar6004-hw13-bdata] = "${nonarch_base_libdir}/firmware/ath6k/AR6004/hw1.3/bdata.bin${@fw_compr_file_suffix(d)}"
