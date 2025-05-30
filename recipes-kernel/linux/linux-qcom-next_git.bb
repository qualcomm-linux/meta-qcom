SECTION = "kernel"

DESCRIPTION = "Linux ${PV} kernel for QCOM devices"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel

COMPATIBLE_MACHINE = "(qcom)"

SRC_URI = "git://github.com/qualcomm-linux/kernel.git;branch=qcom-next;protocol=https"

LINUX_VERSION ?= "6.16-rc2"

PV = "${LINUX_VERSION}+git"

# tag: qcom-next-6.16-rc2-20250623
SRCREV ?= "3c20b557843734abc8fa0ea9ee6ed294eb52ece9"

# To build tip of qcom-next branch set preferred
# virtual/kernel provider to 'linux-qcom-next-upstream'
BBCLASSEXTEND = "devupstream:target"
PN:class-devupstream = "linux-qcom-next-upstream"
SRCREV:class-devupstream ?= "${AUTOREV}"

S = "${UNPACKDIR}/${BP}"

KERNEL_CONFIG_COMMAND ?= "oe_runmake_call -C ${S} O=${B} defconfig"
