DESCRIPTION = "QCOM Iris Video driver"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://vidc/src/resources.c;beginline=1;endline=6;md5=9ff9cd0fdb3934a26516669db252c457"
SRCREV = "57f29f46b23b41c9211d542b58d2991c91fe59cf"

SRC_URI = "git://git.codelinaro.org/clo/le/platform/vendor/opensource/video-driver.git;protocol=https;branch=video.qclinux.0.0"

S = "${WORKDIR}/git"

inherit module

MAKE_TARGETS = "modules"
MODULES_INSTALL_TARGET = "modules_install"
