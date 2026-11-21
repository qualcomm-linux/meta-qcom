DESCRIPTION = "Fuse Blower images that provision secure boot on Qualcomm devices"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

COMPATIBLE_MACHINE = "(qcom)"

inherit nopackages deploy qcom-sign-common

# Stages to build, one sec-<stage>.elf each.  No default: the build
# configuration names the stages it wants (see ci/fuse-blower.yml), so
# nothing is blown that was not asked for.
QCOM_FUSE_BLOWER_STAGES ?= ""

# Fuses every stage blows, besides the root certificate hash taken from
# QCOM_FIRMWARE_SIGN_ROOT_HASH: authentication against that hash and the
# OEM identifiers the signed images are bound to.
QCOM_FUSE_BLOWER_FUSES ?= "\
    --fuse-oem-secure-boot1-pk-hash-in-fuse --fuse-oem-secure-boot1-auth-en \
    --fuse-oem-secure-boot2-pk-hash-in-fuse --fuse-oem-secure-boot2-auth-en \
    --fuse-oem-secure-boot3-pk-hash-in-fuse --fuse-oem-secure-boot3-auth-en \
    --fuse-oem-hw-id=${QCOM_FUSE_OEM_HW_ID} \
    --fuse-oem-product-id=${QCOM_FUSE_OEM_PRODUCT_ID} \
"

# Fuses a stage blows on top of QCOM_FUSE_BLOWER_FUSES: "basic" turns
# secure boot on, "closing" also disables debug and locks the fuse
# regions.  The names come from the Security Profile (sectools fuse-blower
# --help --security-profile <profile>); a machine include overrides a
# stage whose profile lacks some.
QCOM_FUSE_BLOWER_STAGE_FUSES[basic] ?= ""
QCOM_FUSE_BLOWER_STAGE_FUSES[closing] ?= "\
    --fuse-oem-secure-boot-fec-enable \
    --fuse-wdog-en \
    --fuse-shared-qsee-spiden-disable --fuse-shared-qsee-spniden-disable \
    --fuse-shared-mss-dbgen-disable --fuse-shared-mss-niden-disable \
    --fuse-shared-cp-dbgen-disable --fuse-shared-cp-niden-disable \
    --fuse-shared-ns-dbgen-disable --fuse-shared-ns-niden-disable \
    --fuse-apps-dbgen-disable --fuse-apps-niden-disable \
    --fuse-shared-misc-debug-disable \
    --fuse-eku-enforcement-en \
    --fuse-anti-rollback-feature-en=0xF \
    --fuse-read-permissions-write-disable \
    --fuse-oem-configuration-write-disable \
    --fuse-secondary-key-derivation-key-read-disable \
    --fuse-public-key-hash-0-write-disable \
    --fuse-oem-secure-boot-write-disable \
    --fuse-secondary-key-derivation-key-write-disable \
    --fuse-secondary-key-derivation-key-fec-enable \
    --fuse-fec-enables-write-disable \
"

# OEM ID of the devices a stage's signature is valid for: a blank chip
# reports 0, a chip that went through "basic" reports the OEM HW ID blown
# there.
QCOM_FUSE_BLOWER_STAGE_OEM_ID[basic] ?= "0x0"
QCOM_FUSE_BLOWER_STAGE_OEM_ID[closing] ?= "${QCOM_FUSE_OEM_HW_ID}"

# Chip serial numbers the images are valid for.  Empty means every device
# with the stage's OEM ID and QCOM_FUSE_OEM_PRODUCT_ID.
QCOM_FUSE_BLOWER_SERIALS ?= ""

PACKAGE_ARCH = "${MACHINE_ARCH}"
INHIBIT_DEFAULT_DEPS = "1"

python () {
    if d.getVar('QCOM_FUSE_BLOWER_ENABLE') != '1':
        raise bb.parse.SkipRecipe('set QCOM_FUSE_BLOWER_ENABLE = "1" (see ci/fuse-blower.yml)')
    if d.getVar('QCOM_FIRMWARE_SIGN_ENABLE') != '1':
        raise bb.parse.SkipRecipe('needs QCOM_FIRMWARE_SIGN_ENABLE = "1" (see ci/secure-boot.yml)')
}

do_configure[noexec] = "1"
do_install[noexec] = "1"

