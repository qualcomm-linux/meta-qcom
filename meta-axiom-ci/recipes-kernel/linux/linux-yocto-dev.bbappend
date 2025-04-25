FILESEXTRAPATHS:prepend:qcom-axiom-ci := "${THISDIR}/${PN}:"

# Include additional kernel configs.
SRC_URI:append:qcom-axiom-ci = " \
    file://configs/qcom-axiom-ci.cfg \
"

