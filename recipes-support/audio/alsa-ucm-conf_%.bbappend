FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:qcom = " \
    file://0001-Qualcomm-Add-QCS9075-IQ-EVK-HiFi-config.patch \
    file://0001-ucm2-Qualcomm-Update-the-QCM6490-and-QCS6490-hifi-co.patch \
    file://0002-ucm2-Qualcomm-Update-the-HIFI-enable-mixer-commands-.patch \
    file://0001-ucm2-Qualcomm-Rename-qcs6490-rb3gen2-and-qcs9075-iq-.patch \
    file://0001-ucm2-Qualcomm-Add-MONACO-EVK-HiFi-config.patch \
    file://0001-ucm2-Qualcomm-sa8775p-Move-lemans-evk-hifi-to-sa8775.patch \
"
