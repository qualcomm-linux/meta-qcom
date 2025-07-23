SUMMARY = "Hexagon DSP binaries for Qualcomm hardware"
DESCRIPTION = "Hexagon DSP binaries is a package distributed alongside the \
Linux firmware release. It contains libraries and executables to be used \
with the corresponding DSP firmware using the FastRPC interface in order \
to provide additional functionality by the DSPs."

LICENSE = " \
    dspso-WHENCE \
    & dspso-qcom \
    & dspso-qcom-2 \
"
LIC_FILES_CHKSUM = "\
    file://LICENSE.qcom;md5=56e86b6c508490dadc343f39468b5f5e \
    file://LICENSE.qcom-2;md5=165287851294f2fb8ac8cbc5e24b02b0 \
    file://WHENCE;md5=d087a91a94b0dd5f6592fcd16a07a869 \
"
NO_GENERIC_LICENSE[dspso-qcom] = "LICENSE.qcom"
NO_GENERIC_LICENSE[dspso-qcom-2] = "LICENSE.qcom-2"
NO_GENERIC_LICENSE[dspso-WHENCE] = "WHENCE"

SRC_URI = " \
    git://github.com/linux-msm/dsp-binaries;protocol=https;branch=trunk \
"

SRCREV = "4195fbd4da54f3830af2c01d141c30051f446e51"

inherit allarch

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_DEFAULT_DEPS = "1"

do_install () {
	oe_runmake install 'DESTDIR=${D}'
}

PACKAGES =+ "\
    ${PN}-qcom-db820c-adsp \
    ${PN}-qcom-qcs8300-ride-adsp \
    ${PN}-qcom-qcs8300-ride-cdsp \
    ${PN}-qcom-qcs8300-ride-gdsp \
    ${PN}-qcom-sa8775p-ride-adsp \
    ${PN}-qcom-sa8775p-ride-cdsp \
    ${PN}-qcom-sa8775p-ride-gdsp \
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

LICENSE:${PN} = "dspso-WHENCE"
LICENSE:${PN}-qcom-db820c-adsp = "dspso-qcom"
LICENSE:${PN}-qcom-qcs8300-ride-adsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-qcs8300-ride-cdsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-qcs8300-ride-gdsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-sa8775p-ride-adsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-sa8775p-ride-cdsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-sa8775p-ride-gdsp = "dspso-qcom-2"
LICENSE:${PN}-thundercomm-db845c-adsp = "dspso-qcom"
LICENSE:${PN}-thundercomm-db845c-cdsp = "dspso-qcom"
LICENSE:${PN}-thundercomm-db845c-sdsp = "dspso-qcom"
LICENSE:${PN}-thundercomm-rb1-adsp = "dspso-qcom"
LICENSE:${PN}-thundercomm-rb2-adsp = "dspso-qcom"
LICENSE:${PN}-thundercomm-rb2-cdsp = "dspso-qcom"
LICENSE:${PN}-thundercomm-rb3gen2-adsp = "dspso-qcom"
LICENSE:${PN}-thundercomm-rb3gen2-cdsp = "dspso-qcom"
LICENSE:${PN}-thundercomm-rb5-adsp = "dspso-qcom"
LICENSE:${PN}-thundercomm-rb5-cdsp = "dspso-qcom"
LICENSE:${PN}-thundercomm-rb5-sdsp = "dspso-qcom"

