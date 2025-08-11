
SUMMARY = "MSM GBM Backend for Mesa"
DESCRIPTION = "Builds only the GBM component of Mesa with MSM backend support"
LICENSE = "MIT"

LIC_FILES_CHKSUM = "file://docs/license.rst;md5=63779ec98d78d823a9dc533a0735ef10"

SRC_URI = "https://mesa.freedesktop.org/archive/mesa-24.0.7.tar.xz \
           file://0001-mesa-add-meson-option-to-compile-standalone-gbm.patch \
           file://0001-add-MSM-backend-with-xml-schema.patch \
"
SRC_URI[sha256sum] = "7454425f1ed4a6f1b5b107e1672b30c88b22ea0efea000ae2c7d96db93f6c26a"

S = "${UNPACKDIR}/mesa-24.0.7"

inherit meson pkgconfig python3native

DEPENDS = "libdrm expat zlib python3-mako-native glslang-native libxml2"

PACKAGECONFIG = "gbm \
                 standalonegbm \
		 "

PACKAGECONFIG[gbm] = "-Dgbm=enabled,-Dgbm=disabled"
PACKAGECONFIG[standalonegbm] = "-Dstandalone_gbm=true,-Dstandalone_gbm=false"

EXTRA_OEMESON += "-Dvulkan-drivers='' \
 -Dplatforms='' \
 -Dopengl=false \
 -Degl=disabled \
 -Dgles1=disabled \
 -Dgles2=disabled \
 -Dglx=disabled \
 -Dshared-glapi=disabled \
 -Dllvm=disabled \
 -Dgallium-drivers='' \
"

do_install:append() {
    rm -f ${D}${libdir}/libgbm.so*
    rm -f ${D}${libdir}/pkgconfig/gbm.pc
    rm -f ${D}${includedir}/gbm*.h
    rmdir --ignore-fail-on-non-empty ${D}${libdir}/pkgconfig || true
    install -m 0644 ${S}/src/gbm/backends/msm/gbm_msm.h ${D}${includedir}/
}

PACKAGES =+ "msmgbm msmgbm-dev"

FILES:${PN} = "${libdir}/gbm/default_fmt_alignment.xml \
               ${libdir}/gbm/msm_gbm.so.*"

FILES:${PN}-dev = "${includedir}/gbm_msm.h \
                   ${libdir}/gbm/msm_gbm.so"
