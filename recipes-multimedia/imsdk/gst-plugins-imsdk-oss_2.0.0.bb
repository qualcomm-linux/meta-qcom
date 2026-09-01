require gst-plugins-imsdk-common.inc
require gst-plugins-imsdk-packaging.inc

SUMMARY = "Qualcomm IMSDK GStreamer Plugins (OSS)"
DESCRIPTION = "Open-source Qualcomm IMSDK GStreamer multimedia plugins"

DEPENDS += "gst-plugins-imsdk-base"

PACKAGECONFIG ??= "sw tools videoproc"

PACKAGES += "${PN}-media"

FILES:${PN}-media = " \
    ${datadir}/qdemo/* \
"

RDEPENDS:${PN}-apps += "${@bb.utils.contains('PACKAGECONFIG', 'python-apps', '${PN}-media', '', d)}"
RDEPENDS:${PN}-dev += "${@bb.utils.contains('PACKAGECONFIG', 'ml', 'libeigen-dev', '', d)}"
