FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://0001-Add-SELinux-policy-for-nhx.sh.patch \
"
