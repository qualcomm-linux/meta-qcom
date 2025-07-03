SUMMARY = "MSM GBM backend library"
DESCRIPTION = "Mesa GBM backend for MSM, built from Codelinaro repository"
HOMEPAGE = "https://git.codelinaro.org/clo/le/display/libgbm"

LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://src/gbm_msm.h;md5=8c54773149e04ded5c0c3e293bb13509"

PV = "0.0+git"

SRC_URI = "git://git.codelinaro.org/clo/le/display/libgbm.git;branch=display.qclinux.1.0.r1-rel \
           file://0001-QCOM-libgbm-create-MSM-backend-if-KGSL-GPU.patch \
           file://0002-QCOM-libgbm-use-sysconfdir-to-install-configuration-.patch \
           file://0003-QCOM-libgbm-set-install-paths-for-backend-library-co.patch "

SRCREV = "f53e08166dbe75113d466cf8f78497a1ca24668d"

inherit meson pkgconfig

DEPENDS = "mesa libdrm libxml2"

FILES:${PN} = "${libdir}/gbm/msm_gbm.so* ${sysconfdir}/gbm/default_fmt_alignment.xml"

# libgbm loads msm_gbm.so at runtime, so the symlink must be in the main package.
INSANE_SKIP:${PN} += "dev-so"
