require gst-plugins-imsdk-packaging.inc

SUMMARY = "Qualcomm IMSDK GStreamer Plugins (QTI OSS)"
DESCRIPTION = "Open-source Qualcomm IMSDK GStreamer multimedia, CV, ML, and messaging plugins"
SECTION = "multimedia"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2998c54c288b081076c9af987bdf4838"

inherit cmake features_check pkgconfig

REQUIRED_DISTRO_FEATURES = "opengl"

SRC_URI = "git://github.com/qualcomm/gst-plugins-imsdk;branch=main;protocol=https"

SRCREV = "268dc4daa7fb3813bdfd481ff15e38f8d844105c"
PV = "0.0+git"

DEPENDS += "\
    gstreamer1.0 \
    gstreamer1.0-plugins-base \
    virtual/egl \
    virtual/libgbm \
    virtual/libgles2 \
    virtual/libgles3 \
    gobject-introspection \
"

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

PACKAGECONFIG ??= "python-examples sw videoproc"

PACKAGECONFIG[messaging]    = "-DENABLE_GST_MESSAGING_PLUGINS=1, -DENABLE_GST_MESSAGING_PLUGINS=0, librdkafka mosquitto"
PACKAGECONFIG[ml]           = "-DENABLE_GST_ML_PLUGINS=1, -DENABLE_GST_ML_PLUGINS=0, cairo json-glib opencv qairt-sdk, qairt-sdk"
PACKAGECONFIG[python-examples] = "-DENABLE_GST_PYTHON_EXAMPLES=1, -DENABLE_GST_PYTHON_EXAMPLES=0"
PACKAGECONFIG[qmmfsrc]      = "-DENABLE_GST_PLUGIN_QMMFSRC=1  -DVHDR_MODES_ENABLE=ON, -DENABLE_GST_PLUGIN_QMMFSRC=0 -DVHDR_MODES_ENABLE=OFF, camera-service"
PACKAGECONFIG[redissink]    = "-DENABLE_GST_PLUGIN_REDISSINK=1, -DENABLE_GST_PLUGIN_REDISSINK=0, hiredis"
PACKAGECONFIG[sample-apps]  = "-DENABLE_GST_SAMPLE_APPS=1, -DENABLE_GST_SAMPLE_APPS=0, opencv cairo json-glib camera-service"
PACKAGECONFIG[sw]           = "-DENABLE_GST_SOFTWARE_PLUGINS=1, -DENABLE_GST_SOFTWARE_PLUGINS=0, gstreamer1.0-rtsp-server smart-venc-ctrl-algo"
PACKAGECONFIG[tflite]       = "-DENABLE_GST_PLUGIN_MLTFLITE=1, -DENABLE_GST_PLUGIN_MLTFLITE=0, tensorflow-lite"
PACKAGECONFIG[videoproc]    = "-DENABLE_GST_VIDEOPROC_PLUGINS=1, -DENABLE_GST_VIDEOPROC_PLUGINS=0, cairo"

INSTALL_MEDIA = "${sysconfdir}/media"

do_install:append() {
    mkdir -p ${D}${bindir}
    mkdir -p ${D}${INSTALL_MEDIA}
    install -m 755 ${S}/gst-python-examples/files/Qdemo     ${D}${bindir}/
    install -m 755 ${S}/gst-python-examples/files/Qdemo.png ${D}${INSTALL_MEDIA}
    install -m 755 ${S}/gst-python-examples/files/Qdemo.gif ${D}${INSTALL_MEDIA}
}

PACKAGES =+ "${PN}-qdemo"

FILES:${PN}-qdemo = " \
    ${bindir}/Qdemo \
    ${INSTALL_MEDIA}/Qdemo.png \
    ${INSTALL_MEDIA}/Qdemo.gif \
"
# The TFLite plugin loads the TensorFlow Lite library at runtime using dlopen(). To ensure runtime availability, added runtime dependency on 'tensorflow-lite'.
RDEPENDS:${PN}-qtimltflite += "tensorflow-lite"

RDEPENDS:${PN}-apps += "\
    python3-core \
    gstreamer1.0-python \
    python3-pygobject \
    gtk+3 \
    python3-opencv \
"

RDEPENDS:${PN}-qdemo += "bash"

RDEPENDS:${PN} += "${PN}-qdemo"
