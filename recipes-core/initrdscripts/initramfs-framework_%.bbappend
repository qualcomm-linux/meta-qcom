FILESEXTRAPATHS:prepend:qcom := "${THISDIR}/${PN}:"

SRC_URI:append:qcom = " \
    file://0001-initramfs-framework-add-opt-in-root-only-udev-trigg.patch \
"
