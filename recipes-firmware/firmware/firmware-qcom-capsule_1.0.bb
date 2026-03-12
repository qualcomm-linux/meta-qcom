# Yocto recipe for UEFI capsule generation on Qualcomm platforms.
#
# The recipe fetches all Python capsule-generation scripts from the official
# cbsp-boot-utilities GitHub repository.  The EDK2 host tools (GenFfs, GenFv,
# GenerateCapsule.py, Common/) are provided by edk2-basetools-native.
# Boot firmware binaries are consumed from the firmware-qcom-bootbins deploy
# directory (virtual/bootbins provider).
#
# ---------------------------------------------------------------------------
# Integrator checklist
# ---------------------------------------------------------------------------
# 1.  Pin SRCREV_cbsp to the commit you validated.
# 2.  Supply OEM PKI material:
#       CAPSULE_ROOT_CER   - DER-encoded root CA cert (QcFMPRoot.cer)
#       CAPSULE_CERT_PEM   - PEM signing cert + key  (QcFMPCert.pem)
#       CAPSULE_ROOT_PUB   - Root CA public key PEM  (QcFMPRoot.pub.pem)
#       CAPSULE_SUB_PUB    - Sub  CA public key PEM  (QcFMPSub.pub.pem)
# 3.  Append a board-specific FvUpdate.xml via SRC_URI:append if the
#     default one (from cbsp-boot-utilities) does not match your layout.
# 4.  Set CAPSULE_GUID to your board's FMP ESRT GUID.
# 5.  Adjust CAPSULE_FW_VERSION / CAPSULE_FW_LSV for each OTA release.
# ---------------------------------------------------------------------------

DESCRIPTION = "UEFI Capsule generation for Qualcomm platforms"
HOMEPAGE    = "https://github.com/quic/cbsp-boot-utilities"
LICENSE     = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://uefi_capsule_generation/LICENSE;md5=8e1eb38e3de3966193d29f31f5d7e684"

COMPATIBLE_MACHINE = "qcm6490|qcs9100|qcs8300|qcs615"

# cbsp-boot-utilities - all Python capsule scripts
SRCREV_cbsp = "58658d656953aeaa01f40b7ea75a65e7125d8488"

SRC_URI = " \
    git://github.com/quic/cbsp-boot-utilities.git;protocol=https;branch=main;name=cbsp \
"


# ---------------------------------------------------------------------------
# Custom FvUpdate.xml (optional)
# ---------------------------------------------------------------------------
# By default the FvUpdate.xml bundled in cbsp-boot-utilities is used.
# To provide a board/project-specific capsule layout, append your file to
# SRC_URI and name it FvUpdate.xml, e.g. in a .bbappend or local.conf:
#   SRC_URI:append = " file://my-board-FvUpdate.xml;subdir=fvupdate"
# The recipe detects a custom FvUpdate.xml placed in ${WORKDIR} and uses
# it in place of the upstream default.

# ---------------------------------------------------------------------------
# Capsule parameters (override in machine / distro / local.conf)
# ---------------------------------------------------------------------------

# Firmware version embedded in the capsule header
CAPSULE_FW_VERSION ?= "0.0.1.2"
# Lowest supported version (lowest firmware version this capsule accepts)
CAPSULE_FW_LSV     ?= "0.0.0.0"
# Firmware volume type label passed to FVCreation.py / UpdateJsonParameters.py
CAPSULE_FV_TYPE    ?= "SYS_FW"
# FMP ESRT GUID that identifies this firmware on the target
CAPSULE_GUID       ?= "6F25BFD2-A165-468B-980F-AC51A0A45C52"

# ---------------------------------------------------------------------------
# OEM PKI material (must be supplied by the integrator - test key as  defaults)
# ---------------------------------------------------------------------------

# Test PKI material - for development/CI use only.
# Replace with production keys from a secure location before shipping.
SRC_URI:append = " \
    file://test-keys/QcFMPRoot.cer \
    file://test-keys/QcFMPCert.pem \
    file://test-keys/QcFMPRoot.pub.pem \
    file://test-keys/QcFMPSub.pub.pem \
"

# Paths to the PKI artefacts produced by the OpenSSL cert-generation step
# in ci.yml.  In a product build these come from a secure location outside
# the source tree (e.g., a secrets manager, a separate signing recipe, or
# a confidential layer).
#
# CAPSULE_ROOT_CER - DER-encoded root CA certificate (QcFMPRoot.cer)
#                    Converted to hex INC format by BinToHex.py before use.
CAPSULE_ROOT_CER ?= "${UNPACKDIR}/test-keys/QcFMPRoot.cer"
#
# CAPSULE_CERT_PEM - Combined signing key + leaf certificate in PEM format
#                    (QcFMPCert.pem, output of `openssl pkcs12 ... -nodes`)
CAPSULE_CERT_PEM ?= "${UNPACKDIR}/test-keys/QcFMPCert.pem"
#
# CAPSULE_ROOT_PUB - Root CA public key in PEM format (QcFMPRoot.pub.pem)
CAPSULE_ROOT_PUB ?= "${UNPACKDIR}/test-keys/QcFMPRoot.pub.pem"
#
# CAPSULE_SUB_PUB  - Intermediate CA public key in PEM format (QcFMPSub.pub.pem)
CAPSULE_SUB_PUB  ?= "${UNPACKDIR}/test-keys/QcFMPSub.pub.pem"

