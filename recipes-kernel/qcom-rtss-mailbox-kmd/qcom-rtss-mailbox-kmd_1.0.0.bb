# SPDX-License-Identifier: GPL-2.0-only

SUMMARY = "QCOM RTSS Mailbox Kernel Driver Module"
DESCRIPTION = "Dynamically Loadable Kernel Module (DLKM) for RTSS mailbox communication. \
Provides mailbox IPC communication between APSS and RTSS."
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

PV = "1.0.0"

SRC_URI = "git://github.com/qualcomm-linux/rtss-mailbox-kmd.git;branch=rtss-mailbox-kernel.le.0.0;protocol=https;tag=v${PV}"
SRCREV  = "74f4ec17e45a65e455c2bff33638b5c5a7c507e0"

inherit module

RPROVIDES:${PN} += "kernel-module-rtss-mailbox"

EXTRA_OEMAKE += "MACHINE='${MACHINE}'"
MAKE_TARGETS = "modules"
MODULES_INSTALL_TARGET = "modules_install"

# qcom-ipcc (CONFIG_QCOM_IPCC=y) is built into the kernel image, not a DLKM,
# so no RDEPENDS on a qcom-ipcc package is needed.
# The softdep below documents the runtime dependency and ensures modprobe
# loads rtss_mailbox only after the MAILBOX framework is ready.
KERNEL_MODULE_PROBECONF += "rtss_mailbox"
module_conf_rtss_mailbox = "softdep rtss_mailbox pre: qcom_ipcc"

# Install the UAPI header into the sysroot so the UMD build can consume it.
# DEPENDS = "qcom-rtss-mailbox-kmd" in qcom-rtss-mailbox-umd.bb pulls this in first.
do_install:append() {
    install -d ${D}${includedir}
    install -m 0644 ${S}/include/uapi/rtss_mailbox_uapi.h ${D}${includedir}/
}

