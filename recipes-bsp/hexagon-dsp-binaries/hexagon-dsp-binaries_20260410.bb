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
    file://WHENCE;md5=84a238ca84b2b4db57cf6f829d317721 \
    file://conf.d/hexagon-dsp-binaries-qualcomm-iq9075-evk.yaml;endline=2;md5=077232564320a8fce4ea446daad3d726 \
"
NO_GENERIC_LICENSE[dspso-qcom] = "LICENSE.qcom"
NO_GENERIC_LICENSE[dspso-qcom-2] = "LICENSE.qcom-2"
NO_GENERIC_LICENSE[dspso-WHENCE] = "WHENCE"

SRC_URI = " \
    git://github.com/linux-msm/dsp-binaries;protocol=https;branch=trunk;tag=${PV} \
"

SRCREV = "123bddd9cc886b61ece87d7d63b9ceed0654d3a1"

inherit allarch

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_DEFAULT_DEPS = "1"

do_install () {
	oe_runmake install 'DESTDIR=${D}'
}

PACKAGE_BEFORE_PN =+ "\
    ${PN}-conf \
    ${PN}-qcom-arduino-monza-adsp \
    ${PN}-qcom-arduino-monza-cdsp \
    ${PN}-qcom-arduino-monza-gdsp \
    ${PN}-qcom-db820c-adsp \
    ${PN}-qcom-glymur-crd-adsp \
    ${PN}-qcom-glymur-crd-cdsp \
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
    ${PN}-qcom-sdm845-hdk-adsp \
    ${PN}-qcom-sdm845-hdk-cdsp \
    ${PN}-qcom-sm8750-mtp-adsp \
    ${PN}-qcom-sm8750-mtp-cdsp \
    ${PN}-radxa-dragon-q6a-adsp \
    ${PN}-radxa-dragon-q6a-cdsp \
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
    ${PN}-thundercomm-rubikpi3-adsp \
    ${PN}-thundercomm-rubikpi3-cdsp \
"

LICENSE:${PN} = "dspso-WHENCE"
LICENSE:${PN}-conf = "MIT"
LICENSE:${PN}-qcom-arduino-monza-adsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-arduino-monza-cdsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-arduino-monza-gdsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-db820c-adsp = "dspso-qcom"
LICENSE:${PN}-qcom-glymur-crd-adsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-glymur-crd-cdsp = "dspso-qcom-2"
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
LICENSE:${PN}-qcom-sdm845-hdk-adsp = "dspso-qcom"
LICENSE:${PN}-qcom-sdm845-hdk-cdsp = "dspso-qcom"
LICENSE:${PN}-qcom-sm8750-mtp-adsp = "dspso-qcom-2"
LICENSE:${PN}-qcom-sm8750-mtp-cdsp = "dspso-qcom-2"
LICENSE:${PN}-radxa-dragon-q6a-adsp = "dspso-qcom"
LICENSE:${PN}-radxa-dragon-q6a-cdsp = "dspso-qcom"
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
LICENSE:${PN}-thundercomm-rubikpi3-adsp = "dspso-qcom"
LICENSE:${PN}-thundercomm-rubikpi3-cdsp = "dspso-qcom"

RDEPENDS:${PN}-qcom-arduino-monza-adsp = "${PN}-conf linux-firmware-qcom-qcs8300-audio (= 1:${PV})"
RDEPENDS:${PN}-qcom-arduino-monza-adsp += "${PN}-qcom-qcs8300-ride-adsp"
RDEPENDS:${PN}-qcom-arduino-monza-cdsp = "${PN}-conf linux-firmware-qcom-qcs8300-compute (= 1:${PV})"
RDEPENDS:${PN}-qcom-arduino-monza-cdsp += "${PN}-qcom-qcs8300-ride-cdsp"
RDEPENDS:${PN}-qcom-arduino-monza-gdsp = "${PN}-conf linux-firmware-qcom-qcs8300-generalpurpose (= 1:${PV})"
RDEPENDS:${PN}-qcom-arduino-monza-gdsp += "${PN}-qcom-qcs8300-ride-gdsp"
RDEPENDS:${PN}-qcom-db820c-adsp = "${PN}-conf linux-firmware-qcom-apq8096-audio (= 1:${PV})"
RDEPENDS:${PN}-qcom-glymur-crd-adsp = "${PN}-conf linux-firmware-qcom-glymur-audio (= 1:${PV})"
RDEPENDS:${PN}-qcom-glymur-crd-cdsp = "${PN}-conf linux-firmware-qcom-glymur-compute (= 1:${PV})"
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
RDEPENDS:${PN}-radxa-dragon-q6a-adsp = "${PN}-conf linux-firmware-qcom-qcs6490-radxa-dragon-q6a-audio (= 1:${PV})"
RDEPENDS:${PN}-radxa-dragon-q6a-cdsp = "${PN}-conf linux-firmware-qcom-qcs6490-radxa-dragon-q6a-compute (= 1:${PV})"
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
RDEPENDS:${PN}-thundercomm-rubikpi3-adsp = "${PN}-conf linux-firmware-qcom-qcs6490-thundercomm-rubikpi3-audio (= 1:${PV})"

# Keep the base package empty so that one can choose which files
# to include and do not pull all of them all in.
FILES:${PN} = ""
ALLOW_EMPTY:${PN} = "1"

FILES:${PN}-conf = "${datadir}/qcom/conf.d"

FILES:${PN}-qcom-arduino-monza-adsp = "${datadir}/qcom/qcs8300/Arduino/Monza/dsp/adsp"
FILES:${PN}-qcom-arduino-monza-cdsp = "${datadir}/qcom/qcs8300/Arduino/Monza/dsp/cdsp*"
FILES:${PN}-qcom-arduino-monza-gdsp = "${datadir}/qcom/qcs8300/Arduino/Monza/dsp/gdsp*"
FILES:${PN}-qcom-db820c-adsp = "${datadir}/qcom/apq8096/Qualcomm/db820c/dsp/adsp"
FILES:${PN}-qcom-glymur-crd-adsp = "${datadir}/qcom/glymur/Qualcomm/Glymur-CRD/dsp/adsp*"
FILES:${PN}-qcom-glymur-crd-cdsp = "${datadir}/qcom/glymur/Qualcomm/Glymur-CRD/dsp/cdsp*"
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
FILES:${PN}-qcom-sdm845-hdk-adsp = "${datadir}/qcom/sdm845/Qualcomm/SDM845-HDK/dsp/adsp"
FILES:${PN}-qcom-sdm845-hdk-cdsp = "${datadir}/qcom/sdm845/Qualcomm/SDM845-HDK/dsp/cdsp*"
FILES:${PN}-qcom-sm8750-mtp-adsp = "${datadir}/qcom/sm8750/Qualcomm/SM8750-MTP/dsp/adsp"
FILES:${PN}-qcom-sm8750-mtp-cdsp = "${datadir}/qcom/sm8750/Qualcomm/SM8750-MTP/dsp/cdsp*"
FILES:${PN}-radxa-dragon-q6a-adsp = "${datadir}/qcom/qcs6490/radxa/dragon-q6a/dsp/adsp"
FILES:${PN}-radxa-dragon-q6a-cdsp = "${datadir}/qcom/qcs6490/radxa/dragon-q6a/dsp/cdsp"
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
FILES:${PN}-thundercomm-rubikpi3-adsp = "${datadir}/qcom/qcs6490/Thundercomm/RubikPi3/dsp/adsp"
FILES:${PN}-thundercomm-rubikpi3-cdsp = "${datadir}/qcom/qcs6490/Thundercomm/RubikPi3/dsp/cdsp"

INSANE_SKIP:${PN}-qcom-db820c-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-glymur-crd-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-glymur-crd-cdsp = "arch libdir file-rdeps textrel"
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
INSANE_SKIP:${PN}-radxa-dragon-q6a-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-radxa-dragon-q6a-cdsp = "arch libdir file-rdeps textrel"
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
INSANE_SKIP:${PN}-thundercomm-rubikpi3-adsp = "arch libdir file-rdeps textrel"

SKIP_FILEDEPS:${PN}-qcom-glymur-crd-adsp = "1"
SKIP_FILEDEPS:${PN}-qcom-glymur-crd-cdsp = "1"
SKIP_FILEDEPS:${PN}-qcom-hamoa-iot-evk-adsp = "1"
SKIP_FILEDEPS:${PN}-qcom-kaanapali-mtp-adsp = "1"
SKIP_FILEDEPS:${PN}-qcom-kaanapali-mtp-cdsp = "1"
SKIP_FILEDEPS:${PN}-qcom-qcs8300-ride-adsp = "1"
SKIP_FILEDEPS:${PN}-qcom-qcs8300-ride-cdsp = "1"
SKIP_FILEDEPS:${PN}-qcom-qcs8300-ride-gdsp = "1"
SKIP_FILEDEPS:${PN}-qcom-sa8775p-ride-adsp = "1"
SKIP_FILEDEPS:${PN}-qcom-sa8775p-ride-cdsp = "1"
SKIP_FILEDEPS:${PN}-qcom-sa8775p-ride-gdsp = "1"
SKIP_FILEDEPS:${PN}-qcom-sm8750-mtp-adsp = "1"
SKIP_FILEDEPS:${PN}-qcom-sm8750-mtp-cdsp = "1"
SKIP_FILEDEPS:${PN}-thundercomm-db845c-sdsp = "1"
SKIP_FILEDEPS:${PN}-thundercomm-rb2-cdsp = "1"
SKIP_FILEDEPS:${PN}-thundercomm-rb3gen2-cdsp = "1"
SKIP_FILEDEPS:${PN}-thundercomm-rb5-cdsp = "1"
