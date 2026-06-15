FILESEXTRAPATHS:prepend:qcom := "${THISDIR}/bluez5:"

SRC_URI:append:qcom = " \
    file://0001-shared-rap-Introduce-Channel-Sounding-HCI-raw-interf.patch \
    file://0002-main-conf-Add-Channel-Sounding-config-parsing-support.patch \
    file://0003-profiles-ranging-Add-HCI-LE-Event-Handling-in-Reflec.patch \
    file://0004-shared-util-Add-MIN-MAX-implementations.patch \
    file://0005-rap-Cleanup-coding-style-and-unnecessary-code.patch \
    file://0006-src-shared-Add-custom-CCC-callbacks.patch \
    file://0007-src-shared-Add-RAS-packet-format-and-notification-su.patch \
    file://0008-unit-test-rap-Add-PTS-tests-for-CS-reflector.patch \
    file://0009-profiles-ranging-Read-cs_mode_one_data-members.patch \
    file://0010-shared-hci-Add-BPF-filter-for-registered-events.patch \
    file://0011-shared-hci-Add-bt_hci_register_subevent-for-LE-Meta-.patch \
    file://0012-ranging-rap_hci-Use-bt_hci_register_subevent-for-LE-.patch \
    file://0013-test-rap-Fix-gatt_ccc_read_cb-on-big-endian.patch \
    file://0014-shared-rap-fix-use-of-uninitialized-value.patch \
    file://0015-shared-rap-Add-client-ranging-registration-and-notif.patch \
    file://0016-profiles-ranging-Fix-measured_freq_offset.patch \
"
