
SRC_URI:append:qcom = " \
    file://0001-arm64-dts-qcom-qcm2290-temporarily-disable-cluster-i.patch \
    file://qca6390-driver/0001-dt-bindings-mfd-qcom-qca639x-add-binding-for-QCA639x.patch \
    file://qca6390-driver/0002-mfd-qca639x-add-support-for-QCA639x-powerup-sequence.patch \
    file://qca6390-driver/0003-mfd-qcom-qca639x-switch-to-platform-config-data.patch \
    file://qca6390-driver/0004-mfd-qcom-qca639x-change-qca639x-to-use-gpios-rather-.patch \
    file://qca6390-driver/0005-mfd-qcom-qca639x-Add-support-for-WCN6855.patch \
    file://qca6390-dts/0001-arm64-dts-qcom-qrb5165-rb5-add-qca639x-power-domain.patch \
    file://qca6390-dts/0002-arm64-dts-qcom-Add-Bluetooth-support-on-RB5.patch \
    file://qca6390-dts/0003-arm64-dtb-qcom-qrb5165-rb5-add-power-domain-to-pcie0.patch \
    file://generic-drivers/mpm/0001-dt-bindings-interrupt-controller-mpm-Pass-MSG-RAM-sl.patch \
    file://generic-drivers/mpm/0002-irqchip-irq-qcom-mpm-Support-passing-a-slice-of-SRAM.patch \
    file://generic-drivers/mdss-icc/0002_dt_bindings_display_msm_add_reg_bus_and_rotator_interconnects.patch \
    file://generic-drivers/mdss-icc/0001_drm_msm_mdss_switch_mdss_to_use_devm_of_icc_get.patch \
    file://generic-drivers/mdss-icc/0002_drm_msm_mdss_rename_path_references_to_mdp_path.patch \
    file://generic-drivers/mdss-icc/0003_drm_msm_mdss_inline_msm_mdss_icc_request_bw.patch \
    file://generic-drivers/mdss-icc/0004_drm_msm_mdss_handle_the_reg_bus_icc_path.patch \
    file://generic-drivers/icc/0001-interconnect-qcom-icc-rpm-Add-AB-IB-calculations-coe.patch \
    file://generic-drivers/icc/0002-interconnect-qcom-icc-rpm-Separate-out-clock-rate-ca.patch \
    file://generic-drivers/icc/0003-interconnect-qcom-icc-rpm-Let-nodes-drive-their-own-.patch \
    file://generic-drivers/icc/0004-interconnect-qcom-icc-rpm-Check-for-node-specific-ra.patch \
    file://generic-drivers/icc/0005-interconnect-qcom-qcm2290-Hook-up-MAS_APPS_PROC-s-bu.patch \
    file://generic-drivers/icc/0006-interconnect-qcom-qcm2290-Set-AB-coefficients.patch \
    file://generic-drivers/icc/0007-interconnect-qcom-qcm2290-Update-EBI-channel-configu.patch \
    file://generic-drivers/icc/0008-interconnect-qcom-sdm660-Set-AB-IB-coefficients.patch \
    file://generic-drivers/icc/0009-interconnect-qcom-msm8996-Set-AB-IB-coefficients.patch \
    file://qrb2210-drivers/0001_dt_bindings_display_msm_qcm2290_mdss_use_the_non_deprecated_dsi_compat.patch \
    file://qrb2210-drivers/0003_dt_bindings_interconnect_qcom_msm8998_bwmon_add_qcm2290_bwmon_instance.patch \
    file://qrb2210-drivers/0004_dt_bindings_firmware_qcom_scm_allow_interconnect_for_everyone.patch \
    file://qrb2210-drivers/0005_iommu_arm_smmu_qcom_add_qcm2290_mdss_compatible.patch \
    file://qrb2210-dts/sans-icc/0003_arm64_dts_qcom_qrb2210_rb1_enable_remote_processors.patch \
    file://qrb2210-dts/sans-icc/0004_arm64_dts_qcom_qrb2210_rb1_add_gpio_leds.patch \
    file://qrb2210-dts/sans-icc/0005_arm64_dts_qcom_qrb2210_rb1_hook_up_usb3.patch \
    file://qrb2210-dts/sans-icc/0001-arm64-dts-qcom-qrb2210-rb1-use-USB-host-mode.patch \
    file://qrb2210-dts/features/0006_arm64_dts_qcom_sc7180_add_the_missing_mdss_icc_path.patch \
    file://qrb2210-dts/features/0007_arm64_dts_qcom_sc7280_add_the_missing_mdss_icc_path.patch \
    file://qrb2210-dts/features/0008_arm64_dts_qcom_qcm2290_add_display_nodes.patch \
    file://qrb2210-dts/features/0009_arm64_dts_qcom_qcm2290_hook_up_interconnects.patch \
    file://qrb2210-dts/features/0010_arm64_dts_qcom_qrb2210_rb1_set_up_hdmi.patch \
    file://qrb2210-dts/features/0011_arm64_dts_qcom_qrb2210_rb1_enable_can_bus_controller.patch \
    file://qrb2210-dts/features/0012_arm64_dts_qcom_qrb2210_rb1_add_wifi_variant_property.patch \
    file://qrb2210-dts/0001-arm64-dts-qcom-qcm2290-Hook-up-MPM.patch \
    file://qrb4210-drivers/icc/0001-dt-bindings-interconnect-Add-Qualcomm-SM6115-NoC.patch \
    file://qrb4210-drivers/icc/0002-interconnect-qcom-Add-SM6115-interconnect-provider-d.patch \
    file://qrb4210-drivers/icc/0001-iommu-arm-smmu-qcom-Add-SM6115-MDSS-compatible.patch \
    file://qrb4210-dts/0001-arm64-dts-qcom-sm6115-Add-UART3.patch \
    file://qrb4210-dts/0002-arm64-dts-qcom-qrb4210-rb2-Enable-bluetooth.patch \
    file://qrb4210-dts/0001-arm64-dts-qcom-sm6115-Hook-up-interconnects.patch \
    file://qrb4210-dts/0001-arm64-dts-qcom-qrb4210-rb2-Enable-MPSS-and-Wi-Fi.patch \
    file://qrb4210-dts/usb/0002-arm64-dts-qcom-qrb4210-rb2-Select-USB3-host-mode-by-.patch \
    file://qrb4210-dts/usb/0003-arm64-dts-qcom-sm6115-Enable-USB3-SS-phy.patch \
    file://generic-drivers/0001-FROMLIST-dma-heap-Add-proper-kref-handling-on-dma-bu.patch \
    file://generic-drivers/0002-FROMLIST-dma-heap-Provide-accessors-so-that-in-kerne.patch \
    file://qcm6490-drivers/0001-FROMGIT-phy-qcom-qmp-ufs-Add-Phy-Configuration-suppo.patch \
    file://qcm6490-dtsi/0001-FROMLIST-arm64-dts-qcom-sc7280-Add-UFS-nodes-for-sc7.patch \
    file://qcm6490-dtsi/0001-PENDING-arm64-dts-qcom-sc7280-Add-interconnect-paths.patch \
    file://qcm6490-dtsi/0001-UPSTREAM-arm64-dts-qcom-sc7280-Move-video-firmware-t.patch \
    file://qcm6490-dtsi/0001-UPSTREAM-arm64-dts-qcom-Use-QCOM_SCM_VMID-defines-fo.patch \
    file://qcm6490-board-dts/0001-UPSTREAM-dt-bindings-arm-qcom-Add-QCM6490-Fairphone-.patch \
    file://qcm6490-board-dts/0002-UPSTREAM-dt-bindings-arm-qcom-Add-QCM6490-IDP-and-QC.patch \
    file://qcm6490-board-dts/0003-UPSTREAM-arm64-dts-qcom-Add-base-qcm6490-id.patch \
    file://qcm6490-board-dts/0004-UPSTREAM-arm64-dts-qcom-Add-base-qcs6490-rb3gen2-boa.patch \
    file://qcm6490-board-dts/0001-UPSTREAM-arm64-dts-qcom-qcs6490-rb3gen2-Correct-the-.patch \
    file://qcm6490-board-dts/0002-UPSTREAM-arm64-dts-qcom-qcm6490-idp-Correct-the-volt.patch \
    file://qcm6490-board-dts/0001-FROMLIST-arm64-dts-qcom-qcm6490-idp-Update-protected.patch \
    file://qcm6490-board-dts/0002-UPSTREAM-arm64-dts-qcom-qcs6490-rb3gen2-Declare-GCC-.patch \
    file://qcm6490-board-dts/0003-BACKPORT-FROMLIST-arm64-dts-qcom-qcs6490-rb3gen2-Upd.patch \
"