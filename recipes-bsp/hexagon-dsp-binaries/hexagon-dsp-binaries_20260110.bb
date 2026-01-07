SUMMARY = "Hexagon DSP binaries for Qualcomm hardware"
DESCRIPTION = "Hexagon DSP binaries is a package distributed alongside the \
Linux firmware release. It contains libraries and executables to be used \
with the corresponding DSP firmware using the FastRPC interface in order \
to provide additional functionality by the DSPs."

LICENSE = " \
    dspso-WHENCE \
    & dspso-qcom \
    & dspso-qcom-2 \
    & MIT \
"
LIC_FILES_CHKSUM = "\
    file://LICENSE.qcom;md5=56e86b6c508490dadc343f39468b5f5e \
    file://LICENSE.qcom-2;md5=165287851294f2fb8ac8cbc5e24b02b0 \
    file://WHENCE;md5=fe9e5f1940df84614b02a94b4f85e73d \
    file://00-hexagon-dsp-binaries.yaml;endline=4;md5=1e22ea93511ef71fecaca9fc16f1355e \
"
NO_GENERIC_LICENSE[dspso-qcom] = "LICENSE.qcom"
NO_GENERIC_LICENSE[dspso-qcom-2] = "LICENSE.qcom-2"
NO_GENERIC_LICENSE[dspso-WHENCE] = "WHENCE"

SRC_URI = " \
    git://github.com/linux-msm/dsp-binaries;protocol=https;branch=trunk;tag=${PV} \
"

SRCREV = "9efc74739b457bed846e53bb8cf46d524ce58791"

inherit allarch

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_DEFAULT_DEPS = "1"

do_install () {
	oe_runmake install 'DESTDIR=${D}'
}

PACKAGE_BEFORE_PN =+ "\
    ${PN}-conf \
    ${PN}-qcom-db820c-adsp \
    ${PN}-qcom-iq8275-evk-adsp \
    ${PN}-qcom-iq8275-evk-cdsp \
    ${PN}-qcom-iq8275-evk-gdsp \
    ${PN}-qcom-iq9075-evk-adsp \
    ${PN}-qcom-iq9075-evk-cdsp \
    ${PN}-qcom-iq9075-evk-gdsp \
    ${PN}-qcom-hamoa-iot-evk-adsp \
    ${PN}-qcom-hamoa-iot-evk-cdsp \
    ${PN}-qcom-kaanapali-mtp-adsp \
    ${PN}-qcom-kaanapali-mtp-cdsp \
    ${PN}-qcom-qcs615-ride-adsp \
    ${PN}-qcom-qcs615-ride-cdsp \
    ${PN}-qcom-qcs8300-ride-adsp \
    ${PN}-qcom-qcs8300-ride-cdsp \
    ${PN}-qcom-qcs8300-ride-gdsp \
    ${PN}-qcom-sa8775p-ride-adsp \
    ${PN}-qcom-sa8775p-ride-cdsp \
    ${PN}-qcom-sa8775p-ride-gdsp \
    ${PN}-qcom-sm8750-mtp-adsp \
    ${PN}-qcom-sm8750-mtp-cdsp \
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
LICENSE:${PN}-conf = "MIT"
LICENSE:${PN}-qcom-db820c-adsp = "dspso-qcom"
LICENSE:${PN}-qcom-iq8275-evk-adsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-iq8275-evk-cdsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-iq8275-evk-gdsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-iq9075-evk-adsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-iq9075-evk-cdsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-iq9075-evk-gdsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-hamoa-iot-evk-adsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-hamoa-iot-evk-cdsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-kaanapali-mtp-adsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-kaanapali-mtp-cdsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-qcs615-ride-adsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-qcs615-ride-cdsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-qcs8300-ride-adsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-qcs8300-ride-cdsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-qcs8300-ride-gdsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-sa8775p-ride-adsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-sa8775p-ride-cdsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-sa8775p-ride-gdsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-sm8750-mtp-adsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-sm8750-mtp-cdsp = "dspso-qcom-2"
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

