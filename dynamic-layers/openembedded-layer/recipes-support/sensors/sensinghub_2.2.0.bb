SUMMARY = "Qualcomm Sensing hub library"
DESCRIPTION = "Userspace libraries that provides interface to interact with Qualcomm Sensing Hub"
HOMEPAGE = "https://github.com/qualcomm/sensinghub"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=9701d0ef17353f1d05d7b74c8712ebbd"

SRCREV = "cc4f596e119da90fad9efa54e744c5fca333d06f"

SRC_URI = "git://github.com/qualcomm/sensinghub.git;protocol=https;branch=main;tag=v${PV}"

DEPENDS = "protobuf-camx protobuf-camx-native glib-2.0 nanopb-runtime nanopb-generator-native"

inherit autotools pkgconfig

EXTRA_OECONF = "--enable-versioned-lib"
