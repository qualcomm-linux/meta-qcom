# qcs615 DT/Driver patches
SRC_URI:append = " \
    file://qcs615/0001-dt-bindings-arm-qcom-talos-evk-Add-QCS615-Talos-EVK-.patch \
    file://qcs615/0002-arm64-dts-qcom-talos-qcs615-ride-Fix-inconsistent-US.patch \
    file://qcs615/0003-arm64-dts-qcom-talos-evk-Add-support-for-QCS615-talo.patch \
    file://qcs615/0004-media-camss-csiphy-Make-CSIPHY-status-macro-cross-pl.patch \
    file://qcs615/0005-media-qcom-camss-Add-support-for-regulator-init_load.patch \
    file://qcs615/0006-media-qcom-camss-Do-not-enable-cpas-fast-ahb-clock-f.patch \
    file://qcs615/0007-media-qcom-camss-csid-340-Fix-unused-variables.patch \
    file://qcs615/0008-media-qcom-camss-vfe-Fix-out-of-bounds-access-in-vfe.patch \
    file://qcs615/0009-media-qcom-camss-change-internals-of-endpoint-parsin.patch \
    file://qcs615/0010-media-qcom-camss-use-a-handy-v4l2_async_nf_add_fwnod.patch \
    file://qcs615/0011-media-dt-bindings-Add-qcom-sm6150-camss.patch \
    file://qcs615/0012-media-qcom-camss-add-support-for-SM6150-camss.patch \
    file://qcs615/0013-dt-bindings-i2c-qcom-cci-Document-sm6150-compatible.patch \
    file://qcs615/0014-media-i2c-imx412-Assert-reset-GPIO-during-probe.patch \
    file://qcs615/0015-media-i2c-imx412-Extend-the-power-on-waiting-time.patch \
    file://qcs615/0016-arm64-dts-qcom-talos-Add-camss-node.patch \
    file://qcs615/0017-arm64-dts-qcom-talos-Add-CCI-definitions.patch \
    file://qcs615/0018-arm64-dts-qcom-talos-Add-camera-MCLK-pinctrl.patch \
    file://qcs615/0019-arm64-dts-qcom-talos-evk-camera-Add-DT-overlay.patch \
"
