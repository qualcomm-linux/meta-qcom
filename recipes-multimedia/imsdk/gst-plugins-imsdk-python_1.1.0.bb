require gst-plugins-imsdk-common.inc
require gst-plugins-imsdk-packaging.inc

SUMMARY = "Qualcomm IMSDK GStreamer Python Libraries"
DESCRIPTION = "Python binding overrides (complementing the bindings provided by python-gi) for IMSDK GStreamer libraries."

DEPENDS += "gst-plugins-imsdk-base python3-pygobject"

PACKAGECONFIG = "python"

REQUIRED_DISTRO_FEATURES += "gobject-introspection-data"

FILES:${PN}:append = " ${PYTHON_SITEPACKAGES_DIR}"
