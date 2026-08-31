#
# Copyright (c) 2026 Qualcomm Innovation Center, Inc. All rights reserved.
#
# SPDX-License-Identifier: BSD-3-Clause-Clear
#
# Build class for UEFI FMP capsule generation on Qualcomm platforms.

# Firmware version embedded in the capsule header
CAPSULE_FW_VERSION ?= "0.0.1.2"
# Lowest supported version (anti-rollback floor)
CAPSULE_FW_LSV     ?= "0.0.1.1"
# Firmware volume type label passed to FVCreation.py / UpdateJsonParameters.py
CAPSULE_FV_TYPE    ?= "SYS_FW"
# FMP ESRT GUID that identifies this firmware on the target
CAPSULE_GUID       ?= "6F25BFD2-A165-468B-980F-AC51A0A45C52"

# ---------------------------------------------------------------------------
# OEM PKI material (must be supplied by the integrator)
# ---------------------------------------------------------------------------
# These variables must be set explicitly - there are no built-in defaults.
# For CI builds, include ci/capsule-test-keys.yml which sets them to the
# test keys stored under ci/test-keys/.  For production builds, point them
# at keys from a secure location (secrets manager, signing recipe, etc.).
#
# CAPSULE_ROOT_CER - DER-encoded root CA certificate (QcFMPRoot.cer)
#                    Converted to hex INC format by BinToHex.py before use.
CAPSULE_ROOT_CER ?= ""
# CAPSULE_CERT_PEM - Combined signing key + leaf certificate in PEM format
#                    (QcFMPCert.pem, output of `openssl pkcs12 ... -nodes`)
CAPSULE_CERT_PEM ?= ""
# CAPSULE_ROOT_PUB - Root CA public key in PEM format (QcFMPRoot.pub.pem)
CAPSULE_ROOT_PUB ?= ""
# CAPSULE_SUB_PUB  - Intermediate CA public key in PEM format (QcFMPSub.pub.pem)
CAPSULE_SUB_PUB  ?= ""

# ---------------------------------------------------------------------------
# OEM root certificate injection
# ---------------------------------------------------------------------------
# QcCapsuleRootCert is injected into the boot config ELFs by
# firmware-qcom-oem-cert (classes-recipe/qcom-oem-cert.bbclass), not here.
# It has to happen there because the certificate must be in place before
# the config ELFs are signed, and this recipe runs after the boot firmware
# has already been deployed.  This class consumes the result: the
# cert-bearing copies are staged over the pristine boot binaries so the
# capsule firmware volume is built from the same images the device runs.

# ---------------------------------------------------------------------------
# Boot binaries location
# ---------------------------------------------------------------------------
# FVCreation.py resolves firmware paths using the <InputPath> field in
# FvUpdate.xml relative to BOOTBINS_DIR.
# QCOM_BOOT_FILES_SUBDIR is set per-SoC in the machine include files.
BOOTBINS_DIR ?= "${DEPLOY_DIR_IMAGE}/${QCOM_BOOT_FILES_SUBDIR}"

# ---------------------------------------------------------------------------
# Custom / generated FvUpdate.xml
# ---------------------------------------------------------------------------
# To provide a board/project-specific capsule layout, append your file to
# SRC_URI and name it FvUpdate.xml, e.g. in a .bbappend or local.conf:
#   SRC_URI:append = " file://my-board-FvUpdate.xml;subdir=fvupdate"
# The class detects a custom FvUpdate.xml placed in ${WORKDIR} and uses
# it in place of the upstream default.
#
# Alternatively, set CAPSULE_ENTRIES to a space-separated list of entry
# names to generate FvUpdate.xml at build time.  For each name FOO define
# the following flags on CAPSULE_ENTRY_FOO:
#
#   [binary]           - input filename resolved relative to BOOTBINS_STAGED
#   [dest_disk]        - destination DiskType  (e.g. SPINOR, UFS_LUN1)
#   [dest_partition]   - destination PartitionName
#   [dest_guid]        - destination PartitionTypeGUID
#   [backup_disk]      - backup DiskType       (optional)
#   [backup_partition] - backup PartitionName  (optional)
#   [backup_guid]      - backup PartitionTypeGUID (optional)
#
# When CAPSULE_ENTRIES is empty the class falls back to a static FvUpdate.xml
# provided via SRC_URI or the default bundled in cbsp-boot-utilities.
CAPSULE_FLASH_TYPE ?= "UFS"
CAPSULE_ENTRIES    ?= ""

inherit python3native deploy

CAPSULE_DIR = "${WORKDIR}/capsule_gen"

do_compile[depends] += "cbsp-boot-utilities-native:do_populate_sysroot \
                        firmware-qcom-oem-cert:do_deploy"
