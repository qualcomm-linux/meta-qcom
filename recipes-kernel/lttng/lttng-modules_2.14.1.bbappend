python __anonymous() {
    bb.warn("meta-qcom bbappend for lttng-modules is active")
}

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"


SRC_URI += " \
    file://0001-ring-buffer-clarify-requirement-for-disabled-page-fa.patch \
    file://0002-Fix-Protect-syscall-probes-with-preemption-disable.patch \
    file://0003-Fix-Add-wrapper-for-get_pfnblock_migatetype.patch \
	file://0004-fix-remove-duplicated-MODULE-macros.patch \
	file://0005-fix-get_pfnblock_flags_mask-get_pfnblock_migratetype.patch \
	file://0006-fix-always-print-to-kernel-log-on-module-load-failur.patch \
"

