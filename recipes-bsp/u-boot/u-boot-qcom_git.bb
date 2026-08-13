require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

DEPENDS += "bc-native dtc-native gnutls-native python3-pyelftools-native qtestsign-native xxd-native"

# SPL FIT flow: pull in BL31 (TF-A) and OP-TEE so their binaries are staged into
# RECIPE_SYSROOT and can be assembled into the signed U-Boot FIT, plus the SWIV
# annotation tool required before the SPL is signed as the TZ image.
DEPENDS += "${@bb.utils.contains('QCOM_UBOOT_SPL_FIT', '1', 'trusted-firmware-a-qcom optee-os-qcom swiv-build-utility-native', '', d)}"

COMPATIBLE_MACHINE:aarch64 = "(qcom)"

PV = "2026.07+2026.10-rc1+git"

SRCREV = "294c0e13dfffdb9ac0fd2cb7df2825cb4ab5a379"
SRCBRANCH = "nobranch=1"

SRC_URI = "git://github.com/qualcomm-linux/u-boot.git;${SRCBRANCH};protocol=https;name=uboot"
SRC_URI += " \
    file://disable-eficapsule-tool.cfg \
    file://efi-rt-volatile-store.cfg \
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

uboot_compile_config:append() {
    config_mbn_header=$(uboot_config_get_indexed_value "${BOARD_MBN_HEADER}" $i)

    if [ "${QCOM_UBOOT_SPL_FIT}" = "1" ]; then
        # Stage BL31 (TF-A) and OP-TEE next to the .its so uboot-sign's /incbin/
        # defaults (bl31.bin, tee-raw.bin) resolve during do_uboot_assemble_fitimage.
        install -m 0644 ${RECIPE_SYSROOT}/firmware/trusted-firmware-a-qcom/bl31.bin ${B}/${builddir}/bl31.bin
        install -m 0644 ${RECIPE_SYSROOT}${nonarch_base_libdir}/firmware/tee-raw.bin ${B}/${builddir}/tee-raw.bin
        # U-Boot proper is carried inside the FIT, not flashed standalone; the
        # SPL is signed later in do_uboot_sign_spl (after the FIT public key is
        # embedded into the SPL DTB).
    elif [ -n "${config_mbn_header}" ]; then
        export CRYPTOGRAPHY_OPENSSL_NO_LEGACY=1
        qtestsign -${config_mbn_header} aboot -o ${B}/${builddir}/u-boot.mbn ${B}/${builddir}/u-boot.elf
    fi
}

# Sign the SPL for the Qualcomm secure-boot chain. Runs after the FIT has been
# assembled, so that when the FIT is signed the verification key has already
# been embedded into the SPL DTB (concat_spl_dtb rewrites ${SPL_BINARY} = SPL
# nodtb + signed DTB) and the flashed SPL carries it. The SPL binary is then
# wrapped into an ELF at the SPL load address, annotated with a SWIV segment
# (swiv_build_utility) and signed as the TZ image, matching the reference
# boot-firmware flow for the tz partition.
#
# Wrapping repeats what U-Boot does for CONFIG_SPL_REMAKE_ELF and reuses the
# linker script it generates for it. Its own spl/u-boot-spl.elf cannot be used
# directly: it is produced during the U-Boot build, before uboot-sign replaces
# the device tree appended to the SPL binary.
do_uboot_sign_spl() {
    [ "${QCOM_UBOOT_SPL_FIT}" = "1" ] || return 0

    export CRYPTOGRAPHY_OPENSSL_NO_LEGACY=1

    unset i
    for config in ${UBOOT_MACHINE}; do
        i=$(expr ${i:-0} + 1)
        j=0
        for type in ${UBOOT_CONFIG}; do
            j=$(expr $j + 1)
            [ $j -eq $i ] || continue

            builddir="${config}-${type}"
            mbn_header=$(uboot_config_get_indexed_value "${BOARD_MBN_HEADER}" $i)
            [ -n "${mbn_header}" ] || mbn_header="v6"

            cd ${B}/${builddir}
            ${OBJCOPY} -I binary -B aarch64 -O elf64-littleaarch64 ${SPL_BINARY} u-boot-spl.o
            ${LD} u-boot-spl.o -o u-boot-spl-unsigned.elf -EL \
                -T u-boot-elf.lds \
                --defsym=_start=${QCOM_UBOOT_SPL_ENTRY} -Ttext=${QCOM_UBOOT_SPL_ENTRY}
            rm -f u-boot-spl.o
            swiv_build_utility u-boot-spl-swiv.elf u-boot-spl-unsigned.elf \
                ${QCOM_UBOOT_SPL_SWIV_PLATFORM}
            qtestsign -${mbn_header} tz -o u-boot-spl.mbn u-boot-spl-swiv.elf
            rm -f u-boot-spl-unsigned.elf u-boot-spl-swiv.elf
        done
        unset j
    done
}
addtask uboot_sign_spl after do_uboot_assemble_fitimage before do_install do_deploy

uboot_deploy_config:append() {
    if [ "${QCOM_UBOOT_SPL_FIT}" = "1" ]; then
        if [ -f ${B}/${builddir}/u-boot-spl.mbn ]; then
            install -m 0644 ${B}/${builddir}/u-boot-spl.mbn ${DEPLOYDIR}/u-boot-spl-${type}.mbn
        fi
    elif [ -f ${B}/${builddir}/u-boot.mbn ]; then
        install -m 0644 ${B}/${builddir}/u-boot.mbn ${DEPLOYDIR}/u-boot-${type}.mbn
    fi
}
