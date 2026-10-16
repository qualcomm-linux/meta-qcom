require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

DEPENDS += "bc-native dtc-native gnutls-native python3-pyelftools-native qtestsign-native xxd-native"

QCOM_UBOOT_SPL_FIT ?= "0"
QCOM_UBOOT_SPL_FIT_ATF ?= ""
QCOM_UBOOT_SPL_FIT_TEE ?= ""
QCOM_UBOOT_SPL_FIT_FIRMWARE ?= ""
QCOM_UBOOT_SPL_FIT_XBL_SEC ?= ""

# The SPL cannot load an ELF whole, so boot firmware releases ship SHRM, QcLib,
# RPM and the like split per PT_LOAD segment, with a manifest.json giving each
# segment's file, load address, entry and architecture.
# QCOM_UBOOT_SPL_FIT_SEGMENTS names that directory within the
# QCOM_UBOOT_SPL_FIT_FIRMWARE deployment. It is copied into the build
# directory, and the manifest fills in the UBOOT_FIT_LOADABLE_FILENAME,
# _LOADADDRESS, _ENTRYPOINT, _ARCH and _OS varflags of every segment, e.g.
# UBOOT_FIT_LOADABLE_LOADADDRESS[qclib_1], that the machine has not set
# itself. The machine lists the segments it loads in UBOOT_FIT_LOADABLES.
QCOM_UBOOT_SPL_FIT_SEGMENTS ?= ""

# BL31 and OP-TEE go into the FIT, swiv annotates the SPL before signing.
DEPENDS += "${@bb.utils.contains('QCOM_UBOOT_SPL_FIT', '1', '${QCOM_UBOOT_SPL_FIT_ATF} ${QCOM_UBOOT_SPL_FIT_TEE} ${QCOM_UBOOT_SPL_FIT_FIRMWARE} swiv-build-utility-native', '', d)}"

# Blobs packed into the FIT besides BL31, OP-TEE and U-Boot proper (PMIC/DCB
# configs, SHRM, QcLib, ...) and the XBL-sec ELF linked into the SPL come from
# the boot firmware recipe named in QCOM_UBOOT_SPL_FIT_FIRMWARE. It only
# deploys them, so besides DEPENDS also order do_compile after its do_deploy.
# The FIT files staged are the UBOOT_FIT_LOADABLE_FILENAME varflags of the
# UBOOT_FIT_LOADABLES entries.
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

# QCOM_UBOOT_SPL_FIT_XBL_SEC names the XBL-sec ELF in the boot firmware
# deployment, as CONFIG_QCOM_XBL_SEC_ELF expects it at the top of the source
# tree. xbl_sec.elf is linked from it, so it has to be in place before
# uboot_compile_config's first oe_runmake call.
do_compile:prepend() {
    if [ -n "${QCOM_UBOOT_SPL_FIT_XBL_SEC}" ]; then
        install -m 0644 ${DEPLOY_DIR_IMAGE}/${QCOM_BOOT_FILES_SUBDIR}/${QCOM_UBOOT_SPL_FIT_XBL_SEC} ${S}/${QCOM_UBOOT_SPL_FIT_XBL_SEC}
    fi
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

        if [ -n "${QCOM_UBOOT_SPL_FIT_SEGMENTS}" ]; then
            rm -rf ${B}/${builddir}/${QCOM_UBOOT_SPL_FIT_SEGMENTS}
            cp -r ${DEPLOY_DIR_IMAGE}/${QCOM_BOOT_FILES_SUBDIR}/${QCOM_UBOOT_SPL_FIT_SEGMENTS} ${B}/${builddir}/${QCOM_UBOOT_SPL_FIT_SEGMENTS}
        fi
    fi
}

# The manifest is only there once the boot firmware recipe has deployed, so
# it is read as the ITS is written rather than at parse time.
python qcom_spl_fit_segments() {
    import json

    segments = d.getVar('QCOM_UBOOT_SPL_FIT_SEGMENTS')
    if d.getVar('QCOM_UBOOT_SPL_FIT') != '1' or not segments:
        return

    manifest = os.path.join(d.getVar('DEPLOY_DIR_IMAGE'), d.getVar('QCOM_BOOT_FILES_SUBDIR'),
                            segments, 'manifest.json')
    try:
        with open(manifest) as f:
            data = json.load(f)
    except (OSError, ValueError) as e:
        bb.fatal('%s: %s' % (manifest, e))
    if data.get('version') != 1:
        bb.fatal('%s: unsupported manifest version %s' % (manifest, data.get('version')))

    def set_flag(var, name, value):
        if value is not None and not d.getVarFlag(var, name):
            d.setVarFlag(var, name, value)

    for image in data['images']:
        for segment in image['segments']:
            name = segment['name']
            set_flag('UBOOT_FIT_LOADABLE_FILENAME', name, os.path.join(segments, segment['file']))
            set_flag('UBOOT_FIT_LOADABLE_LOADADDRESS', name, segment['load'])
            set_flag('UBOOT_FIT_LOADABLE_ENTRYPOINT', name, segment['entry'])
            set_flag('UBOOT_FIT_LOADABLE_ARCH', name, image['arch'])
            # os describes the image the SPL jumps to, so only the segments
            # with an entry carry it.
            if segment['entry'] is not None:
                set_flag('UBOOT_FIT_LOADABLE_OS', name, image['os'])
}
do_uboot_assemble_fitimage[prefuncs] += "qcom_spl_fit_segments"

# Rebuild the SPL ELF after uboot-sign, add the SWIV segment and sign it as TZ.
uboot_assemble_fitimage_helper:append() {
    if [ "${QCOM_UBOOT_SPL_FIT}" = "1" ]; then
        mbn_header=$(uboot_config_get_indexed_value "${BOARD_MBN_HEADER}" $i)
        [ -n "${mbn_header}" ] || mbn_header="v6"

        rm -f spl/u-boot-spl.elf
        (unset LDFLAGS CFLAGS; oe_runmake -C ${S} O=${B}/${builddir} ${UBOOT_MAKE_OPTS} spl/u-boot-spl.elf)

        export CRYPTOGRAPHY_OPENSSL_NO_LEGACY=1
        swiv_build_utility u-boot-spl-swiv.elf spl/u-boot-spl.elf ${QCOM_FW_SWIV_PLATFORM}
        qtestsign -${mbn_header} tz -o u-boot-spl.mbn u-boot-spl-swiv.elf
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
