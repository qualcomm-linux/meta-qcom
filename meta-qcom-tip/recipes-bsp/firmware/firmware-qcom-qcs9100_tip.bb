QCOM_FW_BOARD = "QCS9100"
QCOM_FW_SOC = "qcs9100"
QCOM_FW_NAME = "QCS9100_fw"

FW_QCOM_NAME = "sa8775p"

DSP_PKG_NAME = "qcom-sa8775p-ride"
DSP_QCOM_VENDOR = "Qualcomm"
DSP_QCOM_DEVICE = "SA8775P-RIDE"

require firmware-qcom-tip.inc

do_install:append() {
    mv ${D}/${DSP_QCOM_PATH}/dsp/*/*jsn ${D}/${FW_QCOM_PATH}
}
