FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

RDEPENDS:${PN}-qcom-qcm6490-audio:append:qcom = " ${PN}-linaro-license"
LICENSE:${PN}-qcom-qcm6490-audio:append:qcom  = " & Firmware-linaro"

SRC_URI:append:qcom = " \
    file://qcs6490/QCS6490-RB3Gen2-tplg.bin \
"

do_install:append:qcom() {
    install -d ${D}${nonarch_base_libdir}/firmware/qcom/qcs6490
    install -m 0644 ${WORKDIR}/sources/qcs6490/QCS6490-RB3Gen2-tplg.bin ${D}${nonarch_base_libdir}/firmware/qcom/qcs6490/
}

FILES:${PN}-qcom-qcm6490-audio:append:qcom = " ${nonarch_base_libdir}/firmware/qcom/qcs6490/QCS6490-RB3Gen2-tplg.bin"