# ---------------------------------------------------------------------------
# XBLConfig DTB certificate injection
# ---------------------------------------------------------------------------
# Set XBLCONFIG_DTB to the post-DDR DTB filename that lives inside the boot
# binaries directory.  When set, the recipe will:
#   1. dump XBLConfig sections
#   2. patch QcCapsuleRootCert in the DTB with the converted root cert
#   3. re-pack the updated DTB back into xbl_config.elf
#
# The default values below match the QCM6490 reference design; adjust for
# other SoCs or board variants.
XBLCONFIG_DTB         ?= ""
XBLCONFIG_DTB:qcm6490 ?= "post-ddr-kodiak-1.0.dtb"
# XBLConfig section index that contains the post-DDR DTB
XBLCONFIG_DTB_SECTION ?= "8"

# ---------------------------------------------------------------------------
# Boot binaries location
# ---------------------------------------------------------------------------
# The firmware-qcom-boot recipe deploys all NHLOS images (.bin/.elf/.fv/
# .mbn) into DEPLOY_DIR_IMAGE/${QCOM_BOOT_IMG_SUBDIR}.  FVCreation.py
# resolves firmware paths using the <InputPath> field in FvUpdate.xml
# relative to this root.
#
# QCOM_BOOT_IMG_SUBDIR is set per-SoC in the machine include files.
# If you supply a custom FvUpdate.xml that uses <InputPath> referencing
# sub-directories, point BOOTBINS_DIR at a directory with the matching layout.
BOOTBINS_DIR ?= "${DEPLOY_DIR_IMAGE}/${SOC_FAMILY}"

# ---------------------------------------------------------------------------
# Build-host (native) dependencies
# ---------------------------------------------------------------------------
# edk2-basetools-native    - provides GenFfs, GenFv, GenerateCapsule.py, Common/
# virtual/bootbins         - provides the NHLOS firmware images consumed by FVCreation.py
# python3-native           - Python 3 interpreter on the build host
# dtc-native               - provides fdtdump used in the XBLConfig patching step
# python3-pyelftools-native - required by xblconfig_parser.py (ELF parsing)
# python3-dtc-native       - provides the pylibfdt Python bindings required by set_dtb_property.py

inherit python3native

DEPENDS = " \
    edk2-basetools-native \
    dtc-native \
    python3-pyelftools-native \
    python3-dtc-native \
"

# ---------------------------------------------------------------------------
# Tasks
# ---------------------------------------------------------------------------

do_configure[noexec] = "1"

# Ensure boot binaries are deployed before we try to consume them
do_compile[depends] += "${@'${QCOM_BOOT_FIRMWARE}:do_deploy' if d.getVar('QCOM_BOOT_FIRMWARE') else ''}"

