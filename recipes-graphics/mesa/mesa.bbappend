FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

# Enable freedreno driver
PACKAGECONFIG_FREEDRENO = "\
    freedreno \
    tools \
    ${@bb.utils.contains('BBFILE_COLLECTIONS', 'openembedded-layer', 'freedreno-fdperf', '', d)} \
"

PACKAGECONFIG:append:qcom = "${PACKAGECONFIG_FREEDRENO}"

SRC_URI:append:qcom = " \
    file://0001-freedreno-check-if-GPU-supported-in-fd_pipe_new2.patch;patch=1"

FILESEXTRAPATHS:prepend:qcm2290 := "${THISDIR}/${BPN}/qcm2290:"
SRC_URI:qcm2290 = " \
    https://archive.mesa3d.org/mesa-${PV}.tar.xz;name=qcm2290 \
    file://0001-meson-misdetects-64bit-atomics-on-mips-clang.patch \
    file://0001-freedreno-don-t-encode-build-path-into-binaries.patch \
    file://0001-meson_options.txt-add-dummy-entris-to-remain-compati.patch \
    file://0001-freedreno-Add-initial-A702-support.patch \
    file://0002-freedreno-A702-fixes-for-deqp-vk.patch \
    file://0003-freedreno-fix-compilation.patch \
"
SRC_URI[qcm2290.sha256sum] = "49eb55ba5acccae91deb566573a6a73144a0f39014be1982d78c21c5b6b0bb3f"
PV:qcm2290 = "25.0.1"
DEPENDS:append:qcm2290 = " python3-pyyaml-native "
LIC_FILES_CHKSUM:qcm2290 = "file://docs/license.rst;md5=ffe678546d4337b732cfd12262e6af11"
GALLIUMDRIVERS:qcm2290 = "softpipe"
# All DRI drivers are symlinks to libdril_dri.so
INSANE_SKIP:mesa-megadriver:qcm2290 += "dev-so"


FILES:libgallium = "${libdir}/libgallium-*.so"
PACKAGES += "libgallium"
FILES:libgbm = "${libdir}/libgbm.so.* ${libdir}/gbm/*_gbm.so"
