inherit module

DESCRIPTION = "Qualcomm KGSL driver for managing Adreno GPU"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://adreno.c;beginline=1;endline=1;md5=fcab174c20ea2e2bc0be64b493708266"

SRCREV = "4300b655d3c0018b5341ecf876698cbb1c31f255"
SRC_URI = " \
    git://github.com/qualcomm-linux/kgsl.git;branch=gfx-kernel.le.0.0;protocol=https;tag=v${PV} \
    file://kgsl.rules \
"

do_install:append() {
      install -m 0644 ${UNPACKDIR}/kgsl.rules -D ${D}${nonarch_base_libdir}/udev/rules.d/kgsl.rules
}

KERNEL_MODULE_PROBECONF += "msm_kgsl"
module_conf_msm_kgsl = "blacklist msm_kgsl"

FILES:${PN} += "${nonarch_base_libdir}/udev/rules.d"

# The module is only promised to support ARMv8 machines
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"