do_compile() {
    # -----------------------------------------------------------------------
    # Working directory for capsule generation - mirrors the ci.yml default:
    #   defaults: run: working-directory: uefi_capsule_generation
    # -----------------------------------------------------------------------
    CAPSULE_DIR="${WORKDIR}/capsule_gen"
    rm -rf "${CAPSULE_DIR}"
    mkdir -p "${CAPSULE_DIR}"

    # Copy all committed Python scripts from cbsp-boot-utilities
    cp -r "${S}/uefi_capsule_generation/." "${CAPSULE_DIR}/"

    # -----------------------------------------------------------------------
    # Populate tools provided by edk2-basetools-native
    # -----------------------------------------------------------------------
    cp "${STAGING_BINDIR_NATIVE}/GenFfs" "${CAPSULE_DIR}/"
    cp "${STAGING_BINDIR_NATIVE}/GenFv"  "${CAPSULE_DIR}/"
    chmod +x "${CAPSULE_DIR}/GenFfs" "${CAPSULE_DIR}/GenFv"

    cp "${STAGING_DATADIR_NATIVE}/edk2-basetools/GenerateCapsule.py" \
       "${CAPSULE_DIR}/"
    cp -r "${STAGING_DATADIR_NATIVE}/edk2-basetools/Common" \
       "${CAPSULE_DIR}/"

    # -----------------------------------------------------------------------
    # FvUpdate.xml selection
    # SRC_URI:append override replaces the default file from the upstream repo.
    # -----------------------------------------------------------------------
    if [ -f "${WORKDIR}/FvUpdate.xml" ]; then
        cp "${WORKDIR}/FvUpdate.xml" "${CAPSULE_DIR}/FvUpdate.xml"
        bbnote "Using custom FvUpdate.xml from SRC_URI"
    else
        bbnote "Using default FvUpdate.xml from cbsp-boot-utilities"
    fi

    # Validate mandatory PKI variables before doing any real work
    if [ -z "${CAPSULE_ROOT_CER}" ]; then
        bbfatal "CAPSULE_ROOT_CER must point to the DER-encoded root CA certificate (QcFMPRoot.cer)"
    fi
    if [ -z "${CAPSULE_CERT_PEM}" ] || [ -z "${CAPSULE_ROOT_PUB}" ] || [ -z "${CAPSULE_SUB_PUB}" ]; then
        bbfatal "CAPSULE_CERT_PEM, CAPSULE_ROOT_PUB and CAPSULE_SUB_PUB must all be set for capsule signing"
    fi

    case "${CAPSULE_ROOT_CER}" in
        *test-keys*)
            bbwarn "firmware-qcom-capsule: using TEST PKI keys for capsule signing." \
                   " Replace CAPSULE_ROOT_CER/CAPSULE_CERT_PEM/CAPSULE_ROOT_PUB/CAPSULE_SUB_PUB" \
                   " with production keys before shipping."
            ;;
    esac

    cd "${CAPSULE_DIR}"

    bbnote "Convert certificate to HEX format"
    ROOT_INC="${CAPSULE_DIR}/QcFMPRoot.inc"
    python3 BinToHex.py "${CAPSULE_ROOT_CER}" "${ROOT_INC}"

    # Stage boot binaries so they are writable (XBLConfig patching modifies
    # xbl_config.elf in place, mirroring the `mv` in ci.yml)
    BOOTBINS_STAGED="${CAPSULE_DIR}/bootbins"
    mkdir -p "${BOOTBINS_STAGED}"
    cp -r "${BOOTBINS_DIR}/." "${BOOTBINS_STAGED}/"

    if [ -n "${XBLCONFIG_DTB}" ]; then
        bbnote "Patching QcCapsuleRootCert into XBLConfig ..."

        python3 xblconfig_parser.py \
            "${BOOTBINS_STAGED}/xbl_config.elf" dump \
            --out-dir "${BOOTBINS_STAGED}"

        ORIG_DTB="${BOOTBINS_STAGED}/${XBLCONFIG_DTB}"
        UPDATED_DTB="${BOOTBINS_STAGED}/${XBLCONFIG_DTB%.dtb}-updated.dtb"

        python3 set_dtb_property.py \
            "${ORIG_DTB}" \
            /sw/uefi/uefiplat \
            QcCapsuleRootCert \
            "@list:${ROOT_INC}" \
            "${UPDATED_DTB}"

        python3 xblconfig_parser.py \
            "${BOOTBINS_STAGED}/xbl_config.elf" replace \
            "${XBLCONFIG_DTB_SECTION}" \
            "${UPDATED_DTB}" \
            "${BOOTBINS_STAGED}/xbl_config_patched.elf"

        mv "${BOOTBINS_STAGED}/xbl_config_patched.elf" \
           "${BOOTBINS_STAGED}/xbl_config.elf"
    fi

    bbnote "Generate firmware version and create firmware volume"
    python3 SYSFW_VERSION_program.py \
        -Gen \
        -FwVer "${CAPSULE_FW_VERSION}" \
        -LFwVer "${CAPSULE_FW_LSV}" \
        -O SYSFW_VERSION.bin

    python3 FVCreation.py firmware.fv \
        -FvType "${CAPSULE_FV_TYPE}" \
        FvUpdate.xml \
        SYSFW_VERSION.bin \
        "${BOOTBINS_STAGED}"

    bbnote "Update JSON parameters"
    python3 UpdateJsonParameters.py \
        -j config.json \
        -f  "${CAPSULE_FV_TYPE}" \
        -b  SYSFW_VERSION.bin \
        -pf firmware.fv \
        -p  "${CAPSULE_CERT_PEM}" \
        -x  "${CAPSULE_ROOT_PUB}" \
        -oc "${CAPSULE_SUB_PUB}" \
        -g  "${CAPSULE_GUID}"

    bbnote "Generate capsule file"
    python3 GenerateCapsule.py \
        -e \
        -j config.json \
        -o "${PN}.cap" \
        --capflag PersistAcrossReset \
        -v
}

do_install[noexec] = "1"

inherit deploy

do_deploy() {
    install -d "${DEPLOYDIR}"
    install -m 0644 "${WORKDIR}/capsule_gen/${PN}.cap" "${DEPLOYDIR}/"

    # When XBLConfig was patched with the OEM root cert, deploy the updated
    # binary under a distinct name to avoid a deploy-manifest conflict with
    # firmware-qcom-bootbins (which already owns xbl_config.elf).
    # Use xbl_config-patched.elf for direct-flash workflows that require the
    # cert-injected binary (e.g. initial provisioning before OTA is active).
    if [ -n "${XBLCONFIG_DTB}" ]; then
        install -m 0644 "${WORKDIR}/capsule_gen/bootbins/xbl_config.elf" \
            "${DEPLOYDIR}/xbl_config-patched.elf"
    fi
}
addtask deploy before do_build after do_compile

PACKAGE_ARCH = "${MACHINE_ARCH}"
