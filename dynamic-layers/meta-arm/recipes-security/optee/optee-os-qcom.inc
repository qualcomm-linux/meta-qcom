require recipes-security/optee/optee-os.inc

PV = "4.10.0-qcom-20260930+git"

SRC_TAG = "tag=qcom-next-4.10-20260930"
SRC_URI = "git://github.com/qualcomm-linux/optee_os.git;protocol=https;name=optee;nobranch=1;${SRC_TAG}"
SRCREV_optee = "5fde2c5c6ca7992d01dac3f012f8b6304bce2376"

# Fetch sources from Shikra specific branch
SRC_TAG:shikra = "tag=early-hwe-shikra-4.10-20261015"
SRCREV_optee:shikra = "ff5f3dced71ba8d9ff9d99a00ba4e2e08654b309"

# Copy of do_deploy from meta-arm until we get it fixed to support configurable deploy dir
OPTEE_DEPLOY ?= "optee"
do_deploy() {
    install -d ${DEPLOYDIR}/${MLPREFIX}${OPTEE_DEPLOY}
    install -m 644 ${D}${nonarch_base_libdir}/firmware/* ${DEPLOYDIR}/${MLPREFIX}${OPTEE_DEPLOY}

    install -d ${DEPLOYDIR}/${MLPREFIX}${OPTEE_DEPLOY}/ta
    install -m 644 ${B}/ta/*/*.elf ${DEPLOYDIR}/${MLPREFIX}${OPTEE_DEPLOY}/ta
}
