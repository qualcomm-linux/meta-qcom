FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:qcom = " \
    file://0001-Add-SELinux-policy-for-camera-nhx.patch \
"

# Qualcomm platforms preload qrtr and qrtr_smd from modules-load.d.
POLICY_BOOLEANS:append:qcom = " \
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', '', 'tee_supplicant_qtee=true', d)} \
    kernel_module_load_net_admin=true \
"
