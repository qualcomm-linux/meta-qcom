SUMMARY = "Hexagon DSP binaries for Qualcomm hardware"
DESCRIPTION = "Hexagon DSP binaries is a package distributed alongside the \
Linux firmware release. It contains libraries and executables to be used \
with the corresponding DSP firmware using the FastRPC interface in order \
to provide additional functionality by the DSPs."

LICENSE = " \
    dspso-WHENCE \
    & Firmware-qcom \
    & Firmware-qcom-2 \
    & MIT \
"
NO_GENERIC_LICENSE[Firmware-qcom] = "LICENSE.qcom"
NO_GENERIC_LICENSE[Firmware-qcom-2] = "LICENSE.qcom-2"
NO_GENERIC_LICENSE[dspso-WHENCE] = "WHENCE"

SRC_URI = " \
    git://github.com/linux-msm/dsp-binaries;protocol=https;branch=trunk;tag=${PV} \
"

inherit allarch

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_DEFAULT_DEPS = "1"

do_install () {
	oe_runmake install 'DESTDIR=${D}'
}

PACKAGES_DYNAMIC = "^${PN}-.*-config"

PACKAGE_BEFORE_PN =+ "\
    ${PN}-config-schema \
    ${PN}-arduino-imola-adsp \
    ${PN}-arduino-monza-adsp \
    ${PN}-arduino-monza-cdsp \
    ${PN}-arduino-monza-gdsp \
    ${PN}-qcom-db820c-adsp \
    ${PN}-qcom-eliza-cqs-evk-adsp \
    ${PN}-qcom-eliza-cqs-evk-cdsp \
    ${PN}-qcom-eliza-mtp-adsp \
    ${PN}-qcom-eliza-mtp-cdsp \
    ${PN}-qcom-glymur-crd-adsp \
    ${PN}-qcom-glymur-crd-cdsp \
    ${PN}-qcom-hamoa-iot-evk-adsp \
    ${PN}-qcom-hamoa-iot-evk-cdsp \
    ${PN}-qcom-hawi-mtp-adsp \
    ${PN}-qcom-hawi-mtp-cdsp \
    ${PN}-qcom-iq8275-evk-adsp \
    ${PN}-qcom-iq8275-evk-cdsp \
    ${PN}-qcom-iq8275-evk-gdsp \
    ${PN}-qcom-iq9075-evk-adsp \
    ${PN}-qcom-iq9075-evk-cdsp \
    ${PN}-qcom-iq9075-evk-gdsp \
    ${PN}-qcom-kaanapali-mtp-adsp \
    ${PN}-qcom-kaanapali-mtp-cdsp \
    ${PN}-qcom-lemans-draco-evk-adsp \
    ${PN}-qcom-lemans-draco-evk-cdsp \
    ${PN}-qcom-lemans-draco-evk-gdsp \
    ${PN}-qcom-mahua-crd-adsp \
    ${PN}-qcom-mahua-crd-cdsp \
    ${PN}-qcom-maili-mtp-adsp \
    ${PN}-qcom-maili-mtp-cdsp \
    ${PN}-qcom-monaco-draco-evk-adsp \
    ${PN}-qcom-monaco-draco-evk-cdsp \
    ${PN}-qcom-monaco-draco-evk-gdsp \
    ${PN}-qcom-purwa-iot-evk-adsp \
    ${PN}-qcom-purwa-iot-evk-cdsp \
    ${PN}-qcom-qcm6490-idp-adsp \
    ${PN}-qcom-qcm6490-idp-cdsp \
    ${PN}-qcom-qcs615-ride-adsp \
    ${PN}-qcom-qcs615-ride-cdsp \
    ${PN}-qcom-qcs8300-ride-adsp \
    ${PN}-qcom-qcs8300-ride-cdsp \
    ${PN}-qcom-qcs8300-ride-gdsp \
    ${PN}-qcom-sa8775p-ride-adsp \
    ${PN}-qcom-sa8775p-ride-cdsp \
    ${PN}-qcom-sa8775p-ride-gdsp \
    ${PN}-qcom-sa8797p-ride-embedded-adsp \
    ${PN}-qcom-sa8797p-ride-embedded-cdsp \
    ${PN}-qcom-sdm845-hdk-adsp \
    ${PN}-qcom-sdm845-hdk-cdsp \
    ${PN}-qcom-shikra-cqm-evk-cdsp \
    ${PN}-qcom-shikra-cqs-evk-cdsp \
    ${PN}-qcom-shikra-iqs-evk-cdsp \
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
LICENSE:${PN}-config-schema = "MIT"
LICENSE:${PN}-arduino-imola-adsp = "Firmware-qcom"
LICENSE:${PN}-arduino-monza-adsp = "Firmware-qcom-2"
LICENSE:${PN}-arduino-monza-cdsp = "Firmware-qcom-2"
LICENSE:${PN}-arduino-monza-gdsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-db820c-adsp = "Firmware-qcom"
LICENSE:${PN}-qcom-eliza-cqs-evk-adsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-eliza-cqs-evk-cdsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-eliza-mtp-adsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-eliza-mtp-cdsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-glymur-crd-adsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-glymur-crd-cdsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-hamoa-iot-evk-adsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-hamoa-iot-evk-cdsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-hawi-mtp-adsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-hawi-mtp-cdsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-iq8275-evk-adsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-iq8275-evk-cdsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-iq8275-evk-gdsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-iq9075-evk-adsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-iq9075-evk-cdsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-iq9075-evk-gdsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-kaanapali-mtp-adsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-kaanapali-mtp-cdsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-lemans-draco-evk-adsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-lemans-draco-evk-cdsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-lemans-draco-evk-gdsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-mahua-crd-adsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-mahua-crd-cdsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-maili-mtp-adsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-maili-mtp-cdsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-monaco-draco-evk-adsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-monaco-draco-evk-cdsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-monaco-draco-evk-gdsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-purwa-iot-evk-adsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-purwa-iot-evk-cdsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-qcm6490-idp-adsp = "Firmware-qcom"
LICENSE:${PN}-qcom-qcm6490-idp-cdsp = "Firmware-qcom"
LICENSE:${PN}-qcom-qcs615-ride-adsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-qcs615-ride-cdsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-qcs8300-ride-adsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-qcs8300-ride-cdsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-qcs8300-ride-gdsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-sa8775p-ride-adsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-sa8775p-ride-cdsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-sa8775p-ride-gdsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-sa8797p-ride-embedded-adsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-sa8797p-ride-embedded-cdsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-sdm845-hdk-adsp = "Firmware-qcom"
LICENSE:${PN}-qcom-sdm845-hdk-cdsp = "Firmware-qcom"
LICENSE:${PN}-qcom-shikra-cqm-evk-cdsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-shikra-cqs-evk-cdsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-shikra-iqs-evk-cdsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-sm8750-mtp-adsp = "Firmware-qcom-2"
LICENSE:${PN}-qcom-sm8750-mtp-cdsp = "Firmware-qcom-2"
LICENSE:${PN}-radxa-dragon-q6a-adsp = "Firmware-qcom"
LICENSE:${PN}-radxa-dragon-q6a-cdsp = "Firmware-qcom"
LICENSE:${PN}-thundercomm-db845c-adsp = "Firmware-qcom"
LICENSE:${PN}-thundercomm-db845c-cdsp = "Firmware-qcom"
LICENSE:${PN}-thundercomm-db845c-sdsp = "Firmware-qcom"
LICENSE:${PN}-thundercomm-rb1-adsp = "Firmware-qcom"
LICENSE:${PN}-thundercomm-rb2-adsp = "Firmware-qcom"
LICENSE:${PN}-thundercomm-rb2-cdsp = "Firmware-qcom"
LICENSE:${PN}-thundercomm-rb3gen2-adsp = "Firmware-qcom"
LICENSE:${PN}-thundercomm-rb3gen2-cdsp = "Firmware-qcom"
LICENSE:${PN}-thundercomm-rb5-adsp = "Firmware-qcom"
LICENSE:${PN}-thundercomm-rb5-cdsp = "Firmware-qcom"
LICENSE:${PN}-thundercomm-rb5-sdsp = "Firmware-qcom"
LICENSE:${PN}-thundercomm-rubikpi3-adsp = "Firmware-qcom"
LICENSE:${PN}-thundercomm-rubikpi3-cdsp = "Firmware-qcom"

