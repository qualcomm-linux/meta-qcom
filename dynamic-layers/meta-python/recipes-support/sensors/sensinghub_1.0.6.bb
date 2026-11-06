SUMMARY = "Qualcomm Sensing hub library"
DESCRIPTION = "Userspace libraries that provides interface to interact with Qualcomm Sensing Hub"
HOMEPAGE = "https://github.com/qualcomm/sensinghub"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=9701d0ef17353f1d05d7b74c8712ebbd"

SRCREV = "c08dcfa05a39aaf7d9b7f3241f612d29a2df1073"

SRC_URI = "git://github.com/qualcomm/sensinghub.git;protocol=https;branch=main;tag=v${PV}"

DEPENDS = "protobuf protobuf-native glib-2.0"

inherit autotools pkgconfig

EXTRA_OECONF = "--enable-versioned-lib"
