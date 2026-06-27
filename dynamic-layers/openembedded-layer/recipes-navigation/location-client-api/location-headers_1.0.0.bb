SUMMARY = "Header-only package for Location API and PLA interfaces"

DESCRIPTION = "This recipe provides header files for the Qualcomm Location \
API and platform abstraction layer (PLA) interfaces which includes definitions \
required for applications and libraries to integrate with the Qualcomm location stack"

inherit allarch
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=8ea33b4e44af458236377788d0c61379"

SRC_URI = "git://github.com/qualcomm-linux/location-hal-qcom.git;protocol=https;branch=location.lnx.0.0;tag=v${PV}"
SRCREV  = "4a35b86db087f7b002ccce6edd2ac6da2b1f9b8d"

S = "${UNPACKDIR}/${BP}"

do_install() {
    install -d ${D}${includedir}
    install -m 644 ${S}/location/*.h ${D}${includedir}
    install -m 644 ${S}/pla/oe/*.h ${D}${includedir}
}

do_configure[noexec] = "1"
do_compile[noexec] = "1"
