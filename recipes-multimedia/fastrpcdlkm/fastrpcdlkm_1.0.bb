DESCRIPTION = "QCOM Fastrpc Kernel driver that carrries multiple additional features like static PD restart and enhanced invocation support."
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://dsp/fastrpc.c;beginline=1;endline=3;md5=58e90984fcb9f71adc866f83edbd5118"
SRCREV = "9ff71841b2d021008a74b7200f736dbefab39f42"

SRC_URI = "git://git.codelinaro.org/clo/la/platform/vendor/qcom/opensource/dsp-kernel.git;protocol=https;branch=dsp-kernel.lnx.3.3;destsuffix=vendor/qcom/opensource/dsp-kernel"

S = "${WORKDIR}/vendor/qcom/opensource/dsp-kernel"

inherit module

EXTRA_OEMAKE += "M=${S}"

RPROVIDES:${PN} += "kernel-module-fastrpc-kernel"

FILESPATH =+ "${WORKSPACE}:"

COMPATIBLE_MACHINE = "qcm6490|qcs9100|qcs8300"

MAKE_TARGETS = "modules"

MODULES_INSTALL_TARGET = "modules_install"
KERNEL_MODULE_AUTOLOAD += "fastrpc-dsp"
