inherit module

DESCRIPTION = "Qualcomm KGSL driver for managing Adreno GPU"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://adreno.c;beginline=1;endline=1;md5=fcab174c20ea2e2bc0be64b493708266"

PV = "0.0+git"
SRCREV = "7e903430c17a2594b1b2907d459f04b2c3d2295b"
SRC_URI = " \
    git://github.com/qualcomm-linux/kgsl.git;branch=gfx-kernel.le.0.0;protocol=https \
    file://kgsl.rules \
"

do_install:append() {
      install -m 0644 ${WORKDIR}/sources/kgsl.rules -D ${D}${nonarch_base_libdir}/udev/rules.d/kgsl.rules
}

KERNEL_MODULE_PROBECONF += "msm_kgsl"
module_conf_msm_kgsl = "blacklist msm_kgsl"

FILES:${PN} += "${nonarch_base_libdir}/udev/rules.d"

# The module is only promised to support ARMv8 machines
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"
