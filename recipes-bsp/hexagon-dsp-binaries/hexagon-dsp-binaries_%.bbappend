# Dependent packages are only available from the lts-linux-firmware-mixins
# layer and thus can't be enabled by default. Enable it for Qualcomm machines,
# which also force enable the updated linux-firmware version.

PACKAGES_FROM_MIXINS_LAYER = " \
    ${PN}-qcom-shikra-cqm-evk-cdsp \
    ${PN}-qcom-shikra-cqs-evk-cdsp \
    ${PN}-qcom-shikra-iqs-evk-cdsp \
"

PACKAGE_BEFORE_PN:remove = "${PACKAGES_FROM_MIXINS_LAYER}"
PACKAGES:append:qcom = "${PACKAGES_FROM_MIXINS_LAYER}"
