DESCRIPTION = "QCOM Iris Video drivers"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://vidc/src/resources.c;beginline=1;endline=2;md5=550794465ba0ec5312d6919e203a55f9"
SRCREV = "57f29f46b23b41c9211d542b58d2991c91fe59cf"

SRC_URI = "${SRCPROJECT};branch=${SRCBRANCH};destsuffix=vendor/qcom/opensource/video-driver"

SRCPROJECT = "git://git.codelinaro.org/clo/le/platform/vendor/opensource/video-driver.git;protocol=https"
SRCBRANCH = "video.qclinux.0.0"
S = "${WORKDIR}/vendor/qcom/opensource/video-driver"

inherit module

MAKE_TARGETS = "modules"

MODULES_INSTALL_TARGET = "modules_install"
