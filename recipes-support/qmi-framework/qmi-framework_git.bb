SUMMARY = "QMI Framework"
DESCRIPTION = "QMI Framework is a messaging interface, \
enabling users to implement clients and servers for inter-process communication (IPC)."

HOMEPAGE = "https://github.com/quic/qmi-framework"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=65b8cd575e75211d9d4ca8603167da1c"

inherit autotools pkgconfig
AUTOTOOLS_AUTORECONF = "yes"
DEPENDS += "glib-2.0"

SRCREV = "0bdd9526ec170a98bf14a1bf7b4284c1b9164c09"
SRC_URI = "git://github.com/quic/qmi-framework.git;protocol=https;branch=main"
PV = "0.1.0"

