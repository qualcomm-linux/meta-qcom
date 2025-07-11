inherit module

DESCRIPTION = "Qualcomm KGSL driver for managing Adreno GPU"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://adreno.c;beginline=1;endline=1;md5=fcab174c20ea2e2bc0be64b493708266"

SRCREV = "e878d0a22e449d92a2ab92f384e4775e6895b7f6"
SRC_URI = "git://github.com/qualcomm-linux/kgsl.git;branch=gfx-kernel.le.0.0;protocol=https"

S = "${WORKDIR}/git"

do_install:append() {
      install -m 0644 ${THISDIR}/kgsl.rules -D ${D}${nonarch_base_libdir}/udev/rules.d/kgsl.rules
}

FILES:${PN} += "${nonarch_base_libdir}/udev/rules.d"
