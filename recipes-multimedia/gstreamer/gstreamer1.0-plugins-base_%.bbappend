# To apply patches to support Qualcomm specific formats and fixes

FILESEXTRAPATHS:prepend:qcom := "${THISDIR}/${BPN}:"

SRC_URI:append:qcom = " \
    file://0001-video-Add-support-for-NV12_Q08C-compressed-8-bit-for.patch \
    file://0002-video-Add-support-for-NV12_Q10LE32C-compressed-10-bi.patch \
    file://0003-videometa-Update-the-aggregation-logic-for-stride-al.patch \
"
