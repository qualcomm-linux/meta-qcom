SECTION = "kernel"

DESCRIPTION = "Linux ${PV} kernel for QCOM devices"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel cml1

COMPATIBLE_MACHINE = "(qcom)"

LINUX_QCOM_FIT_DTB_COMPATIBLE = "conf/machine/include/fit-dtb-compatible-linux-qcom.inc"

LINUX_VERSION ?= "7.3"

PV = "${LINUX_VERSION}+git"

FILESEXTRAPATHS:prepend := "${THISDIR}/linux-qcom-next:"

KERNEL_PAHOLE ?= '${@bb.utils.contains("BBFILE_COLLECTIONS", "openembedded-layer", "1", "0", d)}'
do_configure[depends] += '${@oe.utils.vartrue("KERNEL_PAHOLE", "pahole-native:do_populate_sysroot", "", d)}'
EXTRA_OEMAKE += '${@oe.utils.vartrue("KERNEL_PAHOLE", "", "PAHOLE=false", d)}'

# tag: qcom-7.3-20261111
SRCREV ?= "bda3031521c448a211b844dbf21f223f7433f83f"

SRCBRANCH ?= "nobranch=1"
SRCBRANCH:class-devupstream ?= "branch=qcom-next"

require linux-qcom-localversion.inc
KERNEL_LOCALVERSION ?= "${@qcom_kernel_localversion(d)}"

SRC_URI = "git://github.com/qualcomm-linux/kernel.git;${SRCBRANCH};protocol=https"

