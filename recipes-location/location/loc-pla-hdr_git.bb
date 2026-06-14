inherit allarch
require include/common-location-defines.inc
require include/location-license-bsd3-clause.inc

DESCRIPTION = "GPS Loc Platform Library Abstraction"

SRC_URI = "git://git.codelinaro.org/clo/le/platform/hardware/qcom/gps.git;protocol=https;branch=location.lnx.0.0;destsuffix=hardware/qcom/gps"
SRCREV  = "18cec4cd877f758292f5252d47c755b50f2838b3"

S = "${UNPACKDIR}/hardware/qcom/gps/pla/oe"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${includedir}
    install -m 644 ${S}/*.h ${D}${includedir}
}

