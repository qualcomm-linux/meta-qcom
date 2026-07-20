FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI:append:qcom = " \ 
    file://0001-freedreno-Add-support-for-A704.patch \
    file://0001-freedreno-layout-tu-Fix-UBWC-block-sizes-for-PIPE_FO.patch \
    file://0001-freedreno-Modify-reg_size_vec4-for-a608-and-a612-to-32.patch \
"

# Enable freedreno driver
PACKAGECONFIG_FREEDRENO = "\
    freedreno \
    tools \
"

PACKAGECONFIG:append:qcom = "${PACKAGECONFIG_FREEDRENO}"
