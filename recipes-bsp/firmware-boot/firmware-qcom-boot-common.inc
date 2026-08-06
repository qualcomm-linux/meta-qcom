# Install NHLOS boot binaries in DEPLOY_DIR

S = "${UNPACKDIR}/${BOOTBINARIES}"

QCOM_BOOT_IMG_SUBDIR ?= ""

# Rootfs directory holding the per-board boot firmware build metadata
QCOM_BOOT_INFO_DIR ?= "${datadir}/qcom-boot-firmware"

INHIBIT_DEFAULT_DEPS = "1"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

inherit deploy

python do_install() {
    import os
    import zipfile

    # Prefer the SRC_URI entry named "bootbinaries"; legacy recipes don't
    # name their sole archive, so fall back to the first .zip entry.
    fetch = bb.fetch2.Fetch(d.getVar('SRC_URI').split(), d)
    bootbin = None
    for url in fetch.urls:
        ud = fetch.ud[url]
        if ud.parm.get('name') == 'bootbinaries':
            bootbin = ud
            break
        if bootbin is None and ud.localpath.endswith('.zip'):
            bootbin = ud

    # Qualcomm releases embed build info (Meta_Build_ID, per-image build
    # IDs) as the zip archive comment; releases in other archive formats
    # or without a comment carry no such metadata.
    comment = ''
    if bootbin is not None and zipfile.is_zipfile(bootbin.localpath):
        with zipfile.ZipFile(bootbin.localpath) as z:
            comment = z.comment.decode('utf-8', errors='replace')
        comment = comment.replace('\r\n', '\n').replace('\r', '\n').strip()

    info = 'Recipe: %s\n' % d.getVar('PN')
    info += 'Version: %s\n' % d.getVar('PV')
    if bootbin is not None:
        info += 'Archive: %s\n' % os.path.basename(bootbin.localpath)
        sha256 = getattr(bootbin, 'sha256_expected', None)
        if sha256:
            info += 'Archive-SHA256: %s\n' % sha256
    if comment:
        info += '\n' + comment + '\n'

    board = (d.getVar('QCOM_BOOT_IMG_SUBDIR') or '').strip('/').split('/')[0] \
        or d.getVar('BPN')
    destdir = os.path.join(d.getVar('D') + d.getVar('QCOM_BOOT_INFO_DIR'), board)
    bb.utils.mkdirhier(destdir)
    dest = os.path.join(destdir, 'build-info.txt')
    with open(dest, 'w') as f:
        f.write(info)
    os.chmod(dest, 0o644)
}

FILES:${PN} += "${QCOM_BOOT_INFO_DIR}"

do_deploy() {
    install -d ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}
    find "${S}" -maxdepth 1 -name 'gpt_*.bin' -delete
    find "${S}" -maxdepth 1 -name 'zeros_*.bin' -delete
    find "${S}" -maxdepth 1 -name '*.bin' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;
    find "${S}" -maxdepth 1 -name '*.elf' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;
    find "${S}" -maxdepth 1 -name '*.fv' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;
    find "${S}" -maxdepth 1 -name '*.lzma' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;
    find "${S}" -maxdepth 1 -name '*.mbn' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;
    find "${S}" -maxdepth 1 -name '*.melf' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;
    find "${S}" -maxdepth 1 -name 'qsahara_*.xml' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;
    find "${S}" -maxdepth 1 -name '*.xz' -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR} \;
    install -m 0644 ${D}${QCOM_BOOT_INFO_DIR}/*/build-info.txt ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}/

}
addtask deploy before do_build after do_install

inherit allarch
