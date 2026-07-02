SUMMARY = "Header-only package for Location HAL and PLA interfaces"

DESCRIPTION = "This recipe provides header files for the Qualcomm Location \
HAL and platform abstraction layer (PLA) interfaces which includes definitions \
required for applications and libraries to integrate with the Qualcomm location stack"

inherit allarch
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2998c54c288b081076c9af987bdf4838"

SRC_URI = "git://github.com/qualcomm-linux/location-hal-qcom.git;protocol=https;branch=location.lnx.0.0;tag=v${PV}"
SRCREV  = "3082a1007d01c9433814173196f80ff8d893287e"

S = "${UNPACKDIR}/${BP}"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${includedir}
    install -m 644 ${S}/location/*.h ${D}${includedir}
    install -m 644 ${S}/pla/oe/*.h ${D}${includedir}
}
