FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
# To make the layer pass yocto-check-layer only inherit update-alternatives when building for qualcomm
ALTERNATIVES_CLASS = ""
ALTERNATIVES_CLASS:qcom = "update-alternatives"

WHENCE_CHKSUM:qcom = "633ca1cad82a881056299e9ae7c43c0a"
PATCHTOOL:qcom = "git"

SRC_URI:append:qcom = " \
    file://0001-linux-firmware-qcom-sync-audioreach-firmwares-from-v.patch \
"

PACKAGES:append:qcom = " \
    ${PN}-qcom-kaanapali-audio-tplg \
"

LICENSE:${PN}-qcom-kaanapali-audio-tplg:qcom = "Firmware-linaro"

FILES:${PN}-qcom-kaanapali-audio:append:qcom = " \
    ${nonarch_base_libdir}/firmware/qcom/kaanapali/Kaanapali-MTP-tplg.bin* \
"

RDEPENDS:${PN}-qcom-kaanapali-audio-tplg:qcom = "${PN}-linaro-license"

inherit_defer ${ALTERNATIVES_CLASS}

# firmware-ath6kl provides updated bdata.bin, which can not be accepted into main linux-firmware repo
ALTERNATIVE:${PN}-ath6k:qcom = "ar6004-hw13-bdata"
ALTERNATIVE_LINK_NAME[ar6004-hw13-bdata] = "${nonarch_base_libdir}/firmware/ath6k/AR6004/hw1.3/bdata.bin${@fw_compr_file_suffix(d)}"
