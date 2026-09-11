SUMMARY = "Qualcomm RTSS Mailbox Kernel Driver Module"
DESCRIPTION = "Dynamically Loadable Kernel Module (DLKM) for RTSS mailbox communication. \
Provides mailbox IPC communication between APSS and RTSS. \
NOTICE / LIMITATIONS: this driver is still being upstreamed and its uAPI \
(rtss_mailbox_uapi.h) is not yet frozen and may change before upstream \
acceptance - see rtss-mailbox-kmd's README for details."

require qcom-rtss-mailbox-module-common.inc

inherit module
