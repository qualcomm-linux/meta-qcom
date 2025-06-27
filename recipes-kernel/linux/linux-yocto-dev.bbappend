# do not override KBRANCH and SRCREV_machine, use default ones.
COMPATIBLE_MACHINE:qcom = "(qcom)"

require recipes-kernel/linux/linux-qcom.inc

FILESEXTRAPATHS:prepend:qcom := "${THISDIR}/${PN}:"

SRC_URI:append:qcom = " \
    file://qcm6490-board-dts/0001-FROMLIST-arm64-dts-qcom-qcm6490-idp-Update-protected.patch \
    file://qcm6490-board-dts/0001-dt-bindings-PCI-qcom-pcie-sc7280-Add-global-interrup.patch \
    file://qcm6490-board-dts/0002-arm64-dts-qcom-sc7280-Add-global-PCIe-interrupt.patch \
    file://qcm6490-board-dts/0001-dt-bindings-PCI-Add-binding-for-Toshiba-TC9563-PCIe-.patch \
    file://qcm6490-board-dts/0002-arm64-dts-qcom-qcs6490-rb3gen2-Add-TC9563-PCIe-switc.patch \
    file://qcm6490-board-dts/0001-arm64-dts-qcom-sc7280-Add-support-for-camss.patch \
    file://qcm6490-board-dts/0002-arm64-dts-qcom-qcs6490-rb3gen2-vision-mezzanine-Add-.patch \
    file://qcm6490-board-dts/0001-PENDING-enable-xHCI.patch \
    file://qcm6490-board-dts/0002-arm64-dts-qcom-qcm6490-idp-Update-the-LPASS.patch \
    file://qcm6490-board-dts/0003-arm64-dts-qcom-qcs6490-rb3gen2-Update-the-LPASS-audi.patch \
    file://qcm6490-board-dts/0001-arm64-dts-qcom-sc7280-Add-gpr-node.patch \
    file://qcm6490-board-dts/0002-arm64-dts-qcom-sc7280-Add-WSA-SoundWire-and-LPASS-su.patch \
    file://qcm6490-board-dts/0003-arm64-dts-qcom-qcs6490-rb3gen2-Modify-WSA-and-VA-mac.patch \
    file://qcm6490-board-dts/0004-arm64-dts-qcom-qcs6490-rb3gen2-add-WSA8830-speakers-.patch \
    file://qcm6490-board-dts/0005-arm64-dts-qcom-qcs6490-rb3gen2-Add-sound-card.patch \
    file://qcm6490-board-dts/0001-dts-rb3gen2-soundwire-checkin.patch \
    file://workarounds/0001-QCLINUX-arm64-dts-qcom-qcm6490-disable-sdhc1-for-ufs.patch \
    file://workarounds/0001-PENDING-arm64-dts-qcom-Remove-voltage-vote-support-f.patch \
    file://workarounds/0001-scsi-ufs-qcom-Check-gear-against-max-gear-in-vop-fre.patch \
    file://workarounds/0002-scsi-ufs-qcom-Map-devfreq-OPP-freq-to-UniPro-Core-Cl.patch \
    file://workarounds/0003-scsi-ufs-qcom-Call-ufs_qcom_cfg_timers-in-clock-scal.patch \
    file://workarounds/0001-media-venus-protect-against-spurious-interrupts-duri.patch \
    file://workarounds/0001-media-venus-hfi-explicitly-release-IRQ-during-teardo.patch \
    file://workarounds/0001-media-venus-Fix-probe-error-handling.patch \
    file://drivers/0003-PCI-Add-new-start_link-stop_link-function-ops.patch \
    file://drivers/0004-PCI-dwc-Add-host_start_link-host_start_link-hooks-fo.patch \
    file://drivers/0005-PCI-dwc-Implement-.start_link-.stop_link-hooks.patch \
    file://drivers/0006-PCI-qcom-Add-support-for-host_stop_link-host_start_l.patch \
    file://drivers/0007-PCI-PCI-Add-pcie_link_is_active-to-determine-if-the-.patch \
    file://drivers/0008-PCI-pwrctrl-Add-power-control-driver-for-tc9563.patch \
    file://qrb2210-board-dts/0004-arm64-dts-qcom-qcm2290-fix-some-of-QUP-interconnects.patch \
    file://qrb2210-board-dts/0005-arm64-dts-qcom-qcm2290-add-UART3-device.patch \
    file://qrb2210-board-dts/0006-arm64-dts-qcom-qrb2210-rb1-add-Bluetooth-support.patch \
    file://qcs9075-board-dts/0001-dt-bindings-arm-qcom-Add-bindings-for-QCS9075-SOC-ba.patch \
    file://qcs9075-board-dts/0002-SA8775P-has-a-memory-map-which-caters-to-the-auto-sp.patch \
    file://qcs9075-board-dts/0003-arm64-dts-qcom-qcs9075-Introduce-QCS9075M-SOM.patch \
    file://qcs9075-board-dts/0004-arm64-dts-qcom-Add-support-for-qcs9075-IQ-9075-EVK.patch \
    file://qcs9075-board-dts/0001-arm64-dts-qcom-qcs9075-iq-9075-evk-enable-UFS.patch \
"

# Include additional kernel configs.
SRC_URI:append:qcom = " \
    file://configs/qcom.cfg \
"

# When a defconfig is provided, the linux-yocto configuration
# uses the filename as a trigger to use a 'allnoconfig' baseline
# before merging the defconfig into the build.
#
# If the defconfig file was created with make_savedefconfig,
# not all options are specified, and should be restored with their
# defaults, not set to 'n'. To properly expand a defconfig like
# this, specify: KCONFIG_MODE="--alldefconfig" in the kernel
# recipe.
KCONFIG_MODE:qcom = "--alldefconfig"

KBUILD_DEFCONFIG:qcom ?= "defconfig"

do_install:append:qcom() {
	sed -i 's:${TMPDIR}::g' ${WORKDIR}/linux-${PACKAGE_ARCH}-${LINUX_KERNEL_TYPE}-build/drivers/gpu/drm/msm/generated/*
}
