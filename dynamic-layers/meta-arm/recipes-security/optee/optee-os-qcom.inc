require recipes-security/optee/optee-os.inc

PV = "4.10.0-qcom-20260721+git"

SRC_TAG = "tag=qcom-next-4.10-20260721"
SRC_URI = "git://github.com/qualcomm-linux/optee_os.git;protocol=https;name=optee;nobranch=1;${SRC_TAG}"
SRCREV_optee = "690842e042dc470bf0625eb77a8cab926c372a8f"

# Copy of do_deploy from meta-arm until we get it fixed to support configurable deploy dir
OPTEE_DEPLOY ?= "optee"
do_deploy() {
    install -d ${DEPLOYDIR}/${MLPREFIX}${OPTEE_DEPLOY}
    install -m 644 ${D}${nonarch_base_libdir}/firmware/* ${DEPLOYDIR}/${MLPREFIX}${OPTEE_DEPLOY}

    install -d ${DEPLOYDIR}/${MLPREFIX}${OPTEE_DEPLOY}/ta
    install -m 644 ${B}/ta/*/*.elf ${DEPLOYDIR}/${MLPREFIX}${OPTEE_DEPLOY}/ta
}
