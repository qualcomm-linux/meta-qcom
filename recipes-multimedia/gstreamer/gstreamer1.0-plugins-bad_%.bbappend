# To apply patches to support Qualcomm specific formats and fixes

FILESEXTRAPATHS:prepend:qcom := "${THISDIR}/${BPN}:"

SRC_URI:append:qcom = " \
    file://0001-wayland-Add-support-for-NV12_Q08C-compressed-8-bit-f.patch \
    file://0002-waylandsink-Release-pending-buffers-during-PAUSED-to.patch \
    file://0003-waylandsink-support-gap-buffers.patch \
    file://0004-waylandsink-make-sure-self-window-is-not-NULL-before.patch \
"