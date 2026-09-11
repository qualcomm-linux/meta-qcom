# To apply patches to support Qualcomm specific formats and fixes

FILESEXTRAPATHS:prepend:qcom := "${THISDIR}/${BPN}:"

SRC_URI:append:qcom = " \
    file://0001-v4l2-Add-support-for-V4L2_PIX_FMT_QC08C-format.patch \
    file://0002-v4l2videoenc-Set-format-on-capture-queue-before-enco.patch \
    file://0004-v4l2videodec-Prefer-colorimetry-from-acquired-caps.patch \
    file://0006-v4l2object-providing-aligned-size-when-propose-alloc.patch \
    file://0007-v4l2-Drop-empty-bytesused-0-buffers.patch \
    file://0008-v4l2-Handle-GAP-buffer-in-encoder.patch \
    file://0010-v4l2-Add-support-for-V4L2_PIX_FMT_QC10C-format.patch \
    file://0011-pulse-NULL-check-before-de-reference.patch \
    file://0012-pulse-add-pulsedirectsink-element.patch \
"
