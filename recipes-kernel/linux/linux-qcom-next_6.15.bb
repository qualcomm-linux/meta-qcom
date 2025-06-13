SECTION = "kernel"

DESCRIPTION = "Linux ${PV} kernel for QCOM devices"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel

COMPATIBLE_MACHINE = "(qcom)"

SRC_URI = "git://github.com/qualcomm-linux/kernel.git;protocol=https;branch=qcom-next"

# To build bleeding edge qcom-next kernel set preferred
# provider of virtual/kernel to 'linux-qcom-next-tip'
BBCLASSEXTEND = "devupstream:target"
PN:class-devupstream = "linux-qcom-next-tip"
SRCREV:class-devupstream = "${AUTOREV}"

# tag: qcom-next-6.15-20250609
SRCREV = "d60ff9e83aff9ec3aef464b0cfe8383fcad42010"
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