RDEPENDS:${PN}-arduino-imola-adsp = "${PN}-thundercomm-rb1-adsp (= ${PV})"
RDEPENDS:${PN}-arduino-monza-adsp = "${PN}-qcom-sa8775p-ride-adsp (= ${PV})"
RDEPENDS:${PN}-arduino-monza-cdsp = "${PN}-qcom-qcs8300-ride-cdsp (= ${PV})"
RDEPENDS:${PN}-arduino-monza-gdsp = "${PN}-qcom-sa8775p-ride-gdsp (= ${PV})"
RDEPENDS:${PN}-qcom-db820c-adsp = "linux-firmware-qcom-apq8096-audio (= 1:${PV})"
RDEPENDS:${PN}-qcom-eliza-cqs-evk-adsp = "${PN}-qcom-eliza-mtp-adsp (= ${PV})"
RDEPENDS:${PN}-qcom-eliza-cqs-evk-cdsp = "${PN}-qcom-eliza-mtp-cdsp (= ${PV})"
RDEPENDS:${PN}-qcom-eliza-mtp-adsp = "linux-firmware-qcom-eliza-audio (= 1:${PV})"
RDEPENDS:${PN}-qcom-eliza-mtp-cdsp = "linux-firmware-qcom-eliza-compute (= 1:${PV})"
RDEPENDS:${PN}-qcom-glymur-crd-adsp = "linux-firmware-qcom-glymur-audio (= 1:${PV})"
RDEPENDS:${PN}-qcom-glymur-crd-cdsp = "linux-firmware-qcom-glymur-compute (= 1:${PV})"
RDEPENDS:${PN}-qcom-hamoa-iot-evk-adsp = "linux-firmware-qcom-x1e80100-audio (= 1:${PV})"
RDEPENDS:${PN}-qcom-hamoa-iot-evk-cdsp = "linux-firmware-qcom-x1e80100-compute (= 1:${PV})"
RDEPENDS:${PN}-qcom-hawi-mtp-adsp = "linux-firmware-qcom-hawi-audio (= 1:${PV})"
RDEPENDS:${PN}-qcom-hawi-mtp-cdsp = "linux-firmware-qcom-hawi-compute (= 1:${PV})"
RDEPENDS:${PN}-qcom-iq8275-evk-adsp = "${PN}-qcom-sa8775p-ride-adsp (= ${PV})"
RDEPENDS:${PN}-qcom-iq8275-evk-cdsp = "${PN}-qcom-qcs8300-ride-cdsp (= ${PV})"
RDEPENDS:${PN}-qcom-iq8275-evk-gdsp = "${PN}-qcom-sa8775p-ride-gdsp (= ${PV})"
RDEPENDS:${PN}-qcom-iq9075-evk-adsp = "${PN}-qcom-sa8775p-ride-adsp (= ${PV})"
RDEPENDS:${PN}-qcom-iq9075-evk-cdsp = "${PN}-qcom-sa8775p-ride-cdsp (= ${PV})"
RDEPENDS:${PN}-qcom-iq9075-evk-gdsp = "${PN}-qcom-sa8775p-ride-gdsp (= ${PV})"
RDEPENDS:${PN}-qcom-kaanapali-mtp-adsp = "linux-firmware-qcom-kaanapali-audio (= 1:${PV})"
RDEPENDS:${PN}-qcom-kaanapali-mtp-cdsp = "linux-firmware-qcom-kaanapali-compute (= 1:${PV})"
RDEPENDS:${PN}-qcom-lemans-draco-evk-adsp = "${PN}-qcom-sa8775p-ride-adsp (= ${PV})"
RDEPENDS:${PN}-qcom-lemans-draco-evk-cdsp = "${PN}-qcom-sa8775p-ride-cdsp (= ${PV})"
RDEPENDS:${PN}-qcom-lemans-draco-evk-gdsp = "${PN}-qcom-sa8775p-ride-gdsp (= ${PV})"
RDEPENDS:${PN}-qcom-mahua-crd-adsp = "${PN}-qcom-glymur-crd-adsp (= ${PV})"
RDEPENDS:${PN}-qcom-mahua-crd-cdsp = "${PN}-qcom-glymur-crd-cdsp (= ${PV})"
RDEPENDS:${PN}-qcom-maili-mtp-adsp = "linux-firmware-qcom-maili-audio (= 1:${PV})"
RDEPENDS:${PN}-qcom-maili-mtp-cdsp = "${PN}-qcom-hawi-mtp-cdsp (= ${PV})"
RDEPENDS:${PN}-qcom-monaco-draco-evk-adsp = "${PN}-qcom-sa8775p-ride-adsp (= ${PV})"
RDEPENDS:${PN}-qcom-monaco-draco-evk-cdsp = "${PN}-qcom-qcs8300-ride-cdsp (= ${PV})"
RDEPENDS:${PN}-qcom-monaco-draco-evk-gdsp = "${PN}-qcom-sa8775p-ride-gdsp (= ${PV})"
RDEPENDS:${PN}-qcom-purwa-iot-evk-adsp = "${PN}-qcom-hamoa-iot-evk-adsp (= ${PV})"
RDEPENDS:${PN}-qcom-purwa-iot-evk-cdsp = "${PN}-qcom-hamoa-iot-evk-cdsp (= ${PV})"
RDEPENDS:${PN}-qcom-qcm6490-idp-adsp = "${PN}-thundercomm-rb3gen2-adsp (= ${PV})"
RDEPENDS:${PN}-qcom-qcm6490-idp-cdsp = "${PN}-thundercomm-rb3gen2-cdsp (= ${PV})"
RDEPENDS:${PN}-qcom-qcs615-ride-adsp = "linux-firmware-qcom-qcs615-audio (= 1:${PV})"
RDEPENDS:${PN}-qcom-qcs615-ride-cdsp = "linux-firmware-qcom-qcs615-compute (= 1:${PV})"
RDEPENDS:${PN}-qcom-qcs8300-ride-adsp = "${PN}-qcom-sa8775p-ride-adsp (= ${PV})"
RDEPENDS:${PN}-qcom-qcs8300-ride-cdsp = "linux-firmware-qcom-qcs8300-compute (= 1:${PV})"
RDEPENDS:${PN}-qcom-qcs8300-ride-gdsp = "${PN}-qcom-sa8775p-ride-gdsp (= ${PV})"
RDEPENDS:${PN}-qcom-sa8775p-ride-adsp = "linux-firmware-qcom-sa8775p-audio (= 1:${PV})"
RDEPENDS:${PN}-qcom-sa8775p-ride-cdsp = "linux-firmware-qcom-sa8775p-compute (= 1:${PV})"
RDEPENDS:${PN}-qcom-sa8775p-ride-gdsp = "linux-firmware-qcom-sa8775p-generalpurpose (= 1:${PV})"
RDEPENDS:${PN}-qcom-sa8797p-ride-embedded-adsp = "linux-firmware-qcom-nord-audio (= 1:${PV})"
RDEPENDS:${PN}-qcom-sa8797p-ride-embedded-cdsp = "linux-firmware-qcom-nord-compute (= 1:${PV})"
RDEPENDS:${PN}-qcom-sdm845-hdk-adsp = "${PN}-thundercomm-db845c-adsp (= ${PV})"
RDEPENDS:${PN}-qcom-sdm845-hdk-cdsp = "${PN}-thundercomm-db845c-cdsp (= ${PV})"
RDEPENDS:${PN}-qcom-shikra-cqm-evk-cdsp = "${PN}-qcom-shikra-cqs-evk-cdsp (= ${PV})"
RDEPENDS:${PN}-qcom-shikra-cqs-evk-cdsp = "linux-firmware-qcom-shikra-compute (= 1:${PV})"
RDEPENDS:${PN}-qcom-shikra-iqs-evk-cdsp = "${PN}-qcom-shikra-cqs-evk-cdsp (= ${PV})"
RDEPENDS:${PN}-qcom-sm8750-mtp-adsp = "linux-firmware-qcom-sa8775p-audio (= 1:${PV})"
RDEPENDS:${PN}-qcom-sm8750-mtp-cdsp = "linux-firmware-qcom-sa8775p-compute (= 1:${PV})"
RDEPENDS:${PN}-radxa-dragon-q6a-adsp = "linux-firmware-qcom-qcs6490-radxa-dragon-q6a-audio (= 1:${PV})"
RDEPENDS:${PN}-radxa-dragon-q6a-cdsp = "linux-firmware-qcom-qcs6490-radxa-dragon-q6a-compute (= 1:${PV})"
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
RDEPENDS:${PN}-thundercomm-rubikpi3-adsp = "linux-firmware-qcom-qcs6490-thundercomm-rubikpi3-audio (= 1:${PV})"
RDEPENDS:${PN}-thundercomm-rubikpi3-cdsp = "${PN}-thundercomm-rb3gen2-cdsp (= ${PV})"

