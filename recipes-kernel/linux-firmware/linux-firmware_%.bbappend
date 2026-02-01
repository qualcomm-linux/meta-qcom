FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
# To make the layer pass yocto-check-layer only inherit update-alternatives when building for qualcomm
ALTERNATIVES_CLASS = ""
ALTERNATIVES_CLASS:qcom = "update-alternatives"

WHENCE_CHKSUM:qcom = "fa1ecc68d64a3281098513d08149c230"
PATCHTOOL:qcom = "git"

SRC_URI:append:qcom = " \
    file://0001-linux-firmware-qcom-sync-audioreach-firmwares-from-v.patch \
"

PACKAGES:append:qcom = " \
    ${PN}-qcom-sm8450-audio-tplg \
"

LICENSE:${PN}-qcom-sm8450-audio-tplg:qcom = "Firmware-linaro"
LICENSE:${PN}-qcom-sm8750-audio:append:qcom = " & Firmware-linaro"
LICENSE:${PN}-qcom-qcs615-audio:append:qcom = " & Firmware-linaro"
LICENSE:${PN}-qcom-qcs6490-thundercomm-rubikpi3-audio:append:qcom = " & Firmware-linaro"

FILES:${PN}-qcom-sm8750-audio:append:qcom = " \
    ${nonarch_base_libdir}/firmware/qcom/sm8750/SM8750-MTP-tplg.bin* \
    ${nonarch_base_libdir}/firmware/qcom/sm8750/SM8750-QRD-tplg.bin* \
"
FILES:${PN}-qcom-qcm6490-audio:append:qcom = " \
    ${nonarch_base_libdir}/firmware/qcom/qcm6490/QCM6490-IDP-tplg.bin* \
"
FILES:${PN}-qcom-sm8450-audio-tplg:qcom = " \
    ${nonarch_base_libdir}/firmware/qcom/sm8450/SM8450-HDK-tplg.bin* \
"
FILES:${PN}-qcom-qcs615-audio:append:qcom = " \
    ${nonarch_base_libdir}/firmware/qcom/qcs615/TALOS-EVK-tplg.bin* \
"
FILES:${PN}-qcom-qcs6490-thundercomm-rubikpi3-audio:append:qcom = " \
    ${nonarch_base_libdir}/firmware/qcom/qcs6490/QCS6490-Thundercomm-RubikPi3-tplg.bin* \
    ${nonarch_base_libdir}/firmware/qcom/qcs6490/Thundercomm/RubikPi3/QCS6490-Thundercomm-RubikPi3-tplg.bin* \
"
FILES:${PN}-qcom-qcs6490-radxa-dragon-q6a-audio:append:qcom = " \
    ${nonarch_base_libdir}/firmware/qcom/qcs6490/radxa/dragon-q6a/QCS6490-Radxa-Dragon-Q6A-tplg.bin* \
"
FILES:${PN}-qcom-x1e80100-audio:append:qcom = " \
    ${nonarch_base_libdir}/firmware/qcom/x1e80100/X1E80100-TUXEDO-Elite-14-tplg.bin* \
    ${nonarch_base_libdir}/firmware/qcom/x1e80100/X1E80100-Dell-Latitude-7455-tplg.bin* \
    ${nonarch_base_libdir}/firmware/qcom/x1e80100/X1E80100-ASUS-Vivobook-S15-tplg.bin* \
    ${nonarch_base_libdir}/firmware/qcom/x1e80100/X1E80100-Dell-XPS-13-9345-tplg.bin* \
    ${nonarch_base_libdir}/firmware/qcom/x1e80100/X1E80100-Romulus-tplg.bin* \
    ${nonarch_base_libdir}/firmware/qcom/x1e80100/X1E80100-Dell-Inspiron-14p-7441-tplg.bin* \
    ${nonarch_base_libdir}/firmware/qcom/x1e80100/X1E80100-ASUS-Vivobook-16-tplg.bin* \
    ${nonarch_base_libdir}/firmware/qcom/x1e80100/X1E80100-HP-OMNIBOOK-X14-tplg.bin* \
    ${nonarch_base_libdir}/firmware/qcom/x1e80100/X1E001DE-DEVKIT-tplg.bin* \
    ${nonarch_base_libdir}/firmware/qcom/x1e80100/X1E80100-CRD-tplg.bin* \
    ${nonarch_base_libdir}/firmware/qcom/x1e80100/X1E80100-ASUS-Zenbook-A14-tplg.bin* \
    ${nonarch_base_libdir}/firmware/qcom/x1e80100/ASUSTeK/vivobook-16/X1E80100-ASUS-Vivobook-16-tplg.bin* \
    ${nonarch_base_libdir}/firmware/qcom/x1e80100/ASUSTeK/vivobook-s15/X1E80100-ASUS-Vivobook-S15-tplg.bin* \
    ${nonarch_base_libdir}/firmware/qcom/x1e80100/ASUSTeK/zenbook-a14/X1E80100-ASUS-Zenbook-A14-tplg.bin* \
    ${nonarch_base_libdir}/firmware/qcom/x1e80100/hp/omnibook-x14/X1E80100-HP-OMNIBOOK-X14-tplg.bin* \
    ${nonarch_base_libdir}/firmware/qcom/x1e80100/dell/inspiron-14-plus-7441/X1E80100-Dell-Inspiron-14p-7441-tplg.bin* \
    ${nonarch_base_libdir}/firmware/qcom/x1e80100/dell/latitude-7455/X1E80100-Dell-Latitude-7455-tplg.bin* \
    ${nonarch_base_libdir}/firmware/qcom/x1e80100/dell/xps13-9345/X1E80100-Dell-XPS-13-9345-tplg.bin* \
"
RDEPENDS:${PN}-qcom-sm8450-audio-tplg:qcom = "${PN}-linaro-license"
RDEPENDS:${PN}-qcom-sm8750-audio:append:qcom = " ${PN}-linaro-license"
RDEPENDS:${PN}-qcom-qcs615-audio:append:qcom = " ${PN}-linaro-license"
RDEPENDS:${PN}-qcom-qcs6490-thundercomm-rubikpi3-audio:append:qcom = " ${PN}-linaro-license"

inherit ${ALTERNATIVES_CLASS}

# firmware-ath6kl provides updated bdata.bin, which can not be accepted into main linux-firmware repo
ALTERNATIVE:${PN}-ath6k:qcom = "ar6004-hw13-bdata"
ALTERNATIVE_LINK_NAME[ar6004-hw13-bdata] = "${nonarch_base_libdir}/firmware/ath6k/AR6004/hw1.3/bdata.bin${@fw_compr_file_suffix(d)}"
