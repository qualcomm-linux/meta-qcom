SUMMARY = "Camera metadata library"
DESCRIPTION = "This recipe provides the camera metadata library"

HOMEPAGE = "https://github.com/qualcomm-linux/camx-metadata"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRCREV = "d3205c55104e78631c5fb65a347b9440c5cdb9d3"
SRC_URI = "git://github.com/qualcomm-linux/camx-metadata;protocol=https;branch=main"

EXTRA_OECMAKE = "-DPROJECT_VERSION=${PV}"

inherit cmake