def qcom_fuse_blower_run(cmd):
    try:
        return bb.process.run(cmd)[0]
    except bb.process.ExecutionError as e:
        bb.fatal("%s failed:\n%s" % (' '.join(cmd[:2]), e.stderr or e.stdout))

python do_compile() {
    bb.build.exec_func('qcom_firmware_sign_check_prereqs', d)
    for var in ('QCOM_FUSE_OEM_HW_ID', 'QCOM_FUSE_OEM_PRODUCT_ID'):
        if not d.getVar(var):
            bb.fatal('%s is empty (see ci/ecdsa-secure-boot-test-keys.yml)' % var)
    if not d.getVar('QCOM_FUSE_BLOWER_STAGES'):
        bb.fatal('QCOM_FUSE_BLOWER_STAGES is empty: name the stages to build (see ci/fuse-blower.yml)')

    sectools = d.getVar('QCOM_FIRMWARE_SIGN_SECTOOLS')
    profile = d.getVar('QCOM_FIRMWARE_SIGN_SECPROFILE_PATH')
    with open(d.getVar('QCOM_FIRMWARE_SIGN_ROOT_HASH')) as f:
        root_hash = '0x' + f.read().split()[1]

    fuses = d.getVar('QCOM_FUSE_BLOWER_FUSES').split()
    fuses.append('--fuse-pk-hash-0=' + root_hash)
    skdk = d.getVar('QCOM_FUSE_SEC_KEY_DERIVATION_KEY')
    if skdk:
        fuses.append('--fuse-sec-key-derivation-key=' + skdk)

    restrict = ['--oem-product-id=' + d.getVar('QCOM_FUSE_OEM_PRODUCT_ID'),
                '--anti-rollback-version=' + d.getVar('QCOM_FIRMWARE_SIGN_ANTI_ROLLBACK')]
    serials = d.getVar('QCOM_FUSE_BLOWER_SERIALS').split()
    if serials:
        restrict += ['--serial-number'] + serials

    for stage in d.getVar('QCOM_FUSE_BLOWER_STAGES').split():
        stage_fuses = d.getVarFlag('QCOM_FUSE_BLOWER_STAGE_FUSES', stage, True)
        oem_id = d.getVarFlag('QCOM_FUSE_BLOWER_STAGE_OEM_ID', stage, True)
        if stage_fuses is None or not oem_id:
            bb.fatal('stage "%s": set QCOM_FUSE_BLOWER_STAGE_FUSES[%s] and QCOM_FUSE_BLOWER_STAGE_OEM_ID[%s]'
                     % (stage, stage, stage))

        outfile = os.path.join(d.getVar('B'), 'sec-%s.elf' % stage)
        qcom_fuse_blower_run([sectools, 'fuse-blower', '--security-profile', profile]
                             + fuses + stage_fuses.split()
                             + ['--generate', '--sign', '--signing-mode=LOCAL',
                                '--root-certificate=' + d.getVar('QCOM_FIRMWARE_SIGN_ROOT_CERT'),
                                '--ca-certificate=' + d.getVar('QCOM_FIRMWARE_SIGN_CA_CERT'),
                                '--ca-key=' + d.getVar('QCOM_FIRMWARE_SIGN_CA_KEY'),
                                '--oem-id=' + oem_id]
                             + restrict + ['--outfile', outfile])
        qcom_fuse_blower_run([sectools, 'fuse-blower', '--verify-root', root_hash, outfile])

        # The fuse list, for checking before the image is programmed.
        report = qcom_fuse_blower_run([sectools, 'fuse-blower', outfile,
                                       '--security-profile', profile, '--inspect'])
        with open(outfile[:-4] + '.txt', 'w') as f:
            f.write(report)
}

do_compile[file-checksums] += "${@qcom_firmware_sign_key_checksums(d)}"
do_compile[vardeps] += "${@' '.join('QCOM_FUSE_BLOWER_STAGE_FUSES[%s] QCOM_FUSE_BLOWER_STAGE_OEM_ID[%s]' % (s, s) \
                                     for s in d.getVar('QCOM_FUSE_BLOWER_STAGES').split())}"

do_deploy() {
    install -d ${DEPLOYDIR}/fuse-blower
    install -m 0644 ${B}/sec-*.elf ${B}/sec-*.txt ${DEPLOYDIR}/fuse-blower/
}
addtask deploy after do_compile before do_build