do_compile[dirs] = "${CAPSULE_DIR}"
do_compile[cleandirs] = "${CAPSULE_DIR}"

# QA check: warn when test PKI keys are used instead of production keys.
# Recipes may silence this by adding to INSANE_SKIP:
#   INSANE_SKIP:<pn> += "test-pki-keys"
python () {
    pn = d.getVar('PN')

    # Validate that all mandatory PKI material is set in one go so the user
    # gets a single, complete error rather than tripping on each variable.
    required = ('CAPSULE_ROOT_CER', 'CAPSULE_CERT_PEM',
                'CAPSULE_ROOT_PUB', 'CAPSULE_SUB_PUB')
    missing = [v for v in required if not d.getVar(v)]
    if missing:
        raise bb.parse.SkipRecipe(
            '%s: capsule PKI material is missing: %s. '
            'Set all of CAPSULE_ROOT_CER, CAPSULE_CERT_PEM, '
            'CAPSULE_ROOT_PUB and CAPSULE_SUB_PUB (see ci/capsule-test-keys.yml '
            'for a CI/development overlay).' % (pn, ', '.join(missing)))

    skip = (d.getVar('INSANE_SKIP') or '').split()
    skip += (d.getVar('INSANE_SKIP:' + pn) or '').split()
    if 'test-pki-keys' in skip:
        return
    if 'test-keys' in (d.getVar('CAPSULE_ROOT_CER') or ''):
        bb.warn('%s: built with test PKI keys; replace CAPSULE_ROOT_CER, '
                'CAPSULE_CERT_PEM, CAPSULE_ROOT_PUB and CAPSULE_SUB_PUB '
                'with production keys before shipping' % pn)
}

do_configure[noexec] = "1"

# Ensure boot binaries are deployed before we try to consume them
do_compile[depends] += "${@'${QCOM_BOOT_FIRMWARE}:do_deploy' if d.getVar('QCOM_BOOT_FIRMWARE') else ''}"

# Pull in the kernel DTB when capsule includes a dtb entry.
do_compile[depends] += "${@'virtual/kernel:do_deploy' if 'dtb' in d.getVar('CAPSULE_ENTRIES').split() else ''}"
do_compile[depends] += "${@'virtual/kernel:do_qcom_dtbbin_deploy' if 'dtb' in d.getVar('CAPSULE_ENTRIES').split() and 'linux-qcom-dtbbin' in (d.getVar('KERNEL_CLASSES') or '').split() else ''}"

python generate_fvupdate() {
    """Generate FvUpdate.xml from CAPSULE_ENTRIES when the variable is set."""
    import os

    entries = d.getVar('CAPSULE_ENTRIES').split()
    if not entries:
        return

    flash_type = d.getVar('CAPSULE_FLASH_TYPE')
    outdir     = d.getVar('B')

    lines = [
        '<?xml version="1.0" encoding="utf-8"?>',
        '<FVItems>',
        '    <Metadata>',
        '      <BreakingChangeNumber>0</BreakingChangeNumber>',
        '      <FlashType>%s</FlashType>' % flash_type,
        '    </Metadata>',
        '',
    ]

    for name in entries:
        def flag(f):
            return d.getVarFlag('CAPSULE_ENTRY_%s' % name, f) or ''

        binary    = flag('binary')
        dest_disk = flag('dest_disk')
        dest_part = flag('dest_partition')
        dest_guid = flag('dest_guid')
        bkup_disk = flag('backup_disk')
        bkup_part = flag('backup_partition')
        bkup_guid = flag('backup_guid')

        if not binary or not dest_disk or not dest_part:
            bb.warn('CAPSULE_ENTRY_%s: binary, dest_disk and dest_partition '
                    'are required; skipping entry' % name)
            continue

        lines += [
            '  <FwEntry>',
            '    <InputBinary>%s</InputBinary>' % binary,
            '    <InputPath>Images</InputPath>',
            '    <Operation>UPDATE</Operation>',
            '    <UpdateType>UPDATE_PARTITION</UpdateType>',
            '    <BackupType>BACKUP_PARTITION</BackupType>',
            '    <Dest>',
            '      <DiskType>%s</DiskType>' % dest_disk,
            '      <PartitionName>%s</PartitionName>' % dest_part,
            '      <PartitionTypeGUID>%s</PartitionTypeGUID>' % dest_guid,
            '    </Dest>',
        ]

        if bkup_part:
            lines += [
                '    <Backup>',
                '      <DiskType>%s</DiskType>' % bkup_disk,
                '      <PartitionName>%s</PartitionName>' % bkup_part,
                '      <PartitionTypeGUID>%s</PartitionTypeGUID>' % bkup_guid,
                '    </Backup>',
            ]

        lines += ['  </FwEntry>', '']

    lines.append('</FVItems>')

    os.makedirs(outdir, exist_ok=True)
    out = os.path.join(outdir, 'FvUpdate.xml')
    with open(out, 'w') as f:
        f.write('\n'.join(lines))
    bb.debug(1, 'Generated %s from CAPSULE_ENTRIES' % out)
}

