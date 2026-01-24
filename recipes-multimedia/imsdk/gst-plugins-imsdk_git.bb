SUMMARY = "Qualcomm IMSDK GStreamer Plugins (QTI OSS)"
DESCRIPTION = "Open-source Qualcomm IMSDK GStreamer multimedia, CV, ML, and messaging plugins"
SECTION = "multimedia"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=223037c4be0bfc6cf757035432adf983"

inherit cmake pkgconfig features_check

REQUIRED_DISTRO_FEATURES = "opengl"

SRC_URI = "git://github.com/qualcomm/gst-plugins-imsdk;branch=main;protocol=https"

SRCREV = "93610673ea88033c0b0d46ada93a55a24d9f3b75"
PV = "0.0+git"

DEPENDS += "\
    gstreamer1.0 \
    gstreamer1.0-plugins-base \
    qcom-fastcv-binaries \
    virtual/libgbm \
"

PACKAGECONFIG ??= "videoproc sw"

# The name ext below, indicates that the package depends on recipes from external meta layer other than current meta layer or its dependencies.
PACKAGECONFIG[ext-sw]       = "-DENABLE_GST_EXT_SOFTWARE_PLUGINS=1, -DENABLE_GST_EXT_SOFTWARE_PLUGINS=0, hiredis"
PACKAGECONFIG[ml]           = "-DENABLE_GST_ML_PLUGINS=1, -DENABLE_GST_ML_PLUGINS=0, cairo json-glib opencv"
PACKAGECONFIG[msgbroker]    = "-DENABLE_GST_PLUGIN_MSGBROKER=1, -DENABLE_GST_PLUGIN_MSGBROKER=0, librdkafka mosquitto"
PACKAGECONFIG[sw]           = "-DENABLE_GST_SOFTWARE_PLUGINS=1, -DENABLE_GST_SOFTWARE_PLUGINS=0, gstreamer1.0-rtsp-server smart-venc-ctrl-algo"
PACKAGECONFIG[videoproc]    = "-DENABLE_GST_VIDEOPROC_PLUGINS=1, -DENABLE_GST_VIDEOPROC_PLUGINS=0, cairo"

# fcv backend from common library (present in gst-plugin-base) does dlopen()s FastCV libs
RDEPENDS:${PN} += "\
    qcom-fastcv-binaries \
"

PACKAGES =+ "${PN}-apps"

FILES:${PN} += "${libdir}/gstreamer-1.0/*"
FILES:${PN}-apps = "${bindir}/*"
