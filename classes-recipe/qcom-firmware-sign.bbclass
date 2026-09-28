#
# Copyright (c) 2026 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear
#

QCOM_FIRMWARE_SIGN_ENABLE ??= "0"

# Sectools binary -- defaults to the one staged by sectools-native.
QCOM_FIRMWARE_SIGN_SECTOOLS ?= "${STAGING_BINDIR_NATIVE}/sectools"

QCOM_FIRMWARE_SIGN_SECPROFILE      ?= ""
QCOM_FIRMWARE_SIGN_SECPROFILE_DIR  ?= "${STAGING_DATADIR_NATIVE}/qcom-security-profiles"
# The profile is per SoC (machine conf); keep it out of the task hashes of
# allarch recipes unless signing actually happens.
QCOM_FIRMWARE_SIGN_SECPROFILE[vardepvalue] = "${@d.getVar('QCOM_FIRMWARE_SIGN_SECPROFILE') if d.getVar('QCOM_FIRMWARE_SIGN_ENABLE') == '1' else ''}"

# Directory containing the OEM signing material:
#   qpsa_rootca0.cer        -- root certificate
#   qpsa_attestca0.cer      -- attestation CA certificate
#   qpsa_attestca0.key      -- attestation CA private key
#   sha384_roots_hash.txt   -- root hash for verification
# Tests use ci/test-keys/ecdsa via ci/ecdsa-secure-boot-test-keys.yml.
QCOM_FIRMWARE_SIGN_KEY_DIR ?= ""
QCOM_FIRMWARE_SIGN_KEY_FILES ?= "\
    qpsa_rootca0.cer \
    qpsa_attestca0.cer \
    qpsa_attestca0.key \
    sha384_roots_hash.txt \
"

# Fuse identifiers, passed through to consumers / signing wrappers.
QCOM_FUSE_OEM_HW_ID                  ?= ""
QCOM_FUSE_OEM_PRODUCT_ID             ?= ""
QCOM_FUSE_SEC_KEY_DERIVATION_KEY     ?= ""

# Anti-rollback version embedded in the signed images.
QCOM_FIRMWARE_SIGN_ANTI_ROLLBACK     ?= "0x0"

# Directories whose images do_qcom_firmware_sign signs in place.  Recipes
# that stage images between do_compile and do_deploy for signing use the
# default; the firmware recipes point QCOM_FIRMWARE_SIGN_DIRS at their own.
QCOM_FIRMWARE_SIGN_DIR  ?= "${B}/firmware-to-sign"
QCOM_FIRMWARE_SIGN_DIRS ?= "${QCOM_FIRMWARE_SIGN_DIR}"

# File suffixes to sign, and an optional find(1) -maxdepth.
QCOM_FIRMWARE_SIGN_SUFFIXES ?= "mbn elf"
QCOM_FIRMWARE_SIGN_MAXDEPTH ?= ""

# Pull in the build-host signing helpers only when signing is enabled.
DEPENDS += "${@bb.utils.contains('QCOM_FIRMWARE_SIGN_ENABLE', '1', \
            'sectools-native security-profiles-native', '', d)}"

require conf/qcom-firmware-sign-image-ids.conf

