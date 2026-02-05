require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

DEPENDS += "bc-native dtc-native gnutls-native python3-pyelftools-native qtestsign-native xxd-native"

COMPATIBLE_MACHINE:aarch64 = "(qcom)"

PV = "2026.01+2026.04-rc1+git"

SRCREV = "387b3e2f37a06337789531ffaaf3ac3e3c573ce2"

SRCBRANCH = "nobranch=1"
SRCBRANCH:class-devupstream = "branch=qcom-next"

SRC_URI = "git://github.com/qualcomm-linux/u-boot.git;${SRCBRANCH};protocol=https;name=uboot"

# To build tip of qcom-next branch set preferred
# virtual/bootloader provider to 'u-boot-qcom-upstream'
BBCLASSEXTEND = "devupstream:target"
PN:class-devupstream = "u-boot-qcom-upstream"
SRCREV:class-devupstream = "${AUTOREV}"

python __anonymous() {
    ubootconfig = (d.getVar('UBOOT_CONFIG') or "").split()

    if len(ubootconfig) > 0:
        for config in ubootconfig:
            # Get the MBN header version for this specific config
            mbn_header = d.getVarFlag('BOARD_MBN_HEADER', config)

            if not mbn_header:
                mbn_header = ""

            d.appendVar('BOARD_MBN_HEADER', mbn_header + " ? ")
}

uboot_compile_config:append:qcom() {
    config_mbn_header=$(uboot_config_get_indexed_value "${BOARD_MBN_HEADER}" $i)

    if [ -n "${config_mbn_header}" ]; then
        export CRYPTOGRAPHY_OPENSSL_NO_LEGACY=1
        qtestsign -${config_mbn_header} aboot -o ${B}/${builddir}/u-boot.mbn ${B}/${builddir}/u-boot.elf
    fi
}

uboot_deploy_config:append:qcom() {
    if [ -f ${B}/${builddir}/u-boot.mbn ]; then
        install -m 0644 ${B}/${builddir}/u-boot.mbn ${DEPLOYDIR}/u-boot.mbn
    fi
}