python() {
    for p in d.getVar('PACKAGES').split():
        if not p.endswith('dsp'):
            continue

        cfg = (p[:-4] + 'config').replace('-qcom-', '-qualcomm-')
        if cfg not in (d.getVar('RDEPENDS:' + p) or "").split():
            d.prependVar('RDEPENDS:' + p, cfg + ' ')
}

python populate_packages:prepend () {
    def fix_cfg_package(fn, pkg, file_regex, output_pattern, group):
        d.setVar('LICENSE:' + pkg, 'MIT')

    cfgdir = d.expand('${datadir}/qcom/conf.d')
    do_split_packages(d, cfgdir, '^hexagon-dsp-binaries-(.*).yaml',
                      'hexagon-dsp-binaries-%s-config',
                      'Hexagon DSP config file for %s',
                      extra_depends='',
                      hook=fix_cfg_package)
}

# Keep the base package empty so that one can choose which files
# to include and do not pull all of them all in.
FILES:${PN} = ""
ALLOW_EMPTY:${PN} = "1"

FILES:${PN}-config-schema = "${datadir}/qcom/conf.d/schema.json"

FILES:${PN}-arduino-imola-adsp = "${datadir}/qcom/qcm2290/Arduino/Imola/dsp/adsp"
FILES:${PN}-arduino-monza-adsp = "${datadir}/qcom/qcs8300/Arduino/Monza/dsp/adsp"
FILES:${PN}-arduino-monza-cdsp = "${datadir}/qcom/qcs8300/Arduino/Monza/dsp/cdsp*"
FILES:${PN}-arduino-monza-gdsp = "${datadir}/qcom/qcs8300/Arduino/Monza/dsp/gdsp*"
FILES:${PN}-qcom-db820c-adsp = "${datadir}/qcom/apq8096/Qualcomm/db820c/dsp/adsp"
FILES:${PN}-qcom-eliza-cqs-evk-adsp = "${datadir}/qcom/eliza/Qualcomm/Eliza-CQS-EVK/dsp/adsp"
FILES:${PN}-qcom-eliza-cqs-evk-cdsp = "${datadir}/qcom/eliza/Qualcomm/Eliza-CQS-EVK/dsp/cdsp*"
FILES:${PN}-qcom-eliza-mtp-adsp = "${datadir}/qcom/eliza/Qualcomm/Eliza-MTP/dsp/adsp*"
FILES:${PN}-qcom-eliza-mtp-cdsp = "${datadir}/qcom/eliza/Qualcomm/Eliza-MTP/dsp/cdsp*"
FILES:${PN}-qcom-glymur-crd-adsp = "${datadir}/qcom/glymur/Qualcomm/Glymur-CRD/dsp/adsp*"
FILES:${PN}-qcom-glymur-crd-cdsp = "${datadir}/qcom/glymur/Qualcomm/Glymur-CRD/dsp/cdsp*"
FILES:${PN}-qcom-hamoa-iot-evk-adsp = "${datadir}/qcom/x1e80100/Qualcomm/Hamoa-IoT-EVK/dsp/adsp*"
FILES:${PN}-qcom-hamoa-iot-evk-cdsp = "${datadir}/qcom/x1e80100/Qualcomm/Hamoa-IoT-EVK/dsp/cdsp*"
FILES:${PN}-qcom-hawi-mtp-adsp = "${datadir}/qcom/hawi/Qualcomm/Hawi-MTP/dsp/adsp*"
FILES:${PN}-qcom-hawi-mtp-cdsp = "${datadir}/qcom/hawi/Qualcomm/Hawi-MTP/dsp/cdsp*"
FILES:${PN}-qcom-iq8275-evk-adsp = "${datadir}/qcom/qcs8300/Qualcomm/IQ8275-EVK/dsp/adsp"
FILES:${PN}-qcom-iq8275-evk-cdsp = "${datadir}/qcom/qcs8300/Qualcomm/IQ8275-EVK/dsp/cdsp*"
FILES:${PN}-qcom-iq8275-evk-gdsp = "${datadir}/qcom/qcs8300/Qualcomm/IQ8275-EVK/dsp/gdsp*"
FILES:${PN}-qcom-iq9075-evk-adsp = "${datadir}/qcom/sa8775p/Qualcomm/IQ9075-EVK/dsp/adsp"
FILES:${PN}-qcom-iq9075-evk-cdsp = "${datadir}/qcom/sa8775p/Qualcomm/IQ9075-EVK/dsp/cdsp*"
FILES:${PN}-qcom-iq9075-evk-gdsp = "${datadir}/qcom/sa8775p/Qualcomm/IQ9075-EVK/dsp/gdsp*"
FILES:${PN}-qcom-kaanapali-mtp-adsp = "${datadir}/qcom/kaanapali/Qualcomm/Kaanapali-MTP/dsp/adsp*"
FILES:${PN}-qcom-kaanapali-mtp-cdsp = "${datadir}/qcom/kaanapali/Qualcomm/Kaanapali-MTP/dsp/cdsp*"
FILES:${PN}-qcom-lemans-draco-evk-adsp = "${datadir}/qcom/sa8775p/Qualcomm/Lemans-Draco-EVK/dsp/adsp"
FILES:${PN}-qcom-lemans-draco-evk-cdsp = "${datadir}/qcom/sa8775p/Qualcomm/Lemans-Draco-EVK/dsp/cdsp*"
FILES:${PN}-qcom-lemans-draco-evk-gdsp = "${datadir}/qcom/sa8775p/Qualcomm/Lemans-Draco-EVK/dsp/gdsp*"
FILES:${PN}-qcom-mahua-crd-adsp = "${datadir}/qcom/mahua/Qualcomm/Mahua-CRD/dsp/adsp*"
FILES:${PN}-qcom-mahua-crd-cdsp = "${datadir}/qcom/mahua/Qualcomm/Mahua-CRD/dsp/cdsp*"
FILES:${PN}-qcom-maili-mtp-adsp = "${datadir}/qcom/maili/Qualcomm/Maili-MTP/dsp/adsp*"
FILES:${PN}-qcom-maili-mtp-cdsp = "${datadir}/qcom/maili/Qualcomm/Maili-MTP/dsp/cdsp*"
FILES:${PN}-qcom-monaco-draco-evk-adsp = "${datadir}/qcom/qcs8300/Qualcomm/Monaco-Draco-EVK/dsp/adsp"
FILES:${PN}-qcom-monaco-draco-evk-cdsp = "${datadir}/qcom/qcs8300/Qualcomm/Monaco-Draco-EVK/dsp/cdsp*"
FILES:${PN}-qcom-monaco-draco-evk-gdsp = "${datadir}/qcom/qcs8300/Qualcomm/Monaco-Draco-EVK/dsp/gdsp*"
FILES:${PN}-qcom-purwa-iot-evk-adsp = "${datadir}/qcom/x1p42100/Qualcomm/Purwa-IoT-EVK/dsp/adsp"
FILES:${PN}-qcom-purwa-iot-evk-cdsp = "${datadir}/qcom/x1p42100/Qualcomm/Purwa-IoT-EVK/dsp/cdsp"
FILES:${PN}-qcom-qcm6490-idp-adsp = "${datadir}/qcom/qcm6490/Qualcomm/QCM6490-IDP/dsp/adsp"
FILES:${PN}-qcom-qcm6490-idp-cdsp = "${datadir}/qcom/qcm6490/Qualcomm/QCM6490-IDP/dsp/cdsp"
FILES:${PN}-qcom-qcs615-ride-adsp = "${datadir}/qcom/qcs615/Qualcomm/QCS615-RIDE/dsp/adsp"
FILES:${PN}-qcom-qcs615-ride-cdsp = "${datadir}/qcom/qcs615/Qualcomm/QCS615-RIDE/dsp/cdsp*"
FILES:${PN}-qcom-qcs8300-ride-adsp = "${datadir}/qcom/qcs8300/Qualcomm/QCS8300-RIDE/dsp/adsp"
FILES:${PN}-qcom-qcs8300-ride-cdsp = "${datadir}/qcom/qcs8300/Qualcomm/QCS8300-RIDE/dsp/cdsp*"
FILES:${PN}-qcom-qcs8300-ride-gdsp = "${datadir}/qcom/qcs8300/Qualcomm/QCS8300-RIDE/dsp/gdsp*"
FILES:${PN}-qcom-sa8775p-ride-adsp = "${datadir}/qcom/sa8775p/Qualcomm/SA8775P-RIDE/dsp/adsp"
FILES:${PN}-qcom-sa8775p-ride-cdsp = "${datadir}/qcom/sa8775p/Qualcomm/SA8775P-RIDE/dsp/cdsp*"
FILES:${PN}-qcom-sa8775p-ride-gdsp = "${datadir}/qcom/sa8775p/Qualcomm/SA8775P-RIDE/dsp/gdsp*"
FILES:${PN}-qcom-sa8797p-ride-embedded-adsp = "${datadir}/qcom/nord/Qualcomm/SA8797P-Ride-Embedded/dsp/adsp*"
FILES:${PN}-qcom-sa8797p-ride-embedded-cdsp = "${datadir}/qcom/nord/Qualcomm/SA8797P-Ride-Embedded/dsp/cdsp*"
FILES:${PN}-qcom-sdm845-hdk-adsp = "${datadir}/qcom/sdm845/Qualcomm/SDM845-HDK/dsp/adsp"
FILES:${PN}-qcom-sdm845-hdk-cdsp = "${datadir}/qcom/sdm845/Qualcomm/SDM845-HDK/dsp/cdsp*"
FILES:${PN}-qcom-shikra-cqm-evk-cdsp = "${datadir}/qcom/shikra/Qualcomm/Shikra-CQM-EVK/dsp/cdsp"
FILES:${PN}-qcom-shikra-cqs-evk-cdsp = "${datadir}/qcom/shikra/Qualcomm/Shikra-CQS-EVK/dsp/cdsp"
FILES:${PN}-qcom-shikra-iqs-evk-cdsp = "${datadir}/qcom/shikra/Qualcomm/Shikra-IQS-EVK/dsp/cdsp"
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
INSANE_SKIP:${PN}-qcom-eliza-mtp-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-eliza-mtp-cdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-glymur-crd-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-glymur-crd-cdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-hamoa-iot-evk-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-hamoa-iot-evk-cdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-hawi-mtp-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-hawi-mtp-cdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-kaanapali-mtp-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-kaanapali-mtp-cdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-maili-mtp-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-qcs615-ride-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-qcs615-ride-cdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-qcs8300-ride-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-qcs8300-ride-cdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-qcs8300-ride-gdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-sa8775p-ride-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-sa8775p-ride-cdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-sa8775p-ride-gdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-sa8797p-ride-embedded-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-sa8797p-ride-embedded-cdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:${PN}-qcom-shikra-cqs-evk-cdsp = "arch libdir file-rdeps textrel"
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