# Internal helper: print the absolute path of the security profile XML
# (resolving QCOM_FIRMWARE_SIGN_SECPROFILE bare filenames against
# QCOM_FIRMWARE_SIGN_SECPROFILE_DIR), or fail with bbfatal.
qcom_sign_resolve_secprofile() {
    profile="${QCOM_FIRMWARE_SIGN_SECPROFILE}"
    if [ -z "${profile}" ]; then
        bbfatal "QCOM_FIRMWARE_SIGN_SECPROFILE is empty -- set it to a profile filename (e.g. kodiak_security_profile.xml) or to an absolute path."
    fi
    case "${profile}" in
        /*) printf '%s\n' "${profile}" ;;
        *)  printf '%s/%s\n' "${QCOM_FIRMWARE_SIGN_SECPROFILE_DIR}" "${profile}" ;;
    esac
}

# Verifies that everything the signing step needs exists before the
# task actually runs -- avoids cryptic sectools errors deep in the
# signing pipeline.
qcom_firmware_sign_check_prereqs() {
    if [ ! -x "${QCOM_FIRMWARE_SIGN_SECTOOLS}" ]; then
        bbfatal "QCOM_FIRMWARE_SIGN_SECTOOLS ('${QCOM_FIRMWARE_SIGN_SECTOOLS}') is missing or not executable."
    fi
    secprofile="$(qcom_sign_resolve_secprofile)"
    if [ ! -f "${secprofile}" ]; then
        bbfatal "Security profile not found: ${secprofile}"
    fi
    if [ ! -d "${QCOM_FIRMWARE_SIGN_KEY_DIR}" ]; then
        bbfatal "QCOM_FIRMWARE_SIGN_KEY_DIR ('${QCOM_FIRMWARE_SIGN_KEY_DIR}') is not a directory."
    fi
    for f in ${QCOM_FIRMWARE_SIGN_KEY_FILES}; do
        if [ ! -f "${QCOM_FIRMWARE_SIGN_KEY_DIR}/${f}" ]; then
            bbfatal "Required key file missing: ${QCOM_FIRMWARE_SIGN_KEY_DIR}/${f}"
        fi
    done
}

# Look up the sectools image-id for a given firmware filename in
# QCOM_FIRMWARE_SIGN_IMAGE_ID_MAP.  Prints the id (or "SKIP" / "")
# to stdout; never fails.
qcom_sign_lookup_image_id() {
    basename="$1"
    for entry in ${QCOM_FIRMWARE_SIGN_IMAGE_ID_MAP}; do
        case "${entry}" in
            "${basename}:"*) printf '%s\n' "${entry#*:}"; return 0 ;;
        esac
    done
    printf '\n'
}

# Sign a single file in place.  $1 is the absolute path; any non-zero
# sectools exit triggers a bbfatal in the caller.  The primary signal
# for "is this file signable" is the filename->image-id map -- the
# upstream Qualcomm reference binaries (qcm6490_bootbinaries.1.0-
# test-device-public et al.) arrive WITHOUT a pre-existing signature,
# so checking `--inspect` for a Software ID skips everything.
qcom_sign_only_file() {
    file_path="$1"
    file_name="$(basename "${file_path}")"
    secprofile="$(qcom_sign_resolve_secprofile)"

    image_id="$(qcom_sign_lookup_image_id "${file_name}")"
    if [ -z "${image_id}" ]; then
        bbwarn "${file_name}: not in QCOM_FIRMWARE_SIGN_IMAGE_ID_MAP, left unsigned"
        return 0
    fi
    case "${image_id}" in
        SKIP|SKIP:*)
            bbnote "${file_name}: skipping (mapped to ${image_id})"
            return 0
            ;;
    esac

    # Validate the image-id against the active security profile.  Each
    # profile only declares a subset of the global image-id namespace,
    # so skip-with-warn rather than fail for out-of-scope ids.
    #
    # sectools prints the list as a numbered enumeration, e.g.
    #     Available Image IDs:
    #     1. ABL
    #     2. ACPI
    #     ...
    #     44. XBL
    #     45. XBL-CONFIG
    if ! "${QCOM_FIRMWARE_SIGN_SECTOOLS}" secure-image --available-image-ids \
            --security-profile "${secprofile}" 2>/dev/null \
            | sed -nE 's/^[[:space:]]*[0-9]+\.[[:space:]]*([A-Za-z][A-Za-z0-9_-]*)[[:space:]]*$/\1/p' \
            | grep -Fqx "${image_id}"; then
        bbwarn "${file_name}: image-id '${image_id}' is not valid for the active security profile, skipping"
        return 0
    fi

    bbnote "sectools sign ${file_name} as ${image_id}"
    pre_sha="$(sha256sum "${file_path}" | cut -d' ' -f1)"
    "${QCOM_FIRMWARE_SIGN_SECTOOLS}" secure-image \
        --sign "${file_path}" \
        --image-id="${image_id}" \
        --security-profile "${secprofile}" \
        --anti-rollback-version="${QCOM_FIRMWARE_SIGN_ANTI_ROLLBACK}" \
        --signing-mode LOCAL \
        --root-certificate-index 0 \
        --root-certificate="${QCOM_FIRMWARE_SIGN_KEY_DIR}/qpsa_rootca0.cer" \
        --ca-certificate="${QCOM_FIRMWARE_SIGN_KEY_DIR}/qpsa_attestca0.cer" \
        --ca-key="${QCOM_FIRMWARE_SIGN_KEY_DIR}/qpsa_attestca0.key" \
        --outfile "${file_path}" \
        || bbfatal "Signing ${file_name} failed"

    # sectools' --sign returns 0 for non-fatal "I don't know how to sign
    # this" cases (e.g. plain ELF passed in for an image-id whose profile
    # entry expects MBN-V6) and produces a byte-identical output instead
    # of erroring out.  Detect that explicitly so silent no-ops surface as
    # warnings instead of being caught downstream by a confusing
    # verify-root failure.
    post_sha="$(sha256sum "${file_path}" | cut -d' ' -f1)"
    if [ "${pre_sha}" = "${post_sha}" ]; then
        bbwarn "${file_name}: sectools --sign left the file byte-identical when signing as '${image_id}' -- the input does not appear to be in the format that image-id expects.  Mark this entry as 'SKIP:<reason>' in QCOM_FIRMWARE_SIGN_IMAGE_ID_MAP if this is intentional."
    fi
}

# Sign + verify-root in one step.  Use this for files where the active
# security profile permits OEM-only signatures (i.e. does NOT have
# <oem_vouch_for_disallowed/> on the image entry).  For files where the
# profile requires hybrid OEM+QTI vouching (e.g. VIP under the upstream
# github security-profiles repo), call qcom_sign_only_file() instead --
# sectools' verify-root rejects OEM-only signatures against those
# profile entries even though the boot ROM accepts them.
qcom_sign_verify_file() {
    file_path="$1"
    file_name="$(basename "${file_path}")"

    pre_sha="$(sha256sum "${file_path}" | cut -d' ' -f1)"
    qcom_sign_only_file "$1" || return $?
    post_sha="$(sha256sum "${file_path}" | cut -d' ' -f1)"

    # If sign was a no-op (file not in map, mapped to SKIP, image-id
    # rejected by the profile, or sectools silently refused -- the warn
    # case in qcom_sign_only_file) there is nothing to verify.  Skipping
    # the verify-root is correct: running it against unchanged bytes
    # would fail with a confusing "infile is not signed by OEM" error.
    if [ "${pre_sha}" = "${post_sha}" ]; then
        bbdebug 1 "${file_name}: unchanged after sign attempt, skipping verify-root"
        return 0
    fi

    root_hash="0x$(cut -d' ' -f2 "${QCOM_FIRMWARE_SIGN_KEY_DIR}/sha384_roots_hash.txt")"
    "${QCOM_FIRMWARE_SIGN_SECTOOLS}" secure-image --verify-root "${root_hash}" "${file_path}" \
        || bbfatal "Root hash verification of ${file_name} failed"
}

# Sign every file with a QCOM_FIRMWARE_SIGN_SUFFIXES suffix under $1.
qcom_firmware_sign_dir() {
    dir="$1"
    set --
    for s in ${QCOM_FIRMWARE_SIGN_SUFFIXES}; do
        set -- "$@" -o -iname "*.${s}"
    done
    shift

    bbnote "qcom-firmware-sign: signing under ${dir}"
    find "${dir}" ${QCOM_FIRMWARE_SIGN_MAXDEPTH:+-maxdepth ${QCOM_FIRMWARE_SIGN_MAXDEPTH}} \
            -type f \( "$@" \) | while read -r f; do
        if ! qcom_sign_verify_file "${f}"; then
            bbfatal "Failed to sign: ${f}"
        fi
    done
}

do_qcom_firmware_sign() {
    qcom_firmware_sign_check_prereqs

    for dir in ${QCOM_FIRMWARE_SIGN_DIRS}; do
        if [ -d "${dir}" ]; then
            qcom_firmware_sign_dir "${dir}"
        fi
    done
}
do_qcom_firmware_sign[dirs] = "${QCOM_FIRMWARE_SIGN_DIR}"

do_qcom_firmware_sign[vardeps] += "\
    QCOM_FIRMWARE_SIGN_ENABLE \
    QCOM_FIRMWARE_SIGN_KEY_DIR \
    QCOM_FIRMWARE_SIGN_KEY_FILES \
    QCOM_FIRMWARE_SIGN_SECPROFILE \
    QCOM_FIRMWARE_SIGN_SECPROFILE_DIR \
    QCOM_FIRMWARE_SIGN_ANTI_ROLLBACK \
    QCOM_FIRMWARE_SIGN_DIR \
    QCOM_FIRMWARE_SIGN_DIRS \
    QCOM_FIRMWARE_SIGN_SUFFIXES \
    QCOM_FIRMWARE_SIGN_MAXDEPTH \
    QCOM_FIRMWARE_SIGN_IMAGE_ID_MAP \
    QCOM_FUSE_OEM_HW_ID \
    QCOM_FUSE_OEM_PRODUCT_ID \
    QCOM_FUSE_SEC_KEY_DERIVATION_KEY \
"

# The task always exists and is a no-op unless signing is on: bitbake only
# accepts "1" for the noexec flag, so it is cleared from anonymous python.
# With signing on, pseudo keeps the ownership of the files rewritten in
# ${D}, and the key material is hashed by content so keys rotated in place
# do not leave previously signed images valid in sstate.
addtask qcom_firmware_sign after do_install before do_deploy do_package do_populate_sysroot
do_qcom_firmware_sign[noexec] = "1"
do_qcom_firmware_sign[fakeroot] = "1"
do_qcom_firmware_sign[depends] += "${@'virtual/fakeroot-native:do_populate_sysroot' if d.getVar('QCOM_FIRMWARE_SIGN_ENABLE') == '1' else ''}"
do_qcom_firmware_sign[file-checksums] += "${@' '.join('%s/%s:True' % (d.getVar('QCOM_FIRMWARE_SIGN_KEY_DIR'), f) for f in d.getVar('QCOM_FIRMWARE_SIGN_KEY_FILES').split()) if d.getVar('QCOM_FIRMWARE_SIGN_KEY_DIR') else ''}"

python () {
    if d.getVar('QCOM_FIRMWARE_SIGN_ENABLE') == '1':
        d.delVarFlag('do_qcom_firmware_sign', 'noexec')
}
