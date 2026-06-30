SECTION = "kernel"

DESCRIPTION = "Linux ${PV} kernel for QCOM devices"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel cml1

COMPATIBLE_MACHINE = "(qcom)"

LINUX_QCOM_FIT_DTB_COMPATIBLE = "conf/machine/include/fit-dtb-compatible-linux-qcom.inc"

LINUX_VERSION ?= "6.18.30"

PV = "${LINUX_VERSION}"

FILESEXTRAPATHS:prepend := "${THISDIR}/linux-qcom-6.18:"

# tag:qcom-6.18.y-20260626
SRCREV ?= "8c49474603c0b1c278b8fe00ac4e735b92d78ce9"

SRCBRANCH ?= "nobranch=1"
SRCBRANCH:class-devupstream ?= "branch=qcom-6.18.y"

SRC_URI = " \
    git://github.com/qualcomm-linux/kernel.git;${SRCBRANCH};protocol=https \
    file://0001-tools-use-basename-to-identify-file-in-gen-mach-type.patch \
"

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

# M.2 BT patches (FROMLIST: wcn6855/QCC2072 lemans-evk, base qcom-6.18.y-20260626)
SRC_URI += " \
    file://m2-bt/0001-dt-bindings-connector-Add-PCIe-M.2-Mechanical-Key-M-.patch \
    file://m2-bt/0002-power-sequencing-Add-the-Power-Sequencing-driver-for.patch \
    file://m2-bt/0003-PCI-pwrctrl-Create-pwrctrl-device-if-graph-port-is-f.patch \
    file://m2-bt/0004-PCI-pwrctrl-Add-PCIe-M.2-connector-support.patch \
    file://m2-bt/0005-serdev-Convert-to_serdev_-helpers-to-macros-and-use-.patch \
    file://m2-bt/0006-serdev-Add-an-API-to-find-the-serdev-controller-asso.patch \
    file://m2-bt/0007-serdev-Do-not-return-ENODEV-from-of_serdev_register_.patch \
    file://m2-bt/0008-dt-bindings-serial-Document-the-graph-port.patch \
    file://m2-bt/0009-dt-bindings-connector-Add-PCIe-M.2-Mechanical-Key-E-.patch \
    file://m2-bt/0010-power-sequencing-pcie-m2-Add-support-for-PCIe-M.2-Ke.patch \
    file://m2-bt/0011-power-sequencing-pcie-m2-Create-serdev-device-for-WC.patch \
    file://m2-bt/0012-power-sequencing-pcie-m2-enforce-PCI-and-OF-dependen.patch \
    file://m2-bt/0013-power-sequencing-pcie-m2-add-SERIAL_DEV_BUS-dependen.patch \
    file://m2-bt/0014-power-sequencing-pcie-m2-Fix-device-node-reference-l.patch \
    file://m2-bt/0015-arm64-defconfig-Enable-PCI-M.2-power-sequencing-driv.patch \
    file://m2-bt/0016-power-sequencing-pcie-m2-Fix-inconsistent-function-p.patch \
    file://m2-bt/0017-power-sequencing-pcie-m2-Allow-creating-serdev-for-m.patch \
    file://m2-bt/0018-power-sequencing-pcie-m2-Improve-PCI-device-ID-check.patch \
    file://m2-bt/0019-power-sequencing-pcie-m2-Create-serdev-for-PCI-devic.patch \
    file://m2-bt/0020-power-sequencing-pcie-m2-Create-BT-node-based-on-the.patch \
    file://m2-bt/0021-power-sequencing-Add-an-API-to-return-the-pwrseq-dev.patch \
    file://m2-bt/0022-Bluetooth-hci_qca-Add-M.2-Bluetooth-device-support-u.patch \
    file://m2-bt/0023-Bluetooth-hci_qca-Rename-power_ctrl_enabled-to-bt_en.patch \
    file://m2-bt/0024-Bluetooth-hci_qca-Set-bt_en_available-based-on-W_DIS.patch \
    file://m2-bt/0025-power-sequencing-pcie-m2-Add-0x1103-in-pwrseq_m2_pci.patch \
    file://m2-bt/0026-arm64-dts-qcom-lemans-evk-Describe-the-PCIe-M.2-Key-.patch \
    file://m2-bt/0027-power-sequencing-pcie-m2-Add-PCI-IDs-for-WCN6855-and.patch \
"
