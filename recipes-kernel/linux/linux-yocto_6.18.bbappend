require linux-yocto-qcom.inc

SRC_URI:append:qcom = " \
    file://workarounds/f553aff9a3ab245e722349cc617bcdfe778c69af.patch \
"

KERNEL_DEVICETREE:remove = " \
    qcom/hamoa-iot-evk-camera-imx577.dtbo \
    qcom/lemans-el2.dtbo \
    qcom/monaco-el2.dtbo \
    qcom/monaco-evk-camera-imx577.dtbo \
    qcom/talos-el2.dtbo \
"
