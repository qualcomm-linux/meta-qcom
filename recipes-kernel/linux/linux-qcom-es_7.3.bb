SECTION = "kernel"

DESCRIPTION = "Linux ${PV} early sample kernel for QCOM devices"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel cml1

COMPATIBLE_MACHINE = "(nord|eliza)"

LINUX_QCOM_FIT_DTB_COMPATIBLE = "conf/machine/include/fit-dtb-compatible-linux-qcom.inc"

LINUX_VERSION ?= "7.3"

PV = "${LINUX_VERSION}+git"

# Share the config fragments with linux-qcom-next
FILESEXTRAPATHS:prepend := "${THISDIR}/linux-qcom-next:"

KERNEL_PAHOLE ?= '${@bb.utils.contains("BBFILE_COLLECTIONS", "openembedded-layer", "1", "0", d)}'
do_configure[depends] += '${@oe.utils.vartrue("KERNEL_PAHOLE", "pahole-native:do_populate_sysroot", "", d)}'
EXTRA_OEMAKE += '${@oe.utils.vartrue("KERNEL_PAHOLE", "", "PAHOLE=false", d)}'

# tag: qcom-7.3-es-20261127
SRCREV ?= "f95930c7fa1a7536a199b9cc2a06f6bf20d22f46"

SRCBRANCH ?= "nobranch=1"
SRCBRANCH:class-devupstream ?= "branch=qcom-7.3-es"

require linux-qcom-localversion.inc
KERNEL_LOCALVERSION ?= "${@qcom_kernel_localversion(d)}"

SRC_URI = " \
    git://github.com/qualcomm-linux/kernel.git;${SRCBRANCH};protocol=https \
"

# Additional kernel configs.
SRC_URI += " \
    file://configs/bsp-additions.cfg \
"

# To build tip of qcom-7.3-es branch set preferred
# virtual/kernel provider to 'linux-qcom-7.3-es-upstream'
BBCLASSEXTEND = "devupstream:target"
PN:class-devupstream = "linux-qcom-7.3-es-upstream"
SRCREV:class-devupstream ?= "${AUTOREV}"

S = "${UNPACKDIR}/${BP}"

KBUILD_DEFCONFIG ?= "defconfig"

KBUILD_CONFIG_EXTRA = "${S}/kernel/configs/hardening.config"
KBUILD_CONFIG_EXTRA:append = " ${S}/arch/arm64/configs/prune.config"
KBUILD_CONFIG_EXTRA:append = " ${S}/arch/arm64/configs/qcom.config"
KBUILD_CONFIG_EXTRA:append = " ${@oe.utils.vartrue('DEBUG_BUILD', '${S}/kernel/configs/debug.config', '', d)}"
KBUILD_CONFIG_EXTRA:append = " ${@oe.utils.vartrue('DEBUG_BUILD', '${S}/arch/arm64/configs/qcom_debug.config', '', d)}"

do_configure:prepend() {
    # Use a copy of the 'defconfig' from the actual repo to merge fragments
    cp ${S}/arch/${ARCH}/configs/${KBUILD_DEFCONFIG} ${B}/.config

    # Merge fragment for QCOM value add features
    ${S}/scripts/kconfig/merge_config.sh -m -O ${B} ${B}/.config ${KBUILD_CONFIG_EXTRA} ${@" ".join(find_cfgs(d))}
}
