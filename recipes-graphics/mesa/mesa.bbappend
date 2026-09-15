FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI:append:qcom = " \ 
    file://0001-freedreno-Add-chip-id-support-for-A830v1.patch \
    file://0001-freedreno-Add-chip-support-for-a722.patch \
    file://0001-tu-disable-storage-image-support-depth-stencil.patch \
    file://0002-turnip-scale-xfb-counter-offset-a6xx.patch \
    file://0003-tu-do-not-bake-static-viewport-scissor-fdm.patch \
"

# Enable freedreno driver
PACKAGECONFIG_FREEDRENO = "\
    freedreno \
    tools \
"

PACKAGECONFIG:append:qcom = "${PACKAGECONFIG_FREEDRENO}"
