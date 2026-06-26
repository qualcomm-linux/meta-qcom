PACKAGECONFIG:append = " builder-py"

FILES:${PN} += " \
    ${PYTHON_SITEPACKAGES_DIR}/qimsdk \
    ${bindir}/qimsdk_* \
    ${sysconfdir}/qimsdk \
    ${sysconfdir}/qimsdk/* \
"

RDEPENDS:${PN} += "gstreamer1.0-python python3-pyyaml"
