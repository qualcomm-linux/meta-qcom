require recipes-graphics/mesa/mesa.inc

SRC_URI = " \
    git://gitlab.freedesktop.org/mesa/mesa;branch=25.2;protocol=https \
    file://0001-meson-misdetects-64bit-atomics-on-mips-clang.patch \
    file://0001-freedreno-don-t-encode-build-path-into-binaries.patch \
"
SRCREV ?= "85abdb86d377c0e14a9bf73e8023c1845caf98e9"
PV = "25.1+25.2.0-rc2"

PACKAGECONFIG[opencl] = " \
    -Dgallium-rusticl=true, \
    -Dgallium-rusticl=false, \
    bindgen-cli-native \
"

FILESEXTRAPATHS:prepend := "${THISDIR}/mesa-git:"

# Enable freedreno driver
PACKAGECONFIG_FREEDRENO = "\
    freedreno \
    tools \
    ${@bb.utils.contains('BBFILE_COLLECTIONS', 'openembedded-layer', 'freedreno-fdperf', '', d)} \
"

PACKAGECONFIG:append:qcom = " ${PACKAGECONFIG_FREEDRENO}"
