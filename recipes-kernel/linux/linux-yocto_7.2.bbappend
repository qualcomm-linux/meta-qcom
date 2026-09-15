require linux-yocto-qcom.inc

SRC_URI:append:qcom = " \
    file://workarounds/f553aff9a3ab245e722349cc617bcdfe778c69af.patch \
    file://hamoa-iot-evk-dts/0001-arm64-dts-qcom-hamoa-iot-evk-camera-imx577-Add-DT-ov.patch \
"
