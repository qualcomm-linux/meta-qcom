SUMMARY = "MSM GBM backend library"
DESCRIPTION = "Mesa GBM backend for MSM, built from Codelinaro repository"
HOMEPAGE = "https://git.codelinaro.org/clo/le/display/libgbm"

LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://src/gbm_msm.h;md5=8c54773149e04ded5c0c3e293bb13509"

PV = "0.1+git"
SRCREV = "d2f771e8c80698a18b16f14ea60d5d55abede683"

SRC_URI = "git://git.codelinaro.org/clo/le/display/libgbm.git;protocol=https;branch=display.qclinux.1.0.r1-rel \
           file://0002-QCOM-libgbm-use-sysconfdir-to-install-configuration-.patch \
           file://0003-QCOM-libgbm-set-install-paths-for-backend-library-co.patch "

inherit meson pkgconfig features_check

DEPENDS = "mesa libdrm libxml2"
REQUIRED_DISTRO_FEATURES = "opengl"

FILES:${PN} = "${libdir}/gbm/msm_gbm.so* ${sysconfdir}/gbm/default_fmt_alignment.xml"

# libgbm uses dlopen() to load msm_gbm.so at runtime, so the .so file must be in the main package.
INSANE_SKIP:${PN} += "dev-so"
