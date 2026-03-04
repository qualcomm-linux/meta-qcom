FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:qcom = " \
    file://0001-Qualcomm-qcs615-Remove-JackControl-from-TALOS-EVK-Hi.patch \
"
