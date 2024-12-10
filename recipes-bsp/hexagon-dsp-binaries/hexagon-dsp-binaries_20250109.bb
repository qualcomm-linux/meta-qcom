SUMMARY = "Hexagon DSP binaries for Qualcomm hardware"
DESCRIPTION = "Hexagon DSP binaries is a package distributed alongside the \
Linux firmware release. It contains libraries and executables to be used \
with the corresponding DSP firmware using the FastRPC interface in order \
to provide additional functionality by the DSPs."

LICENSE = "dspso-qcom"
LIC_FILES_CHKSUM = "file://LICENSE.qcom;md5=56e86b6c508490dadc343f39468b5f5e"
NO_GENERIC_LICENSE[dspso-qcom] = "LICENSE.qcom"

SRC_URI = "git://github.com/linux-msm/dsp-binaries;protocol=https;branch=trunk"

SRCREV = "744ee1450a5e73ab041cb55cb134898e34ebbbbb"

S = "${WORKDIR}/git"

inherit allarch

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_DEFAULT_DEPS = "1"

do_install () {
	oe_runmake install 'DESTDIR=${D}'
        install -m 0644 LICENSE.qcom ${D}${datadir}/qcom/
}

PACKAGES =+ "\
    ${PN}-qcom-license \
    ${PN}-qcom-db820c-adsp \
    ${PN}-thundercomm-db845c-adsp \
    ${PN}-thundercomm-db845c-cdsp \
    ${PN}-thundercomm-db845c-sdsp \
    ${PN}-thundercomm-rb1-adsp \
    ${PN}-thundercomm-rb2-adsp \
    ${PN}-thundercomm-rb2-cdsp \
    ${PN}-thundercomm-rb3gen2-adsp \
    ${PN}-thundercomm-rb3gen2-cdsp \
    ${PN}-thundercomm-rb5-adsp \
    ${PN}-thundercomm-rb5-cdsp \
    ${PN}-thundercomm-rb5-sdsp \
"

RDEPENDS:${PN}-qcom-db820c-adsp = "${PN}-qcom-license linux-firmware-qcom-apq8096-audio (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-db845c-adsp = "${PN}-qcom-license linux-firmware-qcom-sdm845-audio (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-db845c-cdsp = "${PN}-qcom-license linux-firmware-qcom-sdm845-compute (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-db845c-sdsp = "${PN}-qcom-license linux-firmware-qcom-sdm845-thundercomm-db845c-sensors (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-rb1-adsp = "${PN}-qcom-license linux-firmware-qcom-qcm2290-audio (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-rb2-adsp = "${PN}-qcom-license linux-firmware-qcom-qrb4210-audio (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-rb2-cdsp = "${PN}-qcom-license linux-firmware-qcom-qrb4210-compute (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-rb3gen2-adsp = "${PN}-qcom-license linux-firmware-qcom-qrb4210-audio (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-rb3gen2-cdsp = "${PN}-qcom-license linux-firmware-qcom-qrb4210-compute (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-rb5-adsp = "${PN}-qcom-license linux-firmware-qcom-sm8250-audio (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-rb5-cdsp = "${PN}-qcom-license linux-firmware-qcom-sm8250-compute (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-rb5-sdsp = "${PN}-qcom-license linux-firmware-qcom-sm8250-thundercomm-rb5-sensors (= 1:${PV})"

FILES:${PN}-qcom-license   = "${datadir}/qcom/LICENSE.qcom"

FILES:${PN}-qcom-db820c-adsp = "${datadir}/qcom/apq8096/Qualcomm/db820c/dsp/adsp"
FILES:${PN}-thundercomm-db845c-adsp = "${datadir}/qcom/sdm845/Thundercomm/db845c/dsp/adsp"
FILES:${PN}-thundercomm-db845c-cdsp = "${datadir}/qcom/sdm845/Thundercomm/db845c/dsp/cdsp"
FILES:${PN}-thundercomm-db845c-sdsp = "${datadir}/qcom/sdm845/Thundercomm/db845c/dsp/sdsp"
FILES:${PN}-thundercomm-rb1-adsp = "${datadir}/qcom/qcm2290/Thundercomm/RB1/dsp/adsp"
FILES:${PN}-thundercomm-rb2-adsp = "${datadir}/qcom/qrb4210/Thundercomm/RB2/dsp/adsp"
FILES:${PN}-thundercomm-rb2-cdsp = "${datadir}/qcom/qrb4210/Thundercomm/RB2/dsp/cdsp"
FILES:${PN}-thundercomm-rb3gen2-adsp = "${datadir}/qcom/qrb4210/Thundercomm/RB2/dsp/adsp"
FILES:${PN}-thundercomm-rb3gen2-cdsp = "${datadir}/qcom/qrb4210/Thundercomm/RB2/dsp/cdsp"
FILES:${PN}-thundercomm-rb5-adsp = "${datadir}/qcom/sm8250/Thundercomm/RB5/dsp/adsp"
FILES:${PN}-thundercomm-rb5-cdsp = "${datadir}/qcom/sm8250/Thundercomm/RB5/dsp/cdsp"
FILES:${PN}-thundercomm-rb5-sdsp = "${datadir}/qcom/sm8250/Thundercomm/RB5/dsp/sdsp"

INSANE_SKIP:${PN}-qcom-db820c-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-thundercomm-db845c-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-thundercomm-db845c-cdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-thundercomm-db845c-sdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-thundercomm-rb1-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-thundercomm-rb2-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-thundercomm-rb2-cdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-thundercomm-rb3gen2-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-thundercomm-rb3gen2-cdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-thundercomm-rb5-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-thundercomm-rb5-cdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-thundercomm-rb5-sdsp = "arch libdir file-rdeps textrel"
