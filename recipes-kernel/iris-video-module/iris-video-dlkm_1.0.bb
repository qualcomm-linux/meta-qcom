DESCRIPTION = "QCOM Iris Video Driver"
LICENSE = "GPL-2.0-only"

LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e"

SRC_URI = "git://github.com/qualcomm-linux/video-driver.git;protocol=https;branch=video.qclinux.0.0"
SRCREV  = "a1288d27523bf54defe95787e87b5ee052d34055"

inherit module

MAKE_TARGETS = "modules"
