FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:qcom = " \
    file://0001-Add-SELinux-policy-for-nhx.sh.patch \
    file://0002-refpolicy-targeted-address-QLI-SELinux-denial-sweep.patch \
"
