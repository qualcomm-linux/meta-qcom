SUMMARY = "MSM GBM backend library"
DESCRIPTION = "Mesa GBM backend for MSM"
HOMEPAGE = "https://github.com/qualcomm-linux/gbm-msm-backend"

LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://src/gbm_msm.h;md5=8c54773149e04ded5c0c3e293bb13509"

PV = "1.2.7"
SRCREV = "f534b0eeafcdba20bb752ec5a1b17754a1797c59"

SRC_URI = "git://github.com/qualcomm-linux/gbm-msm-backend.git;protocol=https;branch=main \
           file://0002-QCOM-libgbm-use-sysconfdir-to-install-configuration-.patch \
           file://0003-QCOM-libgbm-set-install-paths-for-backend-library-co.patch "

inherit meson pkgconfig features_check

COMPATIBLE_MACHINE = "(qcom)"

DEPENDS = "mesa libdrm libxml2"
REQUIRED_DISTRO_FEATURES = "opengl"

FILES:${PN} = "${libdir}/gbm/msm_gbm.so* ${libdir}/gbm/msm-kms_gbm.so ${sysconfdir}/gbm/default_fmt_alignment.xml"

do_install:append() {
    ln -sf msm_gbm.so ${D}${libdir}/gbm/msm-kms_gbm.so
}

# libgbm uses dlopen() to load msm_gbm.so at runtime, so the .so file must be in the main package.
INSANE_SKIP:${PN} += "dev-so"
