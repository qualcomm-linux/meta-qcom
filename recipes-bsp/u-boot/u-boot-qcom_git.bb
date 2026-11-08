require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

DEPENDS += "bc-native dtc-native gnutls-native python3-pyelftools-native qtestsign-native xxd-native"

QCOM_UBOOT_SPL_FIT ?= "0"
QCOM_UBOOT_SPL_FIT_ATF ?= ""
QCOM_UBOOT_SPL_FIT_TEE ?= ""
QCOM_UBOOT_SPL_FIT_XBL_SEC ?= ""
QCOM_UBOOT_SPL_FIT_EXTRA_LOADABLES ?= ""

# "filename:size" entries for extra loadables that must fit the slot the SPL
# reserves for them. A larger file is trimmed back to the slot size when the
# excess bytes are only zero padding, and fails the build otherwise.
QCOM_UBOOT_SPL_FIT_LOADABLE_MAX_SIZES ?= ""

# The SPL cannot load an ELF whole, so each staged loadable listed here as
# "name:image:entry_rule:arch[:os]" is split into one image node per PT_LOAD
# segment, with the entry set on the first (first_only), first and last
# (first_and_last) or every (own_load) segment. The nodes go to
# fit-elf/<name>.dtsi and their names to fit-elf/<name>.loadables, which
# UBOOT_FIT_USER_SETTINGS and the configurations read in with $(cat ...) as
# uboot-sign writes the ITS.
QCOM_UBOOT_SPL_FIT_ELFS ?= ""

# Hash and signature subnodes for the image and configuration nodes a machine
# adds through UBOOT_FIT_USER_SETTINGS and UBOOT_FIT_USER_CONFIGURATIONS, the
# same uboot-sign.bbclass gives its own nodes, escaped for its printf "%b".
def qcom_spl_fit_sign(d, conf):
    if d.getVar('SPL_SIGN_ENABLE') != '1':
        return ''

    sign_alg = '%s,%s' % (d.getVar('UBOOT_FIT_HASH_ALG'), d.getVar('UBOOT_FIT_SIGN_ALG'))
    signature = ['signature {', '    algo = "%s";' % sign_alg,
                 '    key-name-hint = "%s";' % d.getVar('SPL_SIGN_KEYNAME')]
    lines = []
    if conf:
        if d.getVar('SPL_SIGN_CONF') == '1':
            lines += signature + ['    sign-images = "loadables";', '};']
    else:
        if d.getVar('SPL_SIGN_CONF') == '1':
            lines += ['hash-1 {', '    algo = "%s";' % d.getVar('UBOOT_FIT_HASH_ALG'), '};']
        if d.getVar('SPL_SIGN_INDIVIDUAL') == '1':
            lines += signature + ['};']
    return ''.join('            %s\\n' % line.replace('"', '\\"') for line in lines)

QCOM_UBOOT_SPL_FIT_IMAGE_SIGN = "${@qcom_spl_fit_sign(d, False)}"
QCOM_UBOOT_SPL_FIT_CONF_SIGN = "${@qcom_spl_fit_sign(d, True)}"

# BL31 and OP-TEE go into the FIT, swiv annotates the SPL before signing.
DEPENDS += "${@bb.utils.contains('QCOM_UBOOT_SPL_FIT', '1', '${QCOM_UBOOT_SPL_FIT_ATF} ${QCOM_UBOOT_SPL_FIT_TEE} swiv-build-utility-native', '', d)}"

# QCOM_UBOOT_SPL_FIT_EXTRA_LOADABLES (space-separated list of "recipe:filename"
# entries for PMIC/DCB configs, SHRM, QcLib, ... loaded from the FIT) and
# QCOM_UBOOT_SPL_FIT_XBL_SEC (the recipe providing qc_sec.mbn, linked into the
# SPL as the XBL-sec ELF segment) both name deploy-only recipes, so order
# do_compile after each one's do_deploy instead of pulling them in via DEPENDS.
do_compile[depends] += "${@' '.join(sorted(set( \
    (['%s:do_deploy' % entry.split(':', 1)[0] for entry in d.getVar('QCOM_UBOOT_SPL_FIT_EXTRA_LOADABLES').split()] if d.getVar('QCOM_UBOOT_SPL_FIT') == '1' else []) + \
    (['%s:do_deploy' % d.getVar('QCOM_UBOOT_SPL_FIT_XBL_SEC')] if d.getVar('QCOM_UBOOT_SPL_FIT_XBL_SEC') else []) \
)))}"

COMPATIBLE_MACHINE:aarch64 = "(qcom)"

PV = "2026.10+git"

# tag: qcom-next-v2026.10-20261106
SRCREV = "0766fef311064ef58a78282fd92c1eed74aa494b"
SRCBRANCH = "nobranch=1"

