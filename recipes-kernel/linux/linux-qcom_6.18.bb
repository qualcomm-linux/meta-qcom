SECTION = "kernel"

DESCRIPTION = "Linux ${PV} kernel for QCOM devices"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel cml1

COMPATIBLE_MACHINE = "(qcom)"

LINUX_VERSION ?= "6.18.18"

PV = "${LINUX_VERSION}"

FILESEXTRAPATHS:prepend := "${THISDIR}/linux-qcom-6.18:"

# tag:qcom-6.18.y-20260403
SRCREV ?= "7ea01f6a3cff61f8e0535e6b018695d54d38202a"

SRCBRANCH ?= "nobranch=1"
SRCBRANCH:class-devupstream ?= "branch=qcom-6.18.y"

SRC_URI = "git://github.com/qualcomm-linux/kernel.git;${SRCBRANCH};protocol=https"
SRC_URI += "file://0001-tools-use-basename-to-identify-file-in-gen-mach-type.patch"
SRC_URI += "file://v1-0001-arm64-dts-qcom-lemans-Enable-DISPLAY-PORT.patch"
SRC_URI += "file://v1-0001-arm64-dts-qcom-Enable-secondary-mi2s.patch"
SRC_URI += "file://v1-0002-arm64-dts-qcom-qcs6490-Enable-DP-audio.patch"
SRC_URI += "file://v1-0003-ASoC-qcom-q6dsp-Update-bit-format-support-for-sec.patch"
SRC_URI += "file://v1-0004-ASoC-qcom-sc8280xp-don-t-force-S16_LE-in-hw_param.patch"

# Additional kernel configs.
SRC_URI += " \
    file://configs/bsp-additions.cfg \
"

# To build tip of qcom-6.18.y branch set preferred
# virtual/kernel provider to 'linux-qcom-6.18.y-upstream'
BBCLASSEXTEND = "devupstream:target"
PN:class-devupstream = "linux-qcom-6.18.y-upstream"
SRCREV:class-devupstream ?= "${AUTOREV}"

S = "${UNPACKDIR}/${BP}"

KBUILD_DEFCONFIG ?= "defconfig"
KBUILD_DEFCONFIG:qcom-armv7a = "qcom_defconfig"

KBUILD_CONFIG_EXTRA = "${@bb.utils.contains('DISTRO_FEATURES', 'hardened', '${S}/kernel/configs/hardening.config', '', d)}"
KBUILD_CONFIG_EXTRA:append:aarch64 = " ${S}/arch/arm64/configs/prune.config"
KBUILD_CONFIG_EXTRA:append:aarch64 = " ${S}/arch/arm64/configs/qcom.config"
KBUILD_CONFIG_EXTRA:append = " ${@oe.utils.vartrue('DEBUG_BUILD', '${S}/kernel/configs/debug.config', '', d)}"
KBUILD_CONFIG_EXTRA:append:aarch64 = " ${@oe.utils.vartrue('DEBUG_BUILD', '${S}/arch/arm64/configs/qcom_debug.config', '', d)}"

do_configure:prepend() {
    # Use a copy of the 'defconfig' from the actual repo to merge fragments
    cp ${S}/arch/${ARCH}/configs/${KBUILD_DEFCONFIG} ${B}/.config

    # Merge fragment for QCOM value add features
    ${S}/scripts/kconfig/merge_config.sh -m -O ${B} ${B}/.config ${KBUILD_CONFIG_EXTRA} ${@" ".join(find_cfgs(d))}
}
