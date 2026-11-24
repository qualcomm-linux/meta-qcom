require recipes-security/optee/optee-os.inc

DEPENDS += "dtc-native"

PV = "4.11.0-qcom-20261119+git"

SRC_TAG = "tag=qcom-next-4.11-20261119"
SRC_URI = "git://github.com/qualcomm-linux/optee_os.git;protocol=https;name=optee;nobranch=1;${SRC_TAG}"
SRCREV_optee = "e7510c9199ebbb8d2949204f57c0f8103f688129"

# Enable PKCS#11 TA as part of the OP-TEE build
EXTRA_OEMAKE:append = " CFG_PKCS11_TA=y"

# Copy of do_deploy from meta-arm until we get it fixed to support configurable deploy dir
OPTEE_DEPLOY ?= "optee"
do_deploy() {
    install -d ${DEPLOYDIR}/${MLPREFIX}${OPTEE_DEPLOY}
    install -m 644 ${D}${nonarch_base_libdir}/firmware/* ${DEPLOYDIR}/${MLPREFIX}${OPTEE_DEPLOY}

    install -d ${DEPLOYDIR}/${MLPREFIX}${OPTEE_DEPLOY}/ta
    install -m 644 ${B}/ta/*/*.elf ${DEPLOYDIR}/${MLPREFIX}${OPTEE_DEPLOY}/ta
}
