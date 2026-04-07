require gst-plugins-imsdk-common.inc
require gst-plugins-imsdk-packaging.inc

SUMMARY = "Qualcomm IMSDK GStreamer Plugins (Proprietary)"
DESCRIPTION = "Proprietary Qualcomm IMSDK GStreamer plugins including camera, ML/AI (QNN/SNPE), and smart video encoder"

DEPENDS += "gst-plugins-imsdk-base"

PACKAGECONFIG ??= "qairt smartvencbin"
