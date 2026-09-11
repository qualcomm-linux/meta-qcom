SUMMARY = "Qualcomm RTSS Mailbox UAPI headers"
DESCRIPTION = "IOCTL/struct definitions shared between the RTSS mailbox kernel \
driver (rtss-mailbox-kmd) and userspace (rtss-mailbox-umd). Packaged \
standalone so userspace can build against the UAPI without depending on \
the kernel module recipe. \
NOTICE / LIMITATIONS: this uAPI is not yet frozen and may change before \
upstream acceptance - see rtss-mailbox-kmd's README for details."

require qcom-rtss-mailbox-module-common.inc

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    oe_runmake headers_install INSTALL_HDR_PATH=${D}${exec_prefix}
}
