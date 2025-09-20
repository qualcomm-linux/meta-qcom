FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

RDEPENDS:${PN}-qcom-qcm6490-audio:append:qcom = " ${PN}-linaro-license"
LICENSE:${PN}-qcom-qcm6490-audio:append:qcom  = " & Firmware-linaro"
RDEPENDS:${PN}-qcom-sa8775p-audio:append:qcom = " ${PN}-linaro-license"
LICENSE:${PN}-qcom-sa8775p-audio:append:qcom  = " & Firmware-linaro"
RDEPENDS:${PN}-qcom-qcs8300-audio:append:qcom = " ${PN}-linaro-license"
LICENSE:${PN}-qcom-qcs8300-audio:append:qcom  = " & Firmware-linaro"

SRC_URI:append:qcom = " \
    file://qcs6490/QCS6490-RB3Gen2-tplg.bin \
    file://qcs9100/LEMANS-EVK-tplg.bin \
    file://qcs8300/MONACO-EVK-tplg.bin \
"

do_install:append:qcom() {
    install -d ${D}${nonarch_base_libdir}/firmware/qcom/qcs6490
    install -m 0644 ${WORKDIR}/sources/qcs6490/QCS6490-RB3Gen2-tplg.bin ${D}${nonarch_base_libdir}/firmware/qcom/qcs6490/
    install -d ${D}${nonarch_base_libdir}/firmware/qcom/qcs9100
    install -m 0644 ${WORKDIR}/sources/qcs9100/LEMANS-EVK-tplg.bin ${D}${nonarch_base_libdir}/firmware/qcom/qcs9100/
    install -d ${D}${nonarch_base_libdir}/firmware/qcom/qcs8300
    install -m 0644 ${WORKDIR}/sources/qcs8300/MONACO-EVK-tplg.bin ${D}${nonarch_base_libdir}/firmware/qcom/qcs8300/
}

FILES:${PN}-qcom-qcm6490-audio:append:qcom = " ${nonarch_base_libdir}/firmware/qcom/qcs6490/QCS6490-RB3Gen2-tplg.bin"
FILES:${PN}-qcom-sa8775p-audio:append:qcom = " ${nonarch_base_libdir}/firmware/qcom/qcs9100/LEMANS-EVK-tplg.bin"
FILES:${PN}-qcom-qcs8300-audio:append:qcom = " ${nonarch_base_libdir}/firmware/qcom/qcs8300/MONACO-EVK-tplg.bin"
