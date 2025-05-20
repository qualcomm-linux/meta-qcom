FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

# Enable freedreno driver
PACKAGECONFIG_FREEDRENO = "\
    freedreno \
    tools \
    ${@bb.utils.contains('BBFILE_COLLECTIONS', 'openembedded-layer', 'freedreno-fdperf', '', d)} \
"

PACKAGECONFIG:append:qcom = "${PACKAGECONFIG_FREEDRENO}"

SRC_URI:append:qcom = " \
    file://0001-freedreno-Add-initial-A702-support.patch \
"
