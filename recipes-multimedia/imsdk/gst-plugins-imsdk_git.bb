require gst-plugins-imsdk-packaging.inc

SUMMARY = "Qualcomm IMSDK GStreamer Plugins (QTI OSS)"
DESCRIPTION = "Open-source Qualcomm IMSDK GStreamer multimedia, CV, ML, and messaging plugins"
SECTION = "multimedia"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2998c54c288b081076c9af987bdf4838"

inherit cmake features_check pkgconfig

REQUIRED_DISTRO_FEATURES = "opengl"

SRC_URI = "git://github.com/qualcomm/gst-plugins-imsdk;branch=main;protocol=https"

SRCREV = "6ad888f79403ce91092dd49eed3dc3b4242957ca"
PV = "0.0+git"

DEPENDS += "\
    gstreamer1.0 \
    gstreamer1.0-plugins-base \
    virtual/egl \
    virtual/libgbm \
    virtual/libgles2 \
    virtual/libgles3 \
"

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

PACKAGECONFIG ??= "sw videoproc"

PACKAGECONFIG[messaging]    = "-DENABLE_GST_MESSAGING_PLUGINS=1, -DENABLE_GST_MESSAGING_PLUGINS=0, librdkafka mosquitto"
PACKAGECONFIG[ml]           = "-DENABLE_GST_ML_PLUGINS=1, -DENABLE_GST_ML_PLUGINS=0, cairo json-glib opencv qairt-sdk, qairt-sdk"
PACKAGECONFIG[redissink]    = "-DENABLE_GST_PLUGIN_REDISSINK=1, -DENABLE_GST_PLUGIN_REDISSINK=0, hiredis"
PACKAGECONFIG[sw]           = "-DENABLE_GST_SOFTWARE_PLUGINS=1, -DENABLE_GST_SOFTWARE_PLUGINS=0, gstreamer1.0-rtsp-server smart-venc-ctrl-algo"
PACKAGECONFIG[videoproc]    = "-DENABLE_GST_VIDEOPROC_PLUGINS=1, -DENABLE_GST_VIDEOPROC_PLUGINS=0, cairo"
