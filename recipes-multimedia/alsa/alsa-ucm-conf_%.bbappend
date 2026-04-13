FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI:append:qcom = " \
    file://0001-Qualcomm-glymur-Add-GLYMUR-CRD-HiFi-config.patch \
"
