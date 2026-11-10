SUMMARY = "MSM GBM backend library"
DESCRIPTION = "Mesa GBM backend for MSM"
HOMEPAGE = "https://github.com/qualcomm-linux/gbm-msm-backend"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://src/gbm_msm.h;md5=6791d9c2788673887aec4e056911f371"

SRCREV = "d6366f40759e67ff7ce5df3af377e1103ccfd414"

SRC_URI = "git://github.com/qualcomm-linux/gbm-msm-backend.git;protocol=https;branch=main;tag=v${PV} \
           file://0002-QCOM-libgbm-use-sysconfdir-to-install-configuration-.patch \
           file://0003-QCOM-libgbm-set-install-paths-for-backend-library-co.patch "

inherit meson pkgconfig features_check

COMPATIBLE_MACHINE = "(qcom)"

DEPENDS = "mesa libdrm libxml2"
REQUIRED_DISTRO_FEATURES = "opengl"

FILES:${PN} = "${libdir}/gbm/msm_gbm.so* ${libdir}/gbm/msm-kms_gbm.so ${sysconfdir}/gbm/default_fmt_alignment.xml"

# libgbm uses dlopen() to load msm_gbm.so at runtime, so the .so file must be in the main package.
INSANE_SKIP:${PN} += "dev-so"
