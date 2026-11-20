#
# Copyright (c) 2026 Qualcomm Innovation Center, Inc. All rights reserved.
#
# SPDX-License-Identifier: BSD-3-Clause-Clear
#
# Inject the OEM capsule root certificate into the boot config ELFs.
#
# A recipe of its own, not part of qcom-capsule.bbclass, because the order
# is forced: the certificate must be in the config ELF before that ELF is
# signed (editing a DTB inside it invalidates any signature it carried),
# and the capsule built afterwards is verified against that same
# certificate.  The injected ELF is therefore re-signed here when
# QCOM_FIRMWARE_SIGN_ENABLE is "1", before the bundle and the capsule pick
# it up.

# Shared with qcom-capsule.bbclass; ci/capsule-test-keys.yml sets it for CI.
CAPSULE_ROOT_CER ?= ""

BOOTBINS_DIR ?= "${DEPLOY_DIR_IMAGE}/${QCOM_BOOT_FILES_SUBDIR}"

OEM_CERT_STAGE = "${B}/firmware-to-sign"

inherit python3native deploy qcom-sign-common

do_configure[noexec] = "1"
do_install[noexec] = "1"

do_compile[depends] += "qdte-lite-native:do_populate_sysroot \
                        cbsp-boot-utilities-native:do_populate_sysroot"
do_compile[depends] += "${@'${QCOM_BOOT_FIRMWARE}:do_deploy' if d.getVar('QCOM_BOOT_FIRMWARE') else ''}"
do_compile[cleandirs] = "${OEM_CERT_STAGE}"
do_compile[file-checksums] += "${@qcom_firmware_sign_key_checksums(d)}"

python () {
    if not d.getVar('CAPSULE_ROOT_CER'):
        raise bb.parse.SkipRecipe(
            '%s: CAPSULE_ROOT_CER is not set. Point it at the DER-encoded OEM '
            'root certificate (see ci/capsule-test-keys.yml for a '
            'CI/development overlay).' % d.getVar('PN'))
}

# Inject the certificate into one boot config ELF.
#
# The DTB names are asked for rather than configured per machine: they are
# assigned during disassembly, from container metadata in one container and
# from each DTB's /compatible in another, so hardcoding them goes stale.
# More than one line is normal -- on hamoa the property appears in a base
# DTB and in a .dtbo overlay, at different node paths -- and the ops are
# joined with '&' so a single pass applies them all.
#
# bin-to-hex owns the DER-to-cells conversion so the padding of a trailing
# partial cell lives in one place rather than being reimplemented here.
#
# $1 - path to the config ELF or its .xz (rewritten in place)
patch_config_elf_cert() {
    local config_elf="$1"

    local stem
    stem=$(basename "${config_elf}")
    stem="${stem%.xz}"
    stem="${stem%.elf}"

    # qdte-lite exits non-zero both when the property is not there and when
    # the container cannot be read; only the latter says why on stderr.
    local targets err="${B}/qdte-find-${stem}.err"
    if ! targets=$(qdte-lite --nogui --input_file "${config_elf}" \
            --find_property QcCapsuleRootCert 2>"${err}"); then
        if [ -s "${err}" ]; then
            bbfatal "qdte-lite could not read ${stem}: $(cat "${err}")"
        fi
        bbnote "No DTB in ${stem} defines QcCapsuleRootCert; not deploying it."
        rm -f "${config_elf}"
        return 0
    fi

    local root_inc="${B}/QcFMPRoot.inc"
    qcom-capsule-tool bin-to-hex "${CAPSULE_ROOT_CER}" "${root_inc}"

    local modify_arg="" target
    for target in ${targets}; do
        if [ -z "${modify_arg}" ]; then
            modify_arg="${target}=@list:${root_inc}"
        else
            modify_arg="${modify_arg}&${target}=@list:${root_inc}"
        fi
    done

    local outdir="${B}/qdte_out/${stem}"
    rm -rf "${outdir}"
    mkdir -p "${outdir}"

    qdte-lite --nogui \
        --input_file  "${config_elf}" \
        --output_path "${outdir}" \
        --output_file "${stem}.elf" \
        --modify "${modify_arg}"

    # qdte-lite drops the image's hash and signature segments.
    if [ "${QCOM_FIRMWARE_SIGN_ENABLE}" = "1" ]; then
        qcom_sign_verify_file "${outdir}/${stem}.elf"
    fi

    # qdte-lite always writes a plain ELF; restore the input's compression.
    case "${config_elf}" in
    *.xz) xz -c "${outdir}/${stem}.elf" > "${config_elf}" ;;
    *)    install -m 0644 "${outdir}/${stem}.elf" "${config_elf}" ;;
    esac
}

do_compile() {
    if [ "${QCOM_FIRMWARE_SIGN_ENABLE}" = "1" ]; then
        qcom_firmware_sign_check_prereqs
    fi

    install -d "${OEM_CERT_STAGE}"

    # QCOM_XBL_CONFIG is xbl_config_kvm.elf on kvm machines.  Both ELFs are
    # optional: hamoa has no XBLConfig, UFS-boot platforms no uefi_dtbs.xz.
    if [ -f "${BOOTBINS_DIR}/${QCOM_XBL_CONFIG}" ]; then
        install -m 0644 "${BOOTBINS_DIR}/${QCOM_XBL_CONFIG}" "${OEM_CERT_STAGE}/"
        patch_config_elf_cert "${OEM_CERT_STAGE}/${QCOM_XBL_CONFIG}"
    fi

    # uefi_dtbs.xz can sit in a SPI-NOR subdirectory of the boot bins.
    UEFI_DTBS_XZ=$(find "${BOOTBINS_DIR}" -name "uefi_dtbs.xz" -print -quit)
    if [ -n "${UEFI_DTBS_XZ}" ]; then
        install -m 0644 "${UEFI_DTBS_XZ}" "${OEM_CERT_STAGE}/"
        patch_config_elf_cert "${OEM_CERT_STAGE}/uefi_dtbs.xz"
    fi

    if [ -z "$(ls -A ${OEM_CERT_STAGE} 2>/dev/null)" ]; then
        bbfatal "No boot config ELF carrying QcCapsuleRootCert was found under ${BOOTBINS_DIR}."
    fi
}

do_deploy() {
    install -d "${DEPLOYDIR}"

    # Fixed deploy name whatever the machine calls its XBLConfig, and
    # distinct from it so this does not collide in the deploy manifest with
    # the boot firmware recipe that owns the unmodified copy.
    if [ -f "${OEM_CERT_STAGE}/${QCOM_XBL_CONFIG}" ]; then
        install -m 0644 "${OEM_CERT_STAGE}/${QCOM_XBL_CONFIG}" \
            "${DEPLOYDIR}/xbl_config-with-oem-cert.elf"
    fi

    if [ -f "${OEM_CERT_STAGE}/uefi_dtbs.xz" ]; then
        install -m 0644 "${OEM_CERT_STAGE}/uefi_dtbs.xz" \
            "${DEPLOYDIR}/uefi_dtbs-with-oem-cert.xz"
    fi
}
addtask deploy before do_build after do_compile
