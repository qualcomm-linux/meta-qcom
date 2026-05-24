inherit allarch
require include/common-location-defines.inc
require include/location-license-bsd3-clause.inc

DESCRIPTION = "location api interface"

SRCPROJECT = "git://git.codelinaro.org/clo/le/platform/hardware/qcom/gps.git;protocol=https"
SRCBRANCH  = "location.lnx.0.0"
SRCREV     = "18cec4cd877f758292f5252d47c755b50f2838b3"

SRC_URI = "${SRCPROJECT};branch=${SRCBRANCH};destsuffix=hardware/qcom/gps"
S = "${UNPACKDIR}/hardware/qcom/gps/location"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${includedir}
    install -m 644 ${S}/*.h ${D}${includedir}
}
