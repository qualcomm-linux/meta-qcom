require gst-plugins-imsdk-common.inc

SUMMARY = "Qualcomm IMSDK C++ App builder"
DESCRIPTION = "Open-source Qualcomm IMSDK C++ App builder"

DEPENDS += "gst-plugins-imsdk-base"

PACKAGECONFIG ??= "app-builder-cpp"

FILES:${PN} += " \
    ${libdir}/libqtiimsdk.so* \
    ${bindir}/qimsdk_test_* \
    ${sysconfdir}/imsdk \
    ${sysconfdir}/imsdk/* \
"
