FILESEXTRAPATHS:append:qcom := "${THISDIR}/files:"

PACKAGECONFIG:append:qcom = " qrtr"

DEPENDS:append:qcom = " json-glib"

SRC_URI:append:qcom = " \
    file://0001-iface-modem-messaging-sms-Add-TA-storage-support-for.patch \
    file://0002-fixup-move-json-glib-dep-to-root-meson.build-add-to-.patch \
    file://0003-whitespace-cleanup.patch \
    file://0004-whitespace-cleanup-fix-error-freeing.patch \
    file://0005-remove-dead-code.patch \
    file://0006-qmi-error-free-fixup.patch \
    file://0007-whitespace-fixes-some-memory-leak-fixes.patch \
    file://0008-whitespace-fixes-and-adjust-some-sms-storage-functio.patch \
    file://0009-sms-storage-do-hex-binary-conversion-in-sms-storage.patch \
    file://0010-sms-storage-Add-slot-info-for-TA-SMS-in-DB.patch \
    file://0001-qcom-soc-add-QRTR-MHI-based-modem-support.patch \
    file://0001-port-qmi-add-BAM-DMUX-DPM-support-and-fix-QRTR-WDA.patch \
    file://0002-base-modem-allow-QMI-modem-creation-without-net-port.patch \
    file://0003-bearer-qmi-use-BindMuxDataPort-for-BAM-DMUX-WDS-client.patch \
    file://0004-plugins-qcom-soc-send-DPM-open-port-during-enabling.patch \
    file://0005-plugins-qcom-soc-replace-sio_port_per_port_number.patch \
    file://0006-bearer-qmi-use-per-bearer-mux-id-for-MPDP.patch \
    file://0007-port-qmi-enable-mux-rmnet-and-bam-dmux-dl-aggregation.patch \
    file://0008-netlink-clear-socket-on-passcred-failure.patch \
    file://0009-qcom-soc-share-primary-qmi-port-for-mpdp.patch \
"
