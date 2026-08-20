FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI:append:qcom = " file://0001-freedreno-Add-support-for-A704.patch \
                        file://0001-freedreno-Add-chip-id-support-for-A830v1.patch \
"

# Enable freedreno driver
PACKAGECONFIG_FREEDRENO = "\
    freedreno \
    tools \
"

PACKAGECONFIG:append:qcom = "${PACKAGECONFIG_FREEDRENO}"
