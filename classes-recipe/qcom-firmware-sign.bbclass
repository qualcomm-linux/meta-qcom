#
# Copyright (c) 2026 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear
#

QCOM_FIRMWARE_SIGN_ENABLE ??= "0"

# Sectools binary -- defaults to the one staged by sectools-native.
QCOM_FIRMWARE_SIGN_SECTOOLS ?= "${STAGING_BINDIR_NATIVE}/sectools"

# Security Profile XML: a bare filename resolves against
# QCOM_FIRMWARE_SIGN_SECPROFILE_DIR, an absolute path is used as is.
QCOM_FIRMWARE_SIGN_SECPROFILE      ?= ""
QCOM_FIRMWARE_SIGN_SECPROFILE_DIR  ?= "${STAGING_DATADIR_NATIVE}/qcom-security-profiles"
QCOM_FIRMWARE_SIGN_SECPROFILE_PATH = "${@qcom_firmware_sign_secprofile_path(d)}"

# The profile is per SoC (machine conf); keep it out of the task hashes of
# allarch recipes unless signing actually happens.
QCOM_FIRMWARE_SIGN_SECPROFILE[vardepvalue] = "${@d.getVar('QCOM_FIRMWARE_SIGN_SECPROFILE') if d.getVar('QCOM_FIRMWARE_SIGN_ENABLE') == '1' else ''}"
QCOM_FIRMWARE_SIGN_SECPROFILE_PATH[vardepvalue] = "${@d.getVar('QCOM_FIRMWARE_SIGN_SECPROFILE_PATH') if d.getVar('QCOM_FIRMWARE_SIGN_ENABLE') == '1' else ''}"

def qcom_firmware_sign_secprofile_path(d):
    profile = d.getVar('QCOM_FIRMWARE_SIGN_SECPROFILE') or ''
    if not profile or profile.startswith('/'):
        return profile
    return os.path.join(d.getVar('QCOM_FIRMWARE_SIGN_SECPROFILE_DIR'), profile)

# Directory containing the OEM signing material, and the files expected in it:
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

# Directories whose images are signed in place.  Recipes point this at
# wherever their images live; the default suits a recipe that stages
# images for signing between do_compile and do_deploy.
QCOM_FIRMWARE_SIGN_DIRS ?= "${B}/firmware-to-sign"

# File suffixes to sign.
QCOM_FIRMWARE_SIGN_SUFFIXES ?= "mbn elf"

# PACKAGE_ARCH for recipes that are allarch when unsigned: the tune arch
# unless signing is on, which binds the images to the machine's keys and
# fuse ids and so packages them per machine.  Hash the value, not the
# variables it is picked from, so MACHINE does not reach the task hashes
# of an unsigned build.
QCOM_FIRMWARE_SIGN_PACKAGE_ARCH ?= "${@d.getVar('MACHINE_ARCH') if d.getVar('QCOM_FIRMWARE_SIGN_ENABLE') == '1' else d.getVar('TUNE_PKGARCH')}"
QCOM_FIRMWARE_SIGN_PACKAGE_ARCH[vardepvalue] = "${QCOM_FIRMWARE_SIGN_PACKAGE_ARCH}"

# Pull in the build-host signing helpers only when signing is enabled.
DEPENDS += "${@bb.utils.contains('QCOM_FIRMWARE_SIGN_ENABLE', '1', \
            'sectools-native security-profiles-native', '', d)}"

require conf/qcom-firmware-sign-image-ids.conf