# Additional kernel configs.
SRC_URI += " \
    file://configs/bsp-additions.cfg \
    file:0001-dt-bindings-phy-qcom-qmp-usb3-dp-Extend-X1E-descript.patch \
    file:0002-phy-core-Define-TBT-phy_mode.patch \
    file:0003-arm64-dts-qcom-hamoa-Extend-QMPPHY-description-for-U.patch \
    file:0004-dt-bindings-thunderbolt-Add-Qualcomm-USB4-Host-Route.patch \
    file:0005-usb-typec-thunderbolt-Fix-Thunderbolt-adapter-type-b.patch \
    file:0006-phy-qualcomm-qmp-combo-Add-USB4-TBT3-configuration-d.patch \
    file:0007-phy-qualcomm-qmp-combo-Add-preliminary-USB4-support.patch \
    file:0008-phy-qualcomm-qmp-combo-Add-Glymur-USB4v1-PHY-sequenc.patch \
    file:0009-soc-qcom-smem-Add-and-export-qcom_smem_get_serial_no.patch \
    file:0010-soc-qcom-pmic_glink_altmode-Disable-TBT-USB4-negotia.patch \
    file:0011-Revert-soc-qcom-pmic_glink_altmode-Disable-TBT-USB4-.patch \
    file:0012-soc-qcom-pmic_glink_altmode-Pass-xxxx.patch \
    file:0013-soc-qcom-pmic_glink_altmode-Support-USB4-Gen4-speeds.patch \
    file:0014-clk-qcom-regmap-phy-mux-Rework-the-implementation.patch \
    file:0015-irqchip-qcom-pdc-Handle-ESPIs-in-qcom-pdc-ranges.patch \
    file:0016-PCI-qcom-Block-accesses-to-downstream-devices-on-lin.patch \
    file:0017-dt-bindings-clock-qcom-glymur-gcc-Add-missing-USB4-c.patch \
    file:0018-clk-qcom-gcc-glymur-Set-FORCE_MEM_ON-for-USB4-uC-clo.patch \
    file:0019-clk-qcom-gcc-glymur-Add-missing-USB4-resets.patch \
    file:0020-dt-bindings-clock-qcom-gcc-sc8280xp-Add-USB4-MISC-re.patch \
    file:0021-gcc_glymur_phymux.patch \
    file:0022-gcc-glmur-halt_skip.patch \
    file:0023-clk-qcom-gcc-sc8280xp-Describe-USB4-MISC-resets.patch \
    file:0024-clk-qcom-gcc-sc8280xp-Use-common-clock-controller-pr.patch \
    file:0025-clk-qcom-gcc-sc8280xp-Keep-USB4-uC-memory-powered.patch \
    file:0026-usb-typec-ucsi-Schedule-connector-worker-on-freezabl.patch \
    file:0027-of-property-Create-devlink-between-PCI-Host-bridge-a.patch \
    file:0028-thunderbolt-Move-tb_apple_add_links-to-pci.c.patch \
    file:0029-thunderbolt-Make-PCIe-NHI-support-opt-in.patch \
    file:0030-thunderbolt-Allow-batching-of-descriptors.patch \
    file:0031-thunderbolt-nhi-Process-ring-completions-in-the-thre.patch \
    file:0032-thunderbolt-nhi-Manually-clear-interrupts-for-MSI-X-.patch \
    file:0033-thunderbolt-Skip-reading-Rsvd-SB-registers-on-older-.patch \
    file:0034-thunderbolt-Add-pre_switch_suspend-NHI-hook.patch \
    file:0035-thunderbolt-Allow-PCIe-tunnel-activation-before-host.patch \
    file:0036-thunderbolt-Introduce-Qualcomm-USB4-router-driver.patch \
    file:0037-QC_TB_HAX.patch \
    file:0038-arm64-dts-qcom-hamoa-Wire-up-USB4-PCIe-hosts.patch \
    file:0039-arm64-dts-qcom-hamoa-Add-USB4-routers.patch \
    file:0040-usb-typec-mux-Mark-mux-switch-devices-as-PM-not-requ.patch \
    file:0041-usb-typec-bus-Don-t-set-state-alt-when-exiting-altmo.patch \
    file:0042-usb-core-Move-USB4-router-link-creation-from-usb-acp.patch \
    file:0043-USB4-devlink-experiments-for-DT.patch \
    file:0044-phy-qcom-phy-snps-eusb2-Ensure-reset-is-held-for-10-.patch \
    file:0045-SMB2360-repeater-discoveries.patch \
    file:0046-usb-dwc3-Resend-full-Data-Packet-for-non-compliant-U.patch \
    file:0047-PCI-Grow-hotplug-bridge-windows-to-fit-a-hot-added-s.patch \
    file:0048-usb-xhci-debugfs-Expose-the-USB3-tunneling-ext_cap-r.patch \
    file:0049-usb-xhci-Honor-PORTSC.TM-if-valid.patch \
    file:0050-usb-xhci-Allow-custom-op-for-usb_link_tunnel_mode-re.patch \
    file:0051-usb-dwc3-Notify-XHCI-core-of-tunneled-status.patch \
    file:0052-QMP-combo-phy.patch \
    file:0053-Thunderbolt.patch \
    file:0054-PG_ALTMODE.patch \
    file:0055-USB-link-type.patch \
    file:0056-Revert-PCI-Allow-D3-for-native-hotplug-capable-Root-.patch \
    file:0057-PCI-dwc-Align-register-macros-with-Synopsys-document.patch \
    file:0058-PCI-qcom-Handle-tunneling-capable-instances.patch \
    file:0059-rpmsg-retain-transport-endpoints-across-deferred-pro.patch \
    file:0060-soc-qcom-pmic_glink-defer-rpmsg-binding-safely.patch \
    file:0061-usb-dwc3-qcom-Reset-the-controller-on-resume-if-tunn.patch \
    file:0062-thunderbolt-nhi-resume-NHI-for-system-suspend.patch \
    file:0063-usb-hub-link-USB4-before-tunneled-device-enumeration.patch \
    file:0064-usb-hcd-pci-avoid-double-disable-on-surprise-removal.patch \
    file:0065-PCI-pciehp-Fix-AB-BA-deadlocks-between-reset_lock-pc.patch \
    file:0066-thunderbolt-Add-device-links-for-OF-PCIe-tunnels.patch \
    file:0067-PCI-qcom-Support-suspending-USB4-tunneled-PCIe-hosts.patch \
    file:0068-arm64-dts-qcom-glymur-Wire-up-USB4-PCIe-hosts.patch \
    file:0069-arm64-dts-qcom-glymur-Add-USB4-topology.patch \
    file:0070-PCIe-TODO.patch \
    file:0071-phy-qcom-edp-split-power-on-sequencing-by-PHY-versio.patch \
    file:0072-phy-qcom-edp-update-v8-power-on-programming-sequence.patch \
    file:0073-phy-qcom-qmp-combo-Clear-DP-AON-toggle-in-USB4-mode.patch \
    file:0074-thunderbolt-tb-Retry-USB3-tunnel-activation-on-resum.patch \
    file:0075-arm64-dts-qcom-glymur-crd-Enable-the-tertiary-Type-C.patch \
    file:0076-glymur_phy_pll_reset.patch \
    file:0077-WIP-combophy-usb4clamps.patch \
    file:0078-arm64-dts-qcom-glymur-crd-Disable-PCIe4-6-and-USB-HS.patch \
    file:0079-thunderbolt-Raise-TB_TIMEOUT-to-500-ms.patch \
    file:0080-thunderbolt-tb-Authorize-PCIe-tunnels-automatically.patch \
    file:0081-phy-exit-dwc3.patch \
    file:0082-thunderbolt-qcom-Defer-cable-unplug-handling-to-a-fr.patch \
    file:0083-thunderbolt-qcom-Track-the-cable-s-max-gen-instead-o.patch \
    file:0084-thunderbolt-disable-lane-bonding-for-compatiblity.patch \
    file:0085-thunderbolt-qcom-Kconfig-Add-the-missing-TYPEC-depen.patch \
"

# To build tip of qcom-next branch set preferred
# virtual/kernel provider to 'linux-qcom-next-upstream'
BBCLASSEXTEND = "devupstream:target"
PN:class-devupstream = "linux-qcom-next-upstream"
SRCREV:class-devupstream ?= "${AUTOREV}"

S = "${UNPACKDIR}/${BP}"

KBUILD_DEFCONFIG ?= "defconfig"
KBUILD_DEFCONFIG:qcom-armv7a = "qcom_defconfig"

KBUILD_CONFIG_EXTRA = "${S}/kernel/configs/hardening.config"
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
