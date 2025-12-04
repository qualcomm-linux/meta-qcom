SUMMARY = "Build qcom-metadata.dtb from vendored qcom-dtb-metadata"
HOMEPAGE = "https://github.com/qualcomm-linux/qcom-dtb-metadata"

LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2998c54c288b081076c9af987bdf4838"

DEPENDS = "dtc-native"

SRC_URI = "git://github.com/qualcomm-linux/qcom-dtb-metadata.git;branch=main;protocol=https"

SRCREV = "6b9e4b9c093cba36a80035af103015f0a7d3f9fc"

inherit deploy

do_configure[noexec] = "1"

do_deploy() {
    install -d ${DEPLOY_DIR_IMAGE}
    install -m 0644 ${B}/qcom-metadata.dtb ${DEPLOY_DIR_IMAGE}/qcom-metadata.dtb
}
addtask deploy after do_compile before do_build
