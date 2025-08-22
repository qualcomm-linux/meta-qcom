inherit module

DESCRIPTION = "Qualcomm KGSL driver for managing Adreno GPU"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://adreno.c;beginline=1;endline=1;md5=fcab174c20ea2e2bc0be64b493708266"

SRCREV = "e878d0a22e449d92a2ab92f384e4775e6895b7f6"
SRC_URI = " \
    git://github.com/qualcomm-linux/kgsl.git;branch=gfx-kernel.le.0.0;protocol=https \
    file://kgsl.rules \
    file://0001-kgsl-fix-warnings-related-to-missing-integer-arg.patch \
    file://0002-kgsl-mark-gmu_core_iommu_domain_alloc-as-static-inli.patch \
    file://0003-kgsl-mark-_kgsl_set_smmu_aperture-as-static.patch \
    file://0004-kgsl-follow-from_timer-timer_container_of-rename.patch \
    file://0005-kgsl-follow-dma_fence-changes.patch \
    file://0006-kgsl-follow-bin_attribute-changes.patch \
    file://0007-kgsl-fix-undefined-platform_bus_type-error.patch \
    file://0008-kgsl-fix-building-with-6.12-qcom_scm_set_gpu_smmu_ap.patch \
"

do_install:append() {
      install -m 0644 ${WORKDIR}/sources/kgsl.rules -D ${D}${nonarch_base_libdir}/udev/rules.d/kgsl.rules
}

FILES:${PN} += "${nonarch_base_libdir}/udev/rules.d"

# The module is only promised to support ARMv8 machines
COMPATIBLE_MACHINES = ""
COMPATIBLE_MACHINES:aarch64 = "(.*)"
