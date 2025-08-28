inherit module

DESCRIPTION = "Qualcomm KGSL driver for managing Adreno GPU"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://adreno.c;beginline=1;endline=1;md5=fcab174c20ea2e2bc0be64b493708266"

SRCREV = "56d7a36e6b95d3e311fe22f93be98d9d5943036d"
SRC_URI = "git://github.com/qualcomm-linux/kgsl.git;branch=gfx-kernel.le.0.0;protocol=https"
SRC_URI += "file://kgsl.rules"

KERNEL_MODULE_PROBECONF += "msm_kgsl"
module_conf_msm_kgsl = "blacklist msm_kgsl"

do_install:append() {
      install -m 0644 ${WORKDIR}/sources/kgsl.rules -D ${D}${nonarch_base_libdir}/udev/rules.d/kgsl.rules
}

FILES:${PN} += "${nonarch_base_libdir}/udev/rules.d"