do_compile[prefuncs] += "generate_fvupdate"

do_compile() {
    CBSP_DATA="${STAGING_DATADIR_NATIVE}/cbsp-boot-utilities"

    # Use a board-specific FvUpdate.xml if provided via SRC_URI:append or
    # generated from CAPSULE_ENTRIES, otherwise fall back to the default
    # bundled in cbsp-boot-utilities.
    if [ -f "${B}/FvUpdate.xml" ]; then
        FVUPDATE_XML="${B}/FvUpdate.xml"
    elif [ -f "${WORKDIR}/FvUpdate.xml" ]; then
        FVUPDATE_XML="${WORKDIR}/FvUpdate.xml"
    else
        FVUPDATE_XML="${CBSP_DATA}/FvUpdate.xml"
    fi

    cd "${CAPSULE_DIR}"

    # Stage boot binaries so they are writable (XBLConfig patching modifies
    # xbl_config.elf in place)
    BOOTBINS_STAGED="${CAPSULE_DIR}/bootbins"
    mkdir -p "${BOOTBINS_STAGED}"
    cp -r "${BOOTBINS_DIR}/." "${BOOTBINS_STAGED}/"

    # Stage kernel DTB vfat image as dtb.bin so FVCreation.py can find it
    # when FvUpdate.xml references dtb.bin.  Only needed when CAPSULE_ENTRIES
    # includes a dtb entry (avoids touching platforms that don't need it).
    if echo "${CAPSULE_ENTRIES}" | grep -qw dtb && \
            [ -n "${QCOM_DTB_DEFAULT}" ] && \
            [ -f "${DEPLOY_DIR_IMAGE}/dtb-${QCOM_DTB_DEFAULT}-image.vfat" ]; then
        cp "${DEPLOY_DIR_IMAGE}/dtb-${QCOM_DTB_DEFAULT}-image.vfat" \
            "${BOOTBINS_STAGED}/dtb.bin"
    fi

    # Overlay the cert-bearing config ELFs deployed by
    # firmware-qcom-oem-cert, so the capsule firmware volume carries the
    # same images the device boots.  Optional: hamoa has no xbl_config.elf.
    if [ -f "${DEPLOY_DIR_IMAGE}/xbl_config-with-oem-cert.elf" ]; then
        install -m 0644 "${DEPLOY_DIR_IMAGE}/xbl_config-with-oem-cert.elf" \
            "${BOOTBINS_STAGED}/${QCOM_XBL_CONFIG}"
    fi

    qcom-capsule-tool sysfw-version-create \
        -Gen \
        -FwVer "${CAPSULE_FW_VERSION}" \
        -LFwVer "${CAPSULE_FW_LSV}" \
        -O SYSFW_VERSION.bin

    qcom-capsule-tool fv-create firmware.fv \
        -FvType "${CAPSULE_FV_TYPE}" \
        "${FVUPDATE_XML}" \
        SYSFW_VERSION.bin \
        "${BOOTBINS_STAGED}"

    qcom-capsule-tool update-json \
        -j config.json \
        -f  "${CAPSULE_FV_TYPE}" \
        -b  SYSFW_VERSION.bin \
        -pf firmware.fv \
        -p  "${CAPSULE_CERT_PEM}" \
        -x  "${CAPSULE_ROOT_PUB}" \
        -oc "${CAPSULE_SUB_PUB}" \
        -g  "${CAPSULE_GUID}"

    qcom-capsule-tool generate-capsule \
        -e \
        -j config.json \
        -o "${PN}.cap" \
        --capflag PersistAcrossReset \
        -v
}

do_install() {
    install -d "${D}${nonarch_base_libdir}/firmware/efi"
    install -m 0644 "${CAPSULE_DIR}/${PN}.cap" "${D}${nonarch_base_libdir}/firmware/efi/"
}

PACKAGES = "${PN}"
FILES:${PN} = "${nonarch_base_libdir}/firmware/efi/${PN}.cap"

do_deploy() {
    install -d "${DEPLOYDIR}"
    install -m 0644 "${CAPSULE_DIR}/${PN}.cap" "${DEPLOYDIR}/"
}
addtask deploy before do_build after do_compile

PACKAGE_ARCH = "${MACHINE_ARCH}"
