require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

DEPENDS += "bc-native dtc-native gnutls-native python3-pyelftools-native qtestsign-native xxd-native"

QCOM_UBOOT_SPL_FIT ?= "0"
QCOM_UBOOT_SPL_FIT_ATF ?= ""
QCOM_UBOOT_SPL_FIT_TEE ?= ""
QCOM_UBOOT_SPL_FIT_FIRMWARE ?= ""

# "1": rebuild spl/u-boot-spl.elf after uboot-sign, add the SWIV segment and sign it with qtestsign.
# "0": use spl/u-boot-spl.mbn from U-Boot, for defconfigs that set CONFIG_QCOM_GENERATE_MBN=y.
QCOM_UBOOT_SPL_MBN_SIGN ?= "1"

# BL31 and OP-TEE go into the FIT, swiv annotates the SPL before signing.
DEPENDS += "${@bb.utils.contains('QCOM_UBOOT_SPL_FIT', '1', '${QCOM_UBOOT_SPL_FIT_ATF} ${QCOM_UBOOT_SPL_FIT_TEE}', '', d)}"
DEPENDS += "${@'swiv-build-utility-native' if d.getVar('QCOM_UBOOT_SPL_FIT') == '1' and d.getVar('QCOM_UBOOT_SPL_MBN_SIGN') == '1' else ''}"

# Blobs packed into the FIT besides BL31, OP-TEE and U-Boot proper (PMIC/DCB
# configs, SHRM, QcLib, ...) come from the boot firmware recipe named in
# QCOM_UBOOT_SPL_FIT_FIRMWARE. It only deploys them, so besides DEPENDS also
# order do_compile after its do_deploy. The files staged are the
# UBOOT_FIT_LOADABLE_FILENAME varflags of the UBOOT_FIT_LOADABLES entries.
DEPENDS += "${@bb.utils.contains('QCOM_UBOOT_SPL_FIT', '1', '${QCOM_UBOOT_SPL_FIT_FIRMWARE}', '', d)}"
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

# Produce u-boot-spl.mbn, after uboot-sign has assembled the FIT.
uboot_assemble_fitimage_helper:append() {
    if [ "${QCOM_UBOOT_SPL_FIT}" = "1" ]; then
        rm -f u-boot-spl.mbn

        if [ "${QCOM_UBOOT_SPL_MBN_SIGN}" = "1" ]; then
            # Rebuild the SPL ELF, add the SWIV segment and sign it as TZ.
            mbn_header=$(uboot_config_get_indexed_value "${BOARD_MBN_HEADER}" $i)
            [ -n "${mbn_header}" ] || mbn_header="v6"

            rm -f spl/u-boot-spl.elf
            (unset LDFLAGS CFLAGS; oe_runmake -C ${S} O=${B}/${builddir} ${UBOOT_MAKE_OPTS} spl/u-boot-spl.elf)

            export CRYPTOGRAPHY_OPENSSL_NO_LEGACY=1
            swiv_build_utility u-boot-spl-swiv.elf spl/u-boot-spl.elf ${QCOM_FW_SWIV_PLATFORM}
            qtestsign -${mbn_header} tz -o u-boot-spl.mbn u-boot-spl-swiv.elf
            rm -f u-boot-spl-swiv.elf
        else
            [ -f spl/u-boot-spl.mbn ] || bbfatal "U-Boot did not build spl/u-boot-spl.mbn in ${B}/${builddir}"
            install -m 0644 spl/u-boot-spl.mbn u-boot-spl.mbn
        fi
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
