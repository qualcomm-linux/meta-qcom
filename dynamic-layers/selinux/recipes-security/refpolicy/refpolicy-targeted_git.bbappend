FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:qcom = " \
    file://0001-Add-SELinux-policy-for-nhx.sh.patch \
    file://0001-sepolicy-allow-tpm2-abrmd-communication-over-D-Bus.patch \
"
