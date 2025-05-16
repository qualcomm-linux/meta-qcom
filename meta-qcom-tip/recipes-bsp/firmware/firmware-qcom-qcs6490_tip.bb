QCOM_FW_BOARD = "Robotics RB3 Gen2"
QCOM_FW_SOC = "qcm6490"
QCOM_FW_NAME = "QCM6490_fw"

FW_QCOM_NAME = "qcm6490"

DSP_PKG_NAME = "thundercomm-rb3gen2"
DSP_QCOM_VENDOR = "Thundercomm"
DSP_QCOM_DEVICE = "RB3gen2"

require firmware-qcom-tip.inc

do_install:append() {
    install -d ${D}/${FW_QCOM_BASE_PATH}/qcs6490
    ln -sr ${D}/${FW_QCOM_PATH}/* ${D}/${FW_QCOM_BASE_PATH}/qcs6490/
}
