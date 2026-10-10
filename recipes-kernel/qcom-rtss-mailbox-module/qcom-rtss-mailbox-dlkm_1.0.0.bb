SUMMARY = "Qualcomm RTSS Mailbox Kernel Driver Module"
DESCRIPTION = "Dynamically Loadable Kernel Module (DLKM) for RTSS mailbox communication. \
Provides mailbox IPC communication between APSS and RTSS. \
NOTICE / LIMITATIONS: this driver is still being upstreamed and its uAPI \
(rtss_mailbox_uapi.h) is not yet frozen and may change before upstream \
acceptance - see rtss-mailbox-kmd's README for details."

require qcom-rtss-mailbox-module-common.inc

inherit module useradd

COMPATIBLE_MACHINE = "qcs8300|qcs9100|qcom-armv8a"

SRC_URI += "file://rtssmb.rules"

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "-r rtssmb"

do_install:append() {
    install -m 0644 ${UNPACKDIR}/rtssmb.rules -D ${D}${nonarch_base_libdir}/udev/rules.d/rtssmb.rules
}

FILES:${PN} += "${nonarch_base_libdir}/udev/rules.d"
