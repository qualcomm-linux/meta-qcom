FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

# Enable freedreno driver
PACKAGECONFIG_FREEDRENO = "\
    freedreno \
    tools \
"

PACKAGECONFIG:append:qcom = "${PACKAGECONFIG_FREEDRENO}"