RDEPENDS:${PN}-qcom-db820c-adsp = "${PN}-conf linux-firmware-qcom-apq8096-audio (= 1:${PV})"
RDEPENDS:${PN}-qcom-iq8275-evk-adsp = "${PN}-conf linux-firmware-qcom-qcs8300-audio (= 1:${PV})"
RDEPENDS:${PN}-qcom-iq8275-evk-adsp += "${PN}-qcom-qcs8300-ride-adsp"
RDEPENDS:${PN}-qcom-iq8275-evk-cdsp = "${PN}-conf linux-firmware-qcom-qcs8300-compute (= 1:${PV})"
RDEPENDS:${PN}-qcom-iq8275-evk-cdsp += "${PN}-qcom-qcs8300-ride-cdsp"
RDEPENDS:${PN}-qcom-iq8275-evk-gdsp = "${PN}-conf linux-firmware-qcom-qcs8300-generalpurpose (= 1:${PV})"
RDEPENDS:${PN}-qcom-iq8275-evk-gdsp += "${PN}-qcom-qcs8300-ride-gdsp"
RDEPENDS:${PN}-qcom-iq9075-evk-adsp = "${PN}-conf linux-firmware-qcom-sa8775p-audio (= 1:${PV})"
RDEPENDS:${PN}-qcom-iq9075-evk-adsp += "${PN}-qcom-sa8775p-ride-adsp"
RDEPENDS:${PN}-qcom-iq9075-evk-cdsp = "${PN}-conf linux-firmware-qcom-sa8775p-compute (= 1:${PV})"
RDEPENDS:${PN}-qcom-iq9075-evk-cdsp += "${PN}-qcom-sa8775p-ride-cdsp"
RDEPENDS:${PN}-qcom-iq9075-evk-gdsp = "${PN}-conf linux-firmware-qcom-sa8775p-generalpurpose (= 1:${PV})"
RDEPENDS:${PN}-qcom-iq9075-evk-gdsp += "${PN}-qcom-sa8775p-ride-gdsp"
RDEPENDS:${PN}-qcom-hamoa-iot-evk-adsp = "${PN}-conf linux-firmware-qcom-x1e80100-audio (= 1:${PV})"
RDEPENDS:${PN}-qcom-hamoa-iot-evk-cdsp = "${PN}-conf linux-firmware-qcom-x1e80100-compute (= 1:${PV})"
RDEPENDS:${PN}-qcom-kaanapali-mtp-adsp = "${PN}-conf linux-firmware-qcom-kaanapali-audio (= 1:${PV})"
RDEPENDS:${PN}-qcom-kaanapali-mtp-cdsp = "${PN}-conf linux-firmware-qcom-kaanapali-compute (= 1:${PV})"
RDEPENDS:${PN}-qcom-qcs615-ride-adsp = "${PN}-conf linux-firmware-qcom-qcs615-audio (= 1:${PV})"
RDEPENDS:${PN}-qcom-qcs615-ride-cdsp = "${PN}-conf linux-firmware-qcom-qcs615-compute (= 1:${PV})"
RDEPENDS:${PN}-qcom-qcs8300-ride-adsp = "${PN}-conf linux-firmware-qcom-qcs8300-audio (= 1:${PV})"
RDEPENDS:${PN}-qcom-qcs8300-ride-cdsp = "${PN}-conf linux-firmware-qcom-qcs8300-compute (= 1:${PV})"
RDEPENDS:${PN}-qcom-qcs8300-ride-gdsp = "${PN}-conf linux-firmware-qcom-qcs8300-generalpurpose (= 1:${PV})"
RDEPENDS:${PN}-qcom-sa8775p-ride-adsp = "${PN}-conf linux-firmware-qcom-sa8775p-audio (= 1:${PV})"
RDEPENDS:${PN}-qcom-sa8775p-ride-cdsp = "${PN}-conf linux-firmware-qcom-sa8775p-compute (= 1:${PV})"
RDEPENDS:${PN}-qcom-sa8775p-ride-gdsp = "${PN}-conf linux-firmware-qcom-sa8775p-generalpurpose (= 1:${PV})"
RDEPENDS:${PN}-qcom-sm8750-mtp-adsp = "${PN}-conf linux-firmware-qcom-sa8775p-audio (= 1:${PV})"
RDEPENDS:${PN}-qcom-sm8750-mtp-cdsp = "${PN}-conf linux-firmware-qcom-sa8775p-compute (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-db845c-adsp = "${PN}-conf linux-firmware-qcom-sdm845-audio (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-db845c-cdsp = "${PN}-conf linux-firmware-qcom-sdm845-compute (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-db845c-sdsp = "${PN}-conf linux-firmware-qcom-sdm845-thundercomm-db845c-sensors (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-rb1-adsp = "${PN}-conf linux-firmware-qcom-qcm2290-audio (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-rb2-adsp = "${PN}-conf linux-firmware-qcom-qrb4210-audio (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-rb2-cdsp = "${PN}-conf linux-firmware-qcom-qrb4210-compute (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-rb3gen2-adsp = "${PN}-conf linux-firmware-qcom-qcm6490-audio (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-rb3gen2-cdsp = "${PN}-conf linux-firmware-qcom-qcm6490-compute (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-rb5-adsp = "${PN}-conf linux-firmware-qcom-sm8250-audio (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-rb5-cdsp = "${PN}-conf linux-firmware-qcom-sm8250-compute (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-rb5-sdsp = "${PN}-conf linux-firmware-qcom-sm8250-thundercomm-rb5-sensors (= 1:${PV})"

# Keep the base package empty so that one can choose which files
# to include and do not pull all of them all in.
FILES:${PN} = ""
ALLOW_EMPTY:${PN} = "1"

FILES:${PN}-conf = "${datadir}/qcom/conf.d"

FILES:${PN}-qcom-db820c-adsp = "${datadir}/qcom/apq8096/Qualcomm/db820c/dsp/adsp"
FILES:${PN}-qcom-iq8275-evk-adsp = "${datadir}/qcom/qcs8300/Qualcomm/IQ8275-EVK/dsp/adsp"
FILES:${PN}-qcom-iq8275-evk-cdsp = "${datadir}/qcom/qcs8300/Qualcomm/IQ8275-EVK/dsp/cdsp*"
FILES:${PN}-qcom-iq8275-evk-gdsp = "${datadir}/qcom/qcs8300/Qualcomm/IQ8275-EVK/dsp/gdsp*"
FILES:${PN}-qcom-iq9075-evk-adsp = "${datadir}/qcom/sa8775p/Qualcomm/IQ9075-EVK/dsp/adsp"
FILES:${PN}-qcom-iq9075-evk-cdsp = "${datadir}/qcom/sa8775p/Qualcomm/IQ9075-EVK/dsp/cdsp*"
FILES:${PN}-qcom-iq9075-evk-gdsp = "${datadir}/qcom/sa8775p/Qualcomm/IQ9075-EVK/dsp/gdsp*"
FILES:${PN}-qcom-hamoa-iot-evk-adsp = "${datadir}/qcom/x1e80100/Qualcomm/Hamoa-IoT-EVK/dsp/adsp*"
FILES:${PN}-qcom-hamoa-iot-evk-cdsp = "${datadir}/qcom/x1e80100/Qualcomm/Hamoa-IoT-EVK/dsp/cdsp*"
FILES:${PN}-qcom-kaanapali-mtp-adsp = "${datadir}/qcom/kaanapali/Qualcomm/Kaanapali-MTP/dsp/adsp*"
FILES:${PN}-qcom-kaanapali-mtp-cdsp = "${datadir}/qcom/kaanapali/Qualcomm/Kaanapali-MTP/dsp/cdsp*"
FILES:${PN}-qcom-qcs615-ride-adsp = "${datadir}/qcom/qcs615/Qualcomm/QCS615-RIDE/dsp/adsp"
FILES:${PN}-qcom-qcs615-ride-cdsp = "${datadir}/qcom/qcs615/Qualcomm/QCS615-RIDE/dsp/cdsp*"
FILES:${PN}-qcom-qcs8300-ride-adsp = "${datadir}/qcom/qcs8300/Qualcomm/QCS8300-RIDE/dsp/adsp"
FILES:${PN}-qcom-qcs8300-ride-cdsp = "${datadir}/qcom/qcs8300/Qualcomm/QCS8300-RIDE/dsp/cdsp*"
FILES:${PN}-qcom-qcs8300-ride-gdsp = "${datadir}/qcom/qcs8300/Qualcomm/QCS8300-RIDE/dsp/gdsp*"
FILES:${PN}-qcom-sa8775p-ride-adsp = "${datadir}/qcom/sa8775p/Qualcomm/SA8775P-RIDE/dsp/adsp"
FILES:${PN}-qcom-sa8775p-ride-cdsp = "${datadir}/qcom/sa8775p/Qualcomm/SA8775P-RIDE/dsp/cdsp*"
FILES:${PN}-qcom-sa8775p-ride-gdsp = "${datadir}/qcom/sa8775p/Qualcomm/SA8775P-RIDE/dsp/gdsp*"
FILES:${PN}-qcom-sm8750-mtp-adsp = "${datadir}/qcom/sm8750/Qualcomm/SM8750-MTP/dsp/adsp"
FILES:${PN}-qcom-sm8750-mtp-cdsp = "${datadir}/qcom/sm8750/Qualcomm/SM8750-MTP/dsp/cdsp*"
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
INSANE_SKIP:${PN}-qcom-hamoa-iot-evk-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-hamoa-iot-evk-cdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-kaanapali-mtp-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-kaanapali-mtp-cdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-qcs615-ride-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-qcs615-ride-cdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-qcs8300-ride-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-qcs8300-ride-cdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-qcs8300-ride-gdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-sa8775p-ride-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-sa8775p-ride-cdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-sa8775p-ride-gdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-sm8750-mtp-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-sm8750-mtp-cdsp = "arch libdir file-rdeps textrel"
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