SKIP_FILEDEPS:${PN}-qcom-eliza-mtp-adsp = "1"
SKIP_FILEDEPS:${PN}-qcom-eliza-mtp-cdsp = "1"
SKIP_FILEDEPS:${PN}-qcom-glymur-crd-adsp = "1"
SKIP_FILEDEPS:${PN}-qcom-glymur-crd-cdsp = "1"
SKIP_FILEDEPS:${PN}-qcom-hamoa-iot-evk-adsp = "1"
SKIP_FILEDEPS:${PN}-qcom-hamoa-iot-evk-cdsp = "1"
SKIP_FILEDEPS:${PN}-qcom-hawi-mtp-adsp = "1"
SKIP_FILEDEPS:${PN}-qcom-hawi-mtp-cdsp = "1"
SKIP_FILEDEPS:${PN}-qcom-kaanapali-mtp-adsp = "1"
SKIP_FILEDEPS:${PN}-qcom-kaanapali-mtp-cdsp = "1"
SKIP_FILEDEPS:${PN}-qcom-maili-mtp-adsp = "1"
SKIP_FILEDEPS:${PN}-qcom-qcs8300-ride-adsp = "1"
SKIP_FILEDEPS:${PN}-qcom-qcs8300-ride-cdsp = "1"
SKIP_FILEDEPS:${PN}-qcom-qcs8300-ride-gdsp = "1"
SKIP_FILEDEPS:${PN}-qcom-sa8775p-ride-adsp = "1"
SKIP_FILEDEPS:${PN}-qcom-sa8775p-ride-cdsp = "1"
SKIP_FILEDEPS:${PN}-qcom-sa8775p-ride-gdsp = "1"
SKIP_FILEDEPS:${PN}-qcom-sa8797p-ride-embedded-adsp = "1"
SKIP_FILEDEPS:${PN}-qcom-sa8797p-ride-embedded-cdsp = "1"
SKIP_FILEDEPS:${PN}-qcom-shikra-cqs-evk-cdsp = "1"
SKIP_FILEDEPS:${PN}-qcom-sm8750-mtp-adsp = "1"
SKIP_FILEDEPS:${PN}-qcom-sm8750-mtp-cdsp = "1"
SKIP_FILEDEPS:${PN}-radxa-dragon-q6a-adsp = "1"
SKIP_FILEDEPS:${PN}-radxa-dragon-q6a-cdsp = "1"
SKIP_FILEDEPS:${PN}-thundercomm-db845c-sdsp = "1"
SKIP_FILEDEPS:${PN}-thundercomm-rb2-cdsp = "1"
SKIP_FILEDEPS:${PN}-thundercomm-rb3gen2-cdsp = "1"
SKIP_FILEDEPS:${PN}-thundercomm-rb5-cdsp = "1"