# Verifies that everything the signing step needs exists before the
# task actually runs -- avoids cryptic sectools errors deep in the
# signing pipeline.
qcom_firmware_sign_check_prereqs() {
    if [ ! -x "${QCOM_FIRMWARE_SIGN_SECTOOLS}" ]; then
        bbfatal "QCOM_FIRMWARE_SIGN_SECTOOLS ('${QCOM_FIRMWARE_SIGN_SECTOOLS}') is missing or not executable."
    fi
    if [ -z "${QCOM_FIRMWARE_SIGN_SECPROFILE_PATH}" ]; then
        bbfatal "QCOM_FIRMWARE_SIGN_SECPROFILE is empty -- set it to a profile filename (e.g. kodiak_security_profile.xml) or to an absolute path."
    fi
    if [ ! -f "${QCOM_FIRMWARE_SIGN_SECPROFILE_PATH}" ]; then
        bbfatal "Security profile not found: ${QCOM_FIRMWARE_SIGN_SECPROFILE_PATH}"
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

# Print the sectools image id for the firmware filename $1, or nothing if
# it is not in QCOM_FIRMWARE_SIGN_IMAGE_ID_MAP.  The names in the map are
# shell globs and the first match wins.
qcom_sign_lookup_image_id() {
    for entry in ${QCOM_FIRMWARE_SIGN_IMAGE_ID_MAP}; do
        case "$1" in
            ${entry%%:*}) printf '%s\n' "${entry#*:}"; return 0 ;;
        esac
    done
    printf '\n'
}

# Sign a single file in place.  $1 is the absolute path; any non-zero
# sectools exit triggers a bbfatal in the caller.  The filename -> image-id
# map decides what is signable: the stock Qualcomm images already carry a
# signature made with Qualcomm's test keys, so the presence of one says
# nothing about whether a file needs re-signing.
qcom_sign_only_file() {
    file_path="$1"
    file_name="$(basename "${file_path}")"

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
            --security-profile "${QCOM_FIRMWARE_SIGN_SECPROFILE_PATH}" 2>/dev/null \
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
        --security-profile "${QCOM_FIRMWARE_SIGN_SECPROFILE_PATH}" \
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
    find "${dir}" -type f \( "$@" \) | while read -r f; do
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

do_qcom_firmware_sign[vardeps] += "\
    QCOM_FIRMWARE_SIGN_ENABLE \
    QCOM_FIRMWARE_SIGN_KEY_DIR \
    QCOM_FIRMWARE_SIGN_KEY_FILES \
    QCOM_FIRMWARE_SIGN_SECPROFILE \
    QCOM_FIRMWARE_SIGN_SECPROFILE_DIR \
    QCOM_FIRMWARE_SIGN_ANTI_ROLLBACK \
    QCOM_FIRMWARE_SIGN_DIRS \
    QCOM_FIRMWARE_SIGN_SUFFIXES \
    QCOM_FIRMWARE_SIGN_IMAGE_ID_MAP \
    QCOM_FUSE_OEM_HW_ID \
    QCOM_FUSE_OEM_PRODUCT_ID \
    QCOM_FUSE_SEC_KEY_DERIVATION_KEY \
"

# The task always exists and is noexec unless signing is on, which also
# keeps its prefuncs and postfuncs from running.  bitbake only accepts "1"
# for the noexec flag, so it is cleared from anonymous python rather than
# set from an expression.  Runs under pseudo so files rewritten in ${D}
# keep their ownership.  The key material is hashed by content, so keys
# rotated in place under the same path re-sign instead of reusing the
# previously signed images from sstate.
addtask qcom_firmware_sign after do_install before do_deploy do_package do_populate_sysroot
do_qcom_firmware_sign[noexec] = "1"
do_qcom_firmware_sign[fakeroot] = "1"
do_qcom_firmware_sign[depends] += "virtual/fakeroot-native:do_populate_sysroot"
do_qcom_firmware_sign[file-checksums] += "${@qcom_firmware_sign_key_checksums(d)}"

python () {
    if d.getVar('QCOM_FIRMWARE_SIGN_ENABLE') == '1':
        d.delVarFlag('do_qcom_firmware_sign', 'noexec')
}

def qcom_firmware_sign_key_checksums(d):
    keydir = d.getVar('QCOM_FIRMWARE_SIGN_KEY_DIR')
    if not keydir:
        return ''
    return ' '.join('%s/%s:True' % (keydir, f) for f in d.getVar('QCOM_FIRMWARE_SIGN_KEY_FILES').split())
