require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

# The SPL FIT needs uboot-sign options that oe-core doesn't have yet.
inherit ${@'qcom-uboot-sign' if d.getVar('QCOM_UBOOT_SPL_FIT') == '1' else ''}

DEPENDS += "bc-native dtc-native gnutls-native python3-pyelftools-native qtestsign-native xxd-native"

QCOM_UBOOT_SPL_FIT ?= "0"
QCOM_UBOOT_SPL_FIT_ATF ?= ""
QCOM_UBOOT_SPL_FIT_TEE ?= ""
QCOM_UBOOT_SPL_FIT_FIRMWARE ?= ""
QCOM_SPL_FW_TYPE ?= "tz"

# The machines using the SPL FIT name the recipes providing the FIT images.
# The names stay empty for every other machine, so they add nothing there.
DEPENDS += "${QCOM_UBOOT_SPL_FIT_ATF} ${QCOM_UBOOT_SPL_FIT_TEE} ${QCOM_UBOOT_SPL_FIT_FIRMWARE}"
# swiv annotates the SPL before signing, on the platforms that set QCOM_FW_SWIV_PLATFORM.
DEPENDS += "${@'swiv-build-utility-native' if d.getVar('QCOM_UBOOT_SPL_FIT') == '1' and d.getVar('QCOM_FW_SWIV_PLATFORM') else ''}"

# The boot firmware recipe only deploys, so wait for its do_deploy. The FIT
# blobs staged from it are the files of the UBOOT_FIT_LOADABLES entries.
do_compile[depends] += "${@'%s:do_deploy' % d.getVar('QCOM_UBOOT_SPL_FIT_FIRMWARE') if d.getVar('QCOM_UBOOT_SPL_FIT_FIRMWARE') else ''}"
QCOM_UBOOT_SPL_FIT_FILES = "${@' '.join(filter(None, (d.getVarFlag('UBOOT_FIT_LOADABLE_FILENAME', image) for image in (d.getVar('UBOOT_FIT_LOADABLES') or '').split())))}"

COMPATIBLE_MACHINE:aarch64 = "(qcom)"

PV = "2026.10+git"

SRC_TAG = "tag=qcom-next-v2026.10-20261115"
SRCREV_uboot = "331595c5af3baf383c482b24538ce2a717be5e49"

SRC_URI = "git://github.com/qualcomm-linux/u-boot.git;protocol=https;name=uboot;nobranch=1;${SRC_TAG}"
SRC_URI += " \
    file://0001-Add-support-for-OpenSSL-Provider-API.patch \
    file://disable-eficapsule-tool.cfg \
    file://efi-rt-volatile-store.cfg \
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'file://tfa-optee.cfg', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'kvm', 'file://gunyah-exit.cfg', 'file://no-gunyah-exit.cfg', d)} \
    ${@bb.utils.contains('SPL_SIGN_ENABLE', '1', 'file://spl-fit-signature.cfg', '', d)} \
"

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

uboot_compile_config:append() {
    if [ "${QCOM_UBOOT_SPL_FIT}" = "1" ]; then
        # Where uboot-sign's /incbin/ defaults expect them.
        install -m 0644 ${RECIPE_SYSROOT}/firmware/${QCOM_UBOOT_SPL_FIT_ATF}/bl31.bin ${B}/${builddir}/bl31.bin
        install -m 0644 ${RECIPE_SYSROOT}${nonarch_base_libdir}/firmware/tee-raw.bin ${B}/${builddir}/tee-raw.bin
        if [ -n "${QCOM_UBOOT_SPL_FIT_FIRMWARE}" ]; then
            for fit_file in ${QCOM_UBOOT_SPL_FIT_FILES}; do
                install -D -m 0644 ${DEPLOY_DIR_IMAGE}/${QCOM_BOOT_FILES_SUBDIR}/${fit_file} ${B}/${builddir}/${fit_file}
            done
        fi
    fi
}

# Rebuild the SPL ELF after uboot-sign and sign it as QCOM_SPL_FW_TYPE, adding
# the SWIV segment first on the platforms that set QCOM_FW_SWIV_PLATFORM.
uboot_assemble_fitimage_helper:append() {
    if [ "${QCOM_UBOOT_SPL_FIT}" = "1" ]; then
        mbn_header=$(uboot_config_get_indexed_value "${BOARD_MBN_HEADER}" $i)
        [ -n "${mbn_header}" ] || mbn_header="v6"

        rm -f spl/u-boot-spl.elf
        (unset LDFLAGS CFLAGS; oe_runmake -C ${S} O=${B}/${builddir} ${UBOOT_MAKE_OPTS} spl/u-boot-spl.elf)

        export CRYPTOGRAPHY_OPENSSL_NO_LEGACY=1
        spl_elf=spl/u-boot-spl.elf
        if [ -n "${QCOM_FW_SWIV_PLATFORM}" ]; then
            swiv_build_utility u-boot-spl-swiv.elf spl/u-boot-spl.elf ${QCOM_FW_SWIV_PLATFORM}
            spl_elf=u-boot-spl-swiv.elf
        fi
        qtestsign -${mbn_header} ${QCOM_SPL_FW_TYPE} -o u-boot-spl.mbn ${spl_elf}
        rm -f u-boot-spl-swiv.elf
    fi
}

uboot_deploy_config:append() {
    if [ "${QCOM_UBOOT_SPL_FIT}" = "1" ]; then
        if [ -f ${B}/${builddir}/u-boot-spl.mbn ]; then
            install -m 0644 ${B}/${builddir}/u-boot-spl.mbn ${DEPLOYDIR}/u-boot-spl-${type}.mbn
        fi
    elif [ -f ${B}/${builddir}/u-boot.mbn ]; then
        install -m 0644 ${B}/${builddir}/u-boot.mbn ${DEPLOYDIR}/u-boot-${type}.mbn
    fi
}
