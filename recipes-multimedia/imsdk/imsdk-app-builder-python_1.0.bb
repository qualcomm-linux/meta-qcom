require gst-plugins-imsdk-common.inc

SUMMARY = "Qualcomm IMSDK Python App builder"
DESCRIPTION = "Open-source Qualcomm IMSDK Python App builder"

PACKAGECONFIG ??= "app-builder-python"

FILES:${PN} += " \
    ${PYTHON_SITEPACKAGES_DIR}/qimsdk \
    ${bindir}/qimsdk_test_* \
    ${sysconfdir}/imsdk \
    ${sysconfdir}/imsdk/* \
"

RDEPENDS:${PN} += "gstreamer1.0-python python3-pyyaml"
