FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:qcom = " \
    file://0001-Add-SELinux-policy-for-nhx.sh.patch \
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', '', 'file://0002-Enable-the-tunable-flag-tee_supplicant_qtee.patch', d)} \
    file://0003-Allow-network-manager-to-use-bpf-for-IPv4-collision-.patch \
"
