require linux-yocto-qcom.inc

SRC_URI:append:qcom = " \
    file://workarounds/f553aff9a3ab245e722349cc617bcdfe778c69af.patch \
    file://monaco-evk-dts/0001-arm64-dts-qcom-monaco-evk-camera-Add-DT-overlay.patch \
"
