SUMMARY = "QMI Framework"
DESCRIPTION = "QMI Framework is a messaging library, \
enabling users to implement clients and servers for inter-process communication (IPC)."

HOMEPAGE = "https://github.com/quic/qmi-framework"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=65b8cd575e75211d9d4ca8603167da1c"

DEPENDS = "glib-2.0"
SRCREV = "a82b29b33003213bbf5b553994cffc472a30981f"
PV = "0.1.1"

SRC_URI = "git://github.com/quic/qmi-framework.git;protocol=https;branch=main"

inherit autotools pkgconfig
