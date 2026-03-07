FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI:append:qcom = " \
    file://0001-ucm2-Qualcomm-sa8775p-Remove-Fixed-channel-setting-f.patch \
    file://0002-ucm2-Qualcomm-qcs8300-Remove-Fixed-channel-setting-f.patch \
    file://0001-Qualcomm-qcs615-Remove-JackControl-from-TALOS-EVK-Hi.patch \
"
