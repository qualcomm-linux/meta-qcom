SECTION = "kernel"

DESCRIPTION = "Linux ${PV} kernel for QCOM devices"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel

COMPATIBLE_MACHINE = "(qcom)"

SRCBRANCH ?= "qcom-next"
SRC_URI = " \
            git://github.com/qualcomm-linux/kernel.git;protocol=https;branch=${SRCBRANCH} \
          "

# To build bleeding edge qcom-next staging kernel set preferred
# provider of virtual/kernel to 'linux-qcom-next-staging'
BBCLASSEXTEND = "devupstream:target"
PN:class-devupstream = "linux-qcom-next-staging"
SRCBRANCH:class-devupstream = "qcom-next-staging"
SRCREV:class-devupstream = "${AUTOREV}"

# v6.15-rc6
SRCREV = "82f2b0b97b36ee3fcddf0f0780a9a0825d52fec3"
PV = "6.15+git"

S = "${WORKDIR}/git"

KERNEL_CONFIG ?= "defconfig"

do_configure:prepend() {
    if [ ! -f "${S}/arch/${ARCH}/configs/${KERNEL_CONFIG}" ]; then
        bbfatal "KERNEL_CONFIG '${KERNEL_CONFIG}' was specified, but not present in the source tree"
    else
        cp '${S}/arch/${ARCH}/configs/${KERNEL_CONFIG}' '${B}/.config'
    fi
}