WARN_QA:append = " hexagon-dsp-binaries-symlink-deps"
QAPKGTEST[hexagon-dsp-binaries-symlink-deps] = "package_qa_check_symlinks"
def package_qa_check_symlinks(pkg, d):
    packages = set((d.getVar('PACKAGES') or '').split())

    localdata = bb.data.createCopy(d)
    localdata.setVar('OVERRIDES', pkg)

    pkgdest = d.getVar("PKGDEST")
    pkgdir = os.path.join(pkgdest, pkg)

    for walkroot, dirs, files in os.walk(pkgdir):
        for f in files:
            name = os.path.join(walkroot, f)
            if os.path.exists(name) or not os.path.lexists(name):
                continue

            relpath = os.path.relpath(name, pkgdir)
            target = os.path.relpath(os.path.realpath(name), pkgdir)

            found = False
            for p in packages:
                if os.path.exists(os.path.join(pkgdest, p, target)):
                    found = True
                    if p not in (localdata.getVar('RDEPENDS') or ''):
                        oe.qa.handle_error("hexagon-dsp-binaries-symlink-deps", f"In package {pkg} broken symlink {relpath} -> {target} (present in package {p})", d)
            if not found:
                oe.qa.handle_error("hexagon-dsp-binaries-symlink-deps", f"In package {pkg} broken symlink {relpath} -> {target} ", d)
