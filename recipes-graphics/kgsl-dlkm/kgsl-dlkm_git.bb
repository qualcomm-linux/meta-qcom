inherit module

DESCRIPTION = "Qualcomm KGSL driver for managing Adreno GPU"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://adreno.c;beginline=1;endline=1;md5=fcab174c20ea2e2bc0be64b493708266"

SRCREV = "56d7a36e6b95d3e311fe22f93be98d9d5943036d"
SRC_URI = " \
    git://github.com/qualcomm-linux/kgsl.git;branch=gfx-kernel.le.0.0;protocol=https \
    file://kgsl.rules \
"
SRC_URI += " \
    file://0007-kgsl-fix-undefined-platform_bus_type-error.patch \
    file://0008-kgsl-fix-building-with-6.12-qcom_scm_set_gpu_smmu_ap.patch \
"

do_install:append() {
      install -m 0644 ${WORKDIR}/sources/kgsl.rules -D ${D}${nonarch_base_libdir}/udev/rules.d/kgsl.rules
}

KERNEL_MODULE_PROBECONF += "msm_kgsl"
module_conf_msm_kgsl = "blacklist msm_kgsl"

FILES:${PN} += "${nonarch_base_libdir}/udev/rules.d"

# The module is only promised to support ARMv8 machines
COMPATIBLE_MACHINES = ""
COMPATIBLE_MACHINES:aarch64 = "(.*)"
