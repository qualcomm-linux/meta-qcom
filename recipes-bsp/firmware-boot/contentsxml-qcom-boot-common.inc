# Install contents.xml in DEPLOY_DIR

S = "${UNPACKDIR}"

QCOM_BOOT_IMG_SUBDIR ?= ""
CONTENTS_XML_FILE ?= ""

INHIBIT_DEFAULT_DEPS = "1"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

inherit deploy

do_deploy() {
    install -d ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}
    find "${S}" -maxdepth 1 -name "${CONTENTS_XML_FILE}" -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}/contents.xml \;
}
addtask deploy before do_build after do_install

inherit allarch