SRC_URI = "git://github.com/qualcomm-linux/u-boot.git;${SRCBRANCH};protocol=https;name=uboot"
SRC_URI += " \
    file://0001-Add-support-for-OpenSSL-Provider-API.patch \
    file://disable-eficapsule-tool.cfg \
    file://efi-rt-volatile-store.cfg \
    file://qcom-spl-fit-elf.py \
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'file://tfa-optee.cfg', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'kvm', 'file://gunyah-exit.cfg', '', d)} \
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

# xbl_sec.elf is linked from qc_sec.mbn at the top of the source tree, so it
# has to be in place before uboot_compile_config's first oe_runmake call.
do_compile:prepend() {
    if [ -n "${QCOM_UBOOT_SPL_FIT_XBL_SEC}" ]; then
        install -m 0644 ${DEPLOY_DIR_IMAGE}/${QCOM_BOOT_FILES_SUBDIR}/qc_sec.mbn ${S}/qc_sec.mbn
    fi
}

uboot_compile_config:append() {
    config_mbn_header=$(uboot_config_get_indexed_value "${BOARD_MBN_HEADER}" $i)

    if [ "${QCOM_UBOOT_SPL_FIT}" = "1" ]; then
        # Where uboot-sign's /incbin/ defaults expect them.
        install -m 0644 ${RECIPE_SYSROOT}/firmware/${QCOM_UBOOT_SPL_FIT_ATF}/bl31.bin ${B}/${builddir}/bl31.bin
        install -m 0644 ${RECIPE_SYSROOT}${nonarch_base_libdir}/firmware/tee-raw.bin ${B}/${builddir}/tee-raw.bin
        for fit_extra_loadable in ${QCOM_UBOOT_SPL_FIT_EXTRA_LOADABLES}; do
            fit_extra_loadable_file="${fit_extra_loadable#*:}"
            install -m 0644 ${DEPLOY_DIR_IMAGE}/${QCOM_BOOT_FILES_SUBDIR}/${fit_extra_loadable_file} ${B}/${builddir}/${fit_extra_loadable_file}
        done
        for fit_loadable_max_size in ${QCOM_UBOOT_SPL_FIT_LOADABLE_MAX_SIZES}; do
            fit_loadable_file=${B}/${builddir}/${fit_loadable_max_size%%:*}
            max_size=$(printf "%d" ${fit_loadable_max_size#*:})
            size=$(stat -c %s ${fit_loadable_file})
            [ ${size} -gt ${max_size} ] || continue
            if [ $(tail -c $(expr ${size} - ${max_size}) ${fit_loadable_file} | tr -d '\000' | wc -c) -ne 0 ]; then
                bbfatal "${fit_loadable_file} is ${size} bytes, larger than its ${max_size} byte slot"
            fi
            bbwarn "Trimming zero padding from ${fit_loadable_file} (${size} bytes) to its ${max_size} byte slot"
            truncate -s ${max_size} ${fit_loadable_file}
        done

        rm -rf ${B}/${builddir}/fit-elf
        if [ -n "${QCOM_UBOOT_SPL_FIT_ELFS}" ]; then
            (cd ${B}/${builddir} && nativepython3 ${UNPACKDIR}/qcom-spl-fit-elf.py \
                --outdir fit-elf --address-cells ${UBOOT_FIT_ADDRESS_CELLS} \
                --node-extra "$(printf "%b" "${QCOM_UBOOT_SPL_FIT_IMAGE_SIGN}")" \
                ${QCOM_UBOOT_SPL_FIT_ELFS})
        fi
    fi
}

# Rebuild the SPL ELF after uboot-sign, add the SWIV segment and sign it as TZ.
uboot_assemble_fitimage_helper:append() {
    if [ "${QCOM_UBOOT_SPL_FIT}" = "1" ]; then
        mbn_header=$(uboot_config_get_indexed_value "${BOARD_MBN_HEADER}" $i)
        [ -n "${mbn_header}" ] || mbn_header="v6"

        rm -f spl/u-boot-spl.elf
        (unset LDFLAGS CFLAGS; oe_runmake -C ${S} O=${B}/${builddir} ${UBOOT_MAKE_OPTS} spl/u-boot-spl.elf)

        export CRYPTOGRAPHY_OPENSSL_NO_LEGACY=1
        if [ -n "${QCOM_FW_SWIV_PLATFORM}" ]; then
            swiv_build_utility u-boot-spl-swiv.elf spl/u-boot-spl.elf ${QCOM_FW_SWIV_PLATFORM}
            qtestsign -${mbn_header} tz -o u-boot-spl.mbn u-boot-spl-swiv.elf
            rm -f u-boot-spl-swiv.elf
        else
            qtestsign -${mbn_header} ${QCOM_SPL_FW_TYPE} -o u-boot-spl.mbn spl/u-boot-spl.elf
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
