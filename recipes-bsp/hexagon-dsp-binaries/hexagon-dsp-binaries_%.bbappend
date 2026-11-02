# Dependent packages are only available from the lts-linux-firmware-mixins
# layer and thus can't be enabled by default. Enable it for Qualcomm machines,
# which also force enable the updated linux-firmware version.

PACKAGE_BEFORE_PN_ONLY_OECORE = " \
    ${PN}-config-schema \
    ${PN}-arduino-monza-adsp \
    ${PN}-arduino-monza-cdsp \
    ${PN}-arduino-monza-gdsp \
    ${PN}-qcom-db820c-adsp \
    ${PN}-qcom-glymur-crd-adsp \
    ${PN}-qcom-glymur-crd-cdsp \
    ${PN}-qcom-hamoa-iot-evk-adsp \
    ${PN}-qcom-hamoa-iot-evk-cdsp \
    ${PN}-qcom-iq8275-evk-adsp \
    ${PN}-qcom-iq8275-evk-cdsp \
    ${PN}-qcom-iq8275-evk-gdsp \
    ${PN}-qcom-iq9075-evk-adsp \
    ${PN}-qcom-iq9075-evk-cdsp \
    ${PN}-qcom-iq9075-evk-gdsp \
    ${PN}-qcom-kaanapali-mtp-adsp \
    ${PN}-qcom-kaanapali-mtp-cdsp \
    ${PN}-qcom-mahua-crd-adsp \
    ${PN}-qcom-mahua-crd-cdsp \
    ${PN}-qcom-purwa-iot-evk-adsp \
    ${PN}-qcom-purwa-iot-evk-cdsp \
    ${PN}-qcom-qcm6490-idp-adsp \
    ${PN}-qcom-qcm6490-idp-cdsp \
    ${PN}-qcom-qcs615-ride-adsp \
    ${PN}-qcom-qcs615-ride-cdsp \
    ${PN}-qcom-qcs8300-ride-adsp \
    ${PN}-qcom-qcs8300-ride-cdsp \
    ${PN}-qcom-qcs8300-ride-gdsp \
    ${PN}-qcom-sa8775p-ride-adsp \
    ${PN}-qcom-sa8775p-ride-cdsp \
    ${PN}-qcom-sa8775p-ride-gdsp \
    ${PN}-qcom-sdm845-hdk-adsp \
    ${PN}-qcom-sdm845-hdk-cdsp \
    ${PN}-qcom-sm8750-mtp-adsp \
    ${PN}-qcom-sm8750-mtp-cdsp \
    ${PN}-radxa-dragon-q6a-adsp \
    ${PN}-radxa-dragon-q6a-cdsp \
    ${PN}-thundercomm-db845c-adsp \
    ${PN}-thundercomm-db845c-cdsp \
    ${PN}-thundercomm-db845c-sdsp \
    ${PN}-thundercomm-rb1-adsp \
    ${PN}-thundercomm-rb2-adsp \
    ${PN}-thundercomm-rb2-cdsp \
    ${PN}-thundercomm-rb3gen2-adsp \
    ${PN}-thundercomm-rb3gen2-cdsp \
    ${PN}-thundercomm-rb5-adsp \
    ${PN}-thundercomm-rb5-cdsp \
    ${PN}-thundercomm-rb5-sdsp \
    ${PN}-thundercomm-rubikpi3-adsp \
    ${PN}-thundercomm-rubikpi3-cdsp \
"

python() {
    PACKAGE_BEFORE_PN = set(d.getVar('PACKAGE_BEFORE_PN').split())
    PACKAGE_BEFORE_PN_ONLY_OECORE = set(d.getVar('PACKAGE_BEFORE_PN_ONLY_OECORE').split())
    PACKAGE_BEFORE_PN_ONLY_MIXINS = ' '.join(PACKAGE_BEFORE_PN - PACKAGE_BEFORE_PN_ONLY_OECORE)
    d.setVar('PACKAGE_BEFORE_PN_ONLY_MIXINS', PACKAGE_BEFORE_PN_ONLY_MIXINS)
}

PACKAGE_BEFORE_PN:remove = "${PACKAGE_BEFORE_PN_ONLY_MIXINS}"
PACKAGES:prepend:qcom = "${PACKAGE_BEFORE_PN_ONLY_MIXINS} "
