DESCRIPTION = "Qualcomm EVA (CVP) driver"
HOMEPAGE = "https://github.com/qualcomm-linux/eva-driver"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://Kbuild;beginline=1;endline=1;md5=829e563511c9a1d6d41f17a7a4989d6a"

SRC_URI = " \
    git://github.com/qualcomm-linux/eva-driver.git;protocol=https;branch=eva-kernel.qclinux.0.0;tag=${PV} \
"

SRCREV = "6e20debbe82a3939ad3ec5e09ddf6662224ff7bd"

inherit module

MAKE_TARGETS = "modules"
MODULES_INSTALL_TARGET = "modules_install"

EXTRA_OEMAKE += "INCLUDEDIR=${STAGING_DIR_TARGET}${includedir}"
INSANE_SKIP:${PN} += "installed-vs-shipped"
# INSANE_SKIP allows to omit the packaging of the .conf files which we just want to install on the target

do_install:append() {
    install -d ${D}${includedir}/eva/media
    install -m 0644 ${S}/include/uapi/eva/media/*.h ${D}${includedir}/eva/media/
}

KERNEL_MODULE_AUTOLOAD += "msm-eva"
KERNEL_MODULE_PROBECONF += "msm-eva"
module_conf_msm-eva = "options msm_eva mp_load_pil=0"

# This package is designed to run exclusively on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"
