FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

WHENCE_CHKSUM:qcom = "1b708a2f47f82f54d3aa8b9628e4db0c"

PATCHTOOL:qcom = "git"

SRC_URI:append:qcom = " file://0001-qcom-Update-ADSP-firmware-for-QCM6490-platform.patch "
# To make the layer pass yocto-check-layer only inherit update-alternatives when building for qualcomm
ALTERNATIVES_CLASS = ""
ALTERNATIVES_CLASS:qcom = "update-alternatives"

inherit_defer ${ALTERNATIVES_CLASS}

# firmware-ath6kl provides updated bdata.bin, which can not be accepted into main linux-firmware repo
ALTERNATIVE:${PN}-ath6k:qcom = "ar6004-hw13-bdata"
ALTERNATIVE_LINK_NAME[ar6004-hw13-bdata] = "${nonarch_base_libdir}/firmware/ath6k/AR6004/hw1.3/bdata.bin${@fw_compr_file_suffix(d)}"

do_install:append:rb3gen2-core-kit() {
    install -d ${D}${nonarch_base_libdir}/firmware/ath12k/QCC2072/hw1.0
}
FILES:${PN}:append:rb3gen2-core-kit = " ${nonarch_base_libdir}/firmware/ath12k/QCC2072/"
