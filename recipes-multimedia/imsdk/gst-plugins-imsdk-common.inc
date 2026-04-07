# Common configuration for IMSDK GStreamer plugins recipes
# This file contains shared settings used by base, OSS, and proprietary recipes

SECTION = "multimedia"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2998c54c288b081076c9af987bdf4838"

inherit cmake features_check pkgconfig

REQUIRED_DISTRO_FEATURES = "opengl"

SRC_URI = " \
    git://github.com/qualcomm/gst-plugins-imsdk;protocol=https;nobranch=1;tag=${PV} \
    file://0001-gst-plugins-drop-using-imported-include-dirs.patch \
"

SRCREV = "fd9af6b8377da7039ba23423f774287977fa60bc"

DEPENDS += "\
    gstreamer1.0 \
    gstreamer1.0-plugins-base \
"

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

PACKAGECONFIG[base]         = "-DENABLE_GST_PLUGIN_BASE=ON, -DENABLE_GST_PLUGIN_BASE=OFF, json-glib virtual/egl virtual/libgbm virtual/libgles2 virtual/libgles3"
PACKAGECONFIG[camera-base]  = "-DENABLE_GST_PLUGIN_CAMERA_BASE=ON, -DENABLE_GST_PLUGIN_CAMERA_BASE=OFF, camera-service"
PACKAGECONFIG[messaging]    = "-DENABLE_GST_MESSAGING_PLUGINS=1, -DENABLE_GST_MESSAGING_PLUGINS=0, librdkafka mosquitto"
PACKAGECONFIG[ml]           = "-DENABLE_GST_ML_PLUGINS=1, -DENABLE_GST_ML_PLUGINS=0, cairo json-glib opencv"
PACKAGECONFIG[qairt]        = "-DENABLE_GST_QAIRT_PLUGINS=1 -DENABLE_GST_PLUGIN_MLTOOLS=1, -DENABLE_GST_QAIRT_PLUGINS=0 -DENABLE_GST_PLUGIN_MLTOOLS=0, qairt-sdk, qairt-sdk"
PACKAGECONFIG[qmmfsrc]      = "-DENABLE_GST_PLUGIN_QMMFSRC=1 -DVHDR_MODES_ENABLE=ON -DEIS_MODES_ENABLE=ON -DFEATURE_OFFLINE_IFE_SUPPORT=ON, -DENABLE_GST_PLUGIN_QMMFSRC=0 -DVHDR_MODES_ENABLE=OFF -DEIS_MODES_ENABLE=OFF -DFEATURE_OFFLINE_IFE_SUPPORT=OFF, camera-service"
PACKAGECONFIG[redissink]    = "-DENABLE_GST_PLUGIN_REDISSINK=1, -DENABLE_GST_PLUGIN_REDISSINK=0, hiredis"
PACKAGECONFIG[sample-apps]  = "-DENABLE_GST_SAMPLE_APPS=1, -DENABLE_GST_SAMPLE_APPS=0, opencv cairo json-glib camera-service"
PACKAGECONFIG[smartvencbin] = "-DENABLE_GST_PLUGIN_SMARTVENCBIN=1, -DENABLE_GST_PLUGIN_SMARTVENCBIN=0, smart-venc-ctrl-algo"
PACKAGECONFIG[sw]           = "-DENABLE_GST_SOFTWARE_PLUGINS=1, -DENABLE_GST_SOFTWARE_PLUGINS=0, gstreamer1.0-rtsp-server"
PACKAGECONFIG[tflite]       = "-DENABLE_GST_PLUGIN_MLTFLITE=1, -DENABLE_GST_PLUGIN_MLTFLITE=0, tensorflow-lite"
PACKAGECONFIG[tools]        = "-DENABLE_GST_PLUGIN_TOOLS=1, -DENABLE_GST_PLUGIN_TOOLS=0, gstreamer1.0-rtsp-server"
PACKAGECONFIG[videoproc]    = "-DENABLE_GST_VIDEOPROC_PLUGINS=1, -DENABLE_GST_VIDEOPROC_PLUGINS=0, cairo"

# The TFLite plugin loads the TensorFlow Lite library at runtime using dlopen(). To ensure runtime availability, added runtime dependency on 'tensorflow-lite'.
RDEPENDS:${PN}-qtimltflite += "tensorflow-lite"

# The Smart Video Encoder plugin loads the libVideoCtrl library at runtime using dlopen(). To ensure runtime availability, added runtime dependency on 'smart-venc-ctrl-algo'.
RDEPENDS:${PN}-qtismartvencbin += "smart-venc-ctrl-algo"

# The MQTT adaptor loads the mosquitto library at runtime using dlopen(). MQTT server also needed to exercise the use-case.
# To ensure runtime availability, added runtime dependency on 'mosquitto'.
RDEPENDS:libgstqtimqttadaptor += "mosquitto"
