FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:qcom = " \
    file://0001-Add-SELinux-policy-for-camera-nhx.patch \
    file://0003-seatd-allow-self-fifo_file-read-write-for-signal-han.patch \
    file://0004-kernel-allow-module-loaders-to-use-net_admin.patch \
    file://0005-refpolicy-Allow-qtee_supplicant-to-access-the-eMMC-R.patch \
    file://0006-refpolicy-Add-SELinux-policy-for-tqftpserv.patch \
"

# Qualcomm platforms preload qrtr and qrtr_smd from modules-load.d.
POLICY_BOOLEANS:append:qcom = " \
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', '', 'tee_supplicant_qtee=true', d)} \
    kernel_module_load_net_admin=true \
"
