FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI:append:qcom = " \ 
    file://0001-freedreno-Add-chip-id-support-for-A830v1.patch \
    file://0001-freedreno-Add-chip-support-for-a722.patch \
    file://0001-freedreno-a6xx-upload-dummy-sampler.patch \
    file://0002-rusticl-emit-image-texture-barriers.patch \
    file://0003-freedreno-a6xx-enable-f16-infinities.patch \
"

# Enable freedreno driver
PACKAGECONFIG_FREEDRENO = "\
    freedreno \
    tools \
"

PACKAGECONFIG:append:qcom = "${PACKAGECONFIG_FREEDRENO}"
