
FILESEXTRAPATHS:append := "${THISDIR}/${PN}:"

SRC_URI += " \
        file://0001-refpolicy-Add-SELinux-policy-for-qrtr-ns.service.patch \
        "
