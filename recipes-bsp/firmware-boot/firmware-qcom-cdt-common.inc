# Install CDT Firmware in DEPLOY_DIR
CDT_ARTIFACTORY = "artifacts.codelinaro.org/artifactory/codelinaro-le/Qualcomm_Linux"

S = "${UNPACKDIR}"

QCOM_CDT_SUBDIR ?= ""

INHIBIT_DEFAULT_DEPS = "1"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

inherit deploy

do_deploy() {
    install -d ${DEPLOYDIR}/${QCOM_CDT_SUBDIR}
    find "${UNPACKDIR}" -iname '*cdt*.bin' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_CDT_SUBDIR} \;
}
addtask deploy before do_build after do_install

inherit allarch
