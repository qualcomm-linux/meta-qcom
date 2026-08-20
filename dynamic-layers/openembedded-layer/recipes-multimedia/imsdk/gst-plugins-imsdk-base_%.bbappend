PACKAGECONFIG:append = " builder-cpp"

FILES:${PN} += " \
    ${libdir}/libqimsdk-app-builder.so* \
    ${bindir}/qimsdk_* \
    ${sysconfdir}/qimsdk \
    ${sysconfdir}/qimsdk/* \
"
