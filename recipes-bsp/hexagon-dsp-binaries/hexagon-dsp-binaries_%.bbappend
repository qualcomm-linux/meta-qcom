# Dependent packages are only available from the lts-linux-firmware-mixins
# layer and thus can't be enabled by default. Enable it for Qualcomm machines,
# which also force enable the updated linux-firmware version.

PACKAGES_FROM_MIXINS_LAYER = " \
    ${PN}-qcom-eliza-cqs-evk-adsp \
    ${PN}-qcom-eliza-cqs-evk-cdsp \
    ${PN}-qcom-eliza-mtp-adsp \
    ${PN}-qcom-eliza-mtp-cdsp \
    ${PN}-qcom-hawi-mtp-adsp \
    ${PN}-qcom-hawi-mtp-cdsp \
    ${PN}-qcom-maili-mtp-adsp \
    ${PN}-qcom-maili-mtp-cdsp \
    ${PN}-qcom-sa8797p-ride-embedded-adsp \
    ${PN}-qcom-sa8797p-ride-embedded-cdsp \
    ${PN}-qcom-shikra-cqm-evk-cdsp \
    ${PN}-qcom-shikra-cqs-evk-cdsp \
    ${PN}-qcom-shikra-iqs-evk-cdsp \
"

PACKAGE_BEFORE_PN:remove = "${PACKAGES_FROM_MIXINS_LAYER}"
PACKAGES:append:qcom = "${PACKAGES_FROM_MIXINS_LAYER}"
