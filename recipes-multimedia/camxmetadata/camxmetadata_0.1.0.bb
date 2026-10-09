SUMMARY = "Camera metadata library"
DESCRIPTION = "This recipe provides the camera metadata library"

HOMEPAGE = "https://github.com/qualcomm-linux/camx-metadata"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=9d0e7af0ecbaad8dd37fac677a1f9411"

SRCREV = "fad945e0a78d121e4ab23c6f340407ba33f2dfa3"
SRC_URI = "git://github.com/qualcomm-linux/camx-metadata;protocol=https;branch=main"

EXTRA_OECMAKE = "-DPROJECT_VERSION=${PV}"

inherit cmake
