SUMMARY = "Qualcomm Linux Embedded Camera Service"
DESCRIPTION = "Qualcomm Linux Embedded Camera Service with client and server modules."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2998c54c288b081076c9af987bdf4838"

HOMEPAGE = "https://github.com/qualcomm/camera-service"

SRC_URI = "\
    git://github.com/qualcomm/camera-service.git;branch=main;protocol=https \
"

SRCREV = "0a14da9e65fb4c4bd393b8e7706fe425b7968f56"

PV = "0.0+git"

inherit cmake pkgconfig
