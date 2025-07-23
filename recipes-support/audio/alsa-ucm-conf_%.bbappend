FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:qcom = " \
    file://0001-ucm2-Qualcomm-Update-the-QCM6490-and-QCS6490-hifi-co.patch \
    file://0002-ucm2-Qualcomm-Update-the-HIFI-enable-mixer-commands-.patch \
"
