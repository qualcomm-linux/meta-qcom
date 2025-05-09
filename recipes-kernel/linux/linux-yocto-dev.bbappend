# do not override KBRANCH and SRCREV_machine, use default ones.
COMPATIBLE_MACHINE:qcom = "(qcom)"

FILESEXTRAPATHS:prepend:qcom := "${THISDIR}/${PN}:"

SRC_URI:append:qcom = " \
    file://qcm6490-board-dts/0001-FROMLIST-arm64-dts-qcom-qcm6490-idp-Update-protected.patch \
    file://qcm6490-board-dts/0001-dt-bindings-PCI-qcom-pcie-sc7280-Add-global-interrup.patch \
    file://qcm6490-board-dts/0002-arm64-dts-qcom-sc7280-Add-global-PCIe-interrupt.patch \
    file://qcm6490-board-dts/0001-dt-bindings-PCI-Add-binding-for-Toshiba-TC956x-PCIe-.patch \
    file://qcm6490-board-dts/0002-arm64-dts-qcom-qcs6490-rb3gen2-Add-TC956x-PCIe-switc.patch \
    file://qcm6490-board-dts/0001-arm64-dts-qcom-sc7280-Add-support-for-camss.patch \
    file://qcm6490-board-dts/0002-arm64-dts-qcom-qcs6490-rb3gen2-vision-mezzanine-Add-.patch \
    file://qcm6490-board-dts/0002-media-dt-bindings-update-clocks-for-sc7280-camss.patch \
    file://qcm6490-board-dts/0001-PENDING-enable-xHCI.patch \
    file://qcm6490-board-dts/0001-dt-bindings-clock-qcom-Add-compatible-for.patch \
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
    file://workarounds/0002-arm64-dts-qcom-Remove-CPUidle-states-for-QCS615.patch \
    file://workarounds/0003-arm64-dts-qcom-Add-i2c2-support-for-QCS615-RIDE-boar.patch \
    file://drivers/0003-PCI-Add-new-start_link-stop_link-function-ops.patch \
    file://drivers/0004-PCI-dwc-Add-host_start_link-host_start_link-hooks-fo.patch \
    file://drivers/0005-PCI-dwc-Implement-.start_link-.stop_link-hooks.patch \
    file://drivers/0006-PCI-qcom-Add-support-for-host_stop_link-host_start_l.patch \
    file://drivers/0007-PCI-PCI-Add-pcie_is_link_active-to-determine-if-the-.patch \
    file://drivers/0008-PCI-pwrctrl-Add-power-control-driver-for-tc956x.patch \
    file://drivers/0001-media-qcom-camss-update-clock-names-for-sc7280.patch \
    file://drivers/0004-clk-qcom-lpassaudiocc-sc7280-Add-support-for-LPASS-r.patch \
    file://drivers/0001-dt-bindings-net-bluetooth-qualcomm-document-WCN3950.patch \
    file://drivers/0002-Bluetooth-qca-simplify-WCN399x-NVM-loading.patch \
    file://drivers/0003-Bluetooth-qca-add-WCN3950-support.patch \
    file://drivers/0001-Bluetooth-qca-fix-NV-variant-for-one-of-WCN3950-SoCs.patch \
    file://qrb2210-board-dts/0004-arm64-dts-qcom-qcm2290-fix-some-of-QUP-interconnects.patch \
    file://qrb2210-board-dts/0005-arm64-dts-qcom-qcm2290-add-UART3-device.patch \
    file://qrb2210-board-dts/0006-arm64-dts-qcom-qrb2210-rb1-add-Bluetooth-support.patch \
    file://qcs9075-board-dts/0001-dt-bindings-arm-qcom-Document-rb8-ride-ride-r3-on-QC.patch \
    file://qcs9075-board-dts/0002-arm64-dts-qcom-Add-support-for-QCS9075-RB8.patch \
    file://qcs9075-board-dts/0003-arm64-dts-qcom-Add-support-for-QCS9075-Ride-Ride-r3.patch \
    file://qcs9075-board-dts/0004-arm64-dts-qcom-Enable-cpu-cooling-devices-for-QCS907.patch \
    file://qcs9075-board-dts/0001-arm64-dts-qcom-qcs9075-rb8-enable-UFS.patch \
    file://qcs615-board-dts/0001-arm64-dts-qcom-qcs615-Add-Command-DB-support.patch \
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
