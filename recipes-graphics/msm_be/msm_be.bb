SUMMARY = "MSM GBM backend library"
DESCRIPTION = "Mesa GBM backend for MSM, built from Codelinaro repository"
HOMEPAGE = "https://git.codelinaro.org/clo/le/display/libgbm"

LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://src/gbm_msm.h;md5=8c54773149e04ded5c0c3e293bb13509"

SRC_URI = "git://git.codelinaro.org/clo/le/display/libgbm.git;branch=display.qclinux.1.0.r1-rel \
           file://0001-QCOM-libgbm-create-MSM-backend-if-KGSL-GPU.patch"
SRCREV = "f53e08166dbe75113d466cf8f78497a1ca24668d"
S = "${UNPACKDIR}/msm-be"

inherit meson pkgconfig
DEPENDS = "mesa libdrm libxml2"

do_install:append() {
    install -d ${D}${includedir}
    install -m 0644 ${S}/src/gbm_msm.h ${D}${includedir}/
}

PACKAGES =+ "msmgbm msmgbm-dev"

FILES:${PN} = "${libdir}/gbm/default_fmt_alignment.xml \
               ${libdir}/msm_gbm.so*"

FILES:${PN}-dev = "${includedir}/gbm_msm.h"

# libgbm loads msm_gbm.so at runtime, so the symlink must be in the main package.
INSANE_SKIP:${PN} += "dev-so"
