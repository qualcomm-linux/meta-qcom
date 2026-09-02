FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:qcom = " \
    file://0001-libweston-avoid-duplicate-texture2D_swizzle-overload.patch \
    file://0001-libweston-inline-color-pipeline-and-wireframe-helper.patch \
    file://0001-compositor-skip-deferred-repaint-when-no-output-is-a.patch \
    file://0001-backend-drm-only-put-overlay-planes-on-the-per-output.patch \
"
