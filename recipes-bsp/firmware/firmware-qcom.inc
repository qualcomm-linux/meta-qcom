# Optional secure-boot signing (QCOM_FIRMWARE_SIGN_ENABLE).  Unsigned
# firmware is the same for every machine; signed firmware is bound to its
# Security Profile and keys.
inherit qcom-firmware-sign
inherit_defer ${@'allarch' if d.getVar('QCOM_FIRMWARE_SIGN_ENABLE') != '1' else ''}

FILES:${PN} += "${nonarch_base_libdir}/firmware/"

# Firmware files are pre-compiled, pre-stripped, and not target architecture executables.
# Skipping QA checks: 'already-stripped', 'arch' because:
# - Firmware is not AArch64 ELF (arch check fails)
# - file is Pre-stripped  (already-stripped)
INSANE_SKIP:${PN} += "arch already-stripped"

# Default settings
# package name part in linux-firmware-qcom-...-audio
FW_QCOM_NAME ?= "unset"
# Subdir inside /lib/firmware/qcom. Typically it is equal to FW_QCOM_NAME but might differ in complex cases (like C630 Yoga).
FW_QCOM_SUBDIR ?= "${FW_QCOM_NAME}"
FW_QCOM_BASE_PATH = "${nonarch_base_libdir}/firmware/qcom"
FW_QCOM_PATH = "${FW_QCOM_BASE_PATH}/${FW_QCOM_SUBDIR}"

SPLIT_FIRMWARE_PACKAGES ?= ""

PACKAGE_BEFORE_PN += "${SPLIT_FIRMWARE_PACKAGES}"
# RRECOMMEND all packages split from this recipe.
RRECOMMENDS:${PN} += "${SPLIT_FIRMWARE_PACKAGES}"

# Default settings for several split packages
FILES:linux-firmware-qcom-${FW_QCOM_NAME}-adreno = "${FW_QCOM_PATH}/*_zap.mbn*"
FILES:linux-firmware-qcom-${FW_QCOM_NAME}-audio = "${FW_QCOM_PATH}/adsp.mbn* ${FW_QCOM_PATH}/adsp_dtb.mbn* ${FW_QCOM_PATH}/adsp*.jsn ${FW_QCOM_PATH}/battmgr.jsn"
FILES:linux-firmware-qcom-${FW_QCOM_NAME}-compute = "${FW_QCOM_PATH}/cdsp*.mbn* ${FW_QCOM_PATH}/cdsp_dtb.mbn* ${FW_QCOM_PATH}/cdsp*.jsn"
FILES:linux-firmware-qcom-${FW_QCOM_NAME}-dsps = "${FW_QCOM_PATH}/dsps.mbn*"
FILES:linux-firmware-qcom-${FW_QCOM_NAME}-generalpurpose = "${FW_QCOM_PATH}/gpdsp*.mbn*"
FILES:linux-firmware-qcom-${FW_QCOM_NAME}-gss = "${FW_QCOM_PATH}/gss.mbn*"
FILES:linux-firmware-qcom-${FW_QCOM_NAME}-ipa = "${FW_QCOM_PATH}/ipa_fws.mbn* ${FW_QCOM_PATH}/ipa_fws.elf*"
FILES:linux-firmware-qcom-${FW_QCOM_NAME}-modem = "${FW_QCOM_PATH}/mba.mbn* ${FW_QCOM_PATH}/modem.mbn* \
                                                   ${FW_QCOM_PATH}/modem*.jsn ${FW_QCOM_PATH}/modem_pr/so/*.mbn"
FILES:linux-firmware-qcom-${FW_QCOM_NAME}-q6 = "${FW_QCOM_PATH}/q6.mbn*"
FILES:linux-firmware-qcom-${FW_QCOM_NAME}-sensors = "${FW_QCOM_PATH}/slpi.mbn* ${FW_QCOM_PATH}/slpi*.jsn"
FILES:linux-firmware-qcom-${FW_QCOM_NAME}-venus = "${FW_QCOM_PATH}/venus.mbn* ${FW_QCOM_PATH}/vidc* ${FW_QCOM_BASE_PATH}/vpu*"
FILES:linux-firmware-qcom-${FW_QCOM_NAME}-wifi = "${FW_QCOM_PATH}/wcnss.mbn* ${FW_QCOM_PATH}/wlanmdsp.mbn*"

# Possible values are "xz" and "zst".
FIRMWARE_COMPRESSION ?= ""

python() {
    pn = d.getVar("PN")
    insanes = d.getVar("INSANE_SKIP:%s" % pn)
    for pkg in d.getVar("SPLIT_FIRMWARE_PACKAGES").split():
        # append the INSANE_SKIP of the main package to pass QA
        d.appendVar("INSANE_SKIP:" + pkg, " " + insanes)
    if d.getVar("FW_QCOM_NAME") == "unset" and d.getVar("SPLIT_FIRMWARE_PACKAGES") != "":
        bb.error("%s: split firmware-qcom packages engaged, but FW_QCOM_NAME is not defined" % pn)
}

firmware_qcom_compress() {
    for fw in $(find ${D}${FW_QCOM_BASE_PATH} -type f ! -name '*.jsn')
    do
        case "${FIRMWARE_COMPRESSION}" in
            zst | zstd)
                zstd --compress --rm ${fw}
                ;;
            xz)
                xz --compress --check=crc32 ${fw}
                ;;
        esac
    done
}

do_install:append() {
    firmware_qcom_compress
}

# Undo firmware_qcom_compress, whatever FIRMWARE_COMPRESSION was when the
# blobs were compressed.
firmware_qcom_decompress() {
    find ${D}${FW_QCOM_BASE_PATH} -type f -name '*.zst' -exec zstd --decompress --rm {} \;
    find ${D}${FW_QCOM_BASE_PATH} -type f -name '*.xz' -exec xz --decompress {} \;
}

# Signing needs the raw blobs: compressed, they no longer match the
# image-id map.  Undo the do_install compression, sign, compress again.
do_qcom_firmware_sign() {
    if ! qcom_check_signing_enabled ; then
        return 0
    fi

    # Placeholder recipes install no firmware.
    [ -d "${D}${FW_QCOM_BASE_PATH}" ] || return 0

    firmware_qcom_decompress

    bbnote "qcom-firmware-sign: walking ${D}${FW_QCOM_BASE_PATH} for MBN/ELF blobs"
    find "${D}${FW_QCOM_BASE_PATH}" -type f \
        \( -iname '*.mbn' -o -iname '*.elf' \) \
        | while read -r f; do
            qcom_sign_verify_file "${f}" || bbfatal "Failed to sign: ${f}"
        done

    firmware_qcom_compress
}
do_qcom_firmware_sign[vardeps] += "FIRMWARE_COMPRESSION"

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_DEFAULT_DEPS = "1"

# These pkgs are empty and when present will be pulled into SDK
# by default and cause license conflits, so don't generate them.
PACKAGES:remove = "${PN}-dbg"
PACKAGES:remove = "${PN}-dev"
PACKAGES:remove = "${PN}-staticdev"
