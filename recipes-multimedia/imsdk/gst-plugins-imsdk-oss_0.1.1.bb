require gst-plugins-imsdk-common.inc
require gst-plugins-imsdk-packaging.inc

SUMMARY = "Qualcomm IMSDK GStreamer Plugins (OSS)"
DESCRIPTION = "Open-source Qualcomm IMSDK GStreamer multimedia plugins"

DEPENDS += "gst-plugins-imsdk-base"

PACKAGECONFIG ??= "sw tools videoproc"