RDEPENDS:${PN}-qcom-db820c-adsp = "linux-firmware-qcom-apq8096-audio (= 1:${PV})"
RDEPENDS:${PN}-qcom-qcs8300-ride-adsp = "linux-firmware-qcom-qcs8300-audio (= 1:${PV})"
RDEPENDS:${PN}-qcom-qcs8300-ride-cdsp = "linux-firmware-qcom-qcs8300-compute (= 1:${PV})"
RDEPENDS:${PN}-qcom-qcs8300-ride-gdsp = "linux-firmware-qcom-qcs8300-generalpurpose (= 1:${PV})"
RDEPENDS:${PN}-qcom-sa8775p-ride-adsp = "linux-firmware-qcom-sa8775p-audio (= 1:${PV})"
RDEPENDS:${PN}-qcom-sa8775p-ride-cdsp = "linux-firmware-qcom-sa8775p-compute (= 1:${PV})"
RDEPENDS:${PN}-qcom-sa8775p-ride-gdsp = "linux-firmware-qcom-sa8775p-generalpurpose (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-db845c-adsp = "linux-firmware-qcom-sdm845-audio (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-db845c-cdsp = "linux-firmware-qcom-sdm845-compute (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-db845c-sdsp = "linux-firmware-qcom-sdm845-thundercomm-db845c-sensors (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-rb1-adsp = "linux-firmware-qcom-qcm2290-audio (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-rb2-adsp = "linux-firmware-qcom-qrb4210-audio (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-rb2-cdsp = "linux-firmware-qcom-qrb4210-compute (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-rb3gen2-adsp = "linux-firmware-qcom-qcm6490-audio (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-rb3gen2-cdsp = "linux-firmware-qcom-qcm6490-compute (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-rb5-adsp = "linux-firmware-qcom-sm8250-audio (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-rb5-cdsp = "linux-firmware-qcom-sm8250-compute (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-rb5-sdsp = "linux-firmware-qcom-sm8250-thundercomm-rb5-sensors (= 1:${PV})"

FILES:${PN}-qcom-db820c-adsp = "${datadir}/qcom/apq8096/Qualcomm/db820c/dsp/adsp"
FILES:${PN}-qcom-qcs8300-ride-adsp = "${datadir}/qcom/qcs8300/Qualcomm/QCS8300-RIDE/dsp/adsp"
FILES:${PN}-qcom-qcs8300-ride-cdsp = "${datadir}/qcom/qcs8300/Qualcomm/QCS8300-RIDE/dsp/cdsp*"
FILES:${PN}-qcom-qcs8300-ride-gdsp = "${datadir}/qcom/qcs8300/Qualcomm/QCS8300-RIDE/dsp/gdsp*"
FILES:${PN}-qcom-sa8775p-ride-adsp = "${datadir}/qcom/sa8775p/Qualcomm/SA8775P-RIDE/dsp/adsp"
FILES:${PN}-qcom-sa8775p-ride-cdsp = "${datadir}/qcom/sa8775p/Qualcomm/SA8775P-RIDE/dsp/cdsp*"
FILES:${PN}-qcom-sa8775p-ride-gdsp = "${datadir}/qcom/sa8775p/Qualcomm/SA8775P-RIDE/dsp/gdsp*"
FILES:${PN}-thundercomm-db845c-adsp = "${datadir}/qcom/sdm845/Thundercomm/db845c/dsp/adsp"
FILES:${PN}-thundercomm-db845c-cdsp = "${datadir}/qcom/sdm845/Thundercomm/db845c/dsp/cdsp"
FILES:${PN}-thundercomm-db845c-sdsp = "${datadir}/qcom/sdm845/Thundercomm/db845c/dsp/sdsp"
FILES:${PN}-thundercomm-rb1-adsp = "${datadir}/qcom/qcm2290/Thundercomm/RB1/dsp/adsp"
FILES:${PN}-thundercomm-rb2-adsp = "${datadir}/qcom/qrb4210/Thundercomm/RB2/dsp/adsp"
FILES:${PN}-thundercomm-rb2-cdsp = "${datadir}/qcom/qrb4210/Thundercomm/RB2/dsp/cdsp"
FILES:${PN}-thundercomm-rb3gen2-adsp = "${datadir}/qcom/qcm6490/Thundercomm/RB3gen2/dsp/adsp"
FILES:${PN}-thundercomm-rb3gen2-cdsp = "${datadir}/qcom/qcm6490/Thundercomm/RB3gen2/dsp/cdsp"
FILES:${PN}-thundercomm-rb5-adsp = "${datadir}/qcom/sm8250/Thundercomm/RB5/dsp/adsp"
FILES:${PN}-thundercomm-rb5-cdsp = "${datadir}/qcom/sm8250/Thundercomm/RB5/dsp/cdsp"
FILES:${PN}-thundercomm-rb5-sdsp = "${datadir}/qcom/sm8250/Thundercomm/RB5/dsp/sdsp"

INSANE_SKIP:${PN}-qcom-db820c-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-qcs8300-ride-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-qcs8300-ride-cdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-qcs8300-ride-gdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-sa8775p-ride-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-sa8775p-ride-cdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-sa8775p-ride-gdsp = "arch libdir file-rdeps textrel"
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
