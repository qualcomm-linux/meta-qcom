SUMMARY = "Qualcomm QRTR applications and library"
HOMEPAGE = "https://github.com/linux-msm/qrtr.git"
SECTION = "devel"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=15329706fbfcb5fc5edcc1bc7c139da5"

SRCREV = "b51ffaf22707b6000ecfb894c5b750f3bb7843b2"
SRC_URI = "git://github.com/linux-msm/${BPN}.git;branch=master;protocol=https;tag=v${PV}"

inherit meson pkgconfig
