FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI:append:qcom = " \
    file://0001-Introduce-Qualcomm-PDS-service-support.patch \
"
