inherit kernel-arch

require conf/image-fitimage.conf

DEPENDS += "\
    u-boot-tools-native \
"

# Set default configuration
FIT_CONF_DEFAULT_DTB = "1"

MKIMAGE ?= "${STAGING_BINDIR_NATIVE}/mkimage"

QCOMFIT_DEPLOYDIR = "${WORKDIR}/qcom_fitimage_deploy-${PN}"

do_generate_qcom_fitimage[depends] += "qcom-dtb-metadata:do_deploy"
do_generate_qcom_fitimage[cleandirs] += "${QCOMFIT_DEPLOYDIR}"
python do_generate_qcom_fitimage() {
    import sys, os, shutil, re
    import oe.types
    from pathlib import Path

    fit_dir = d.getVar('QCOMFIT_DEPLOYDIR')

    itsfile = os.path.join(fit_dir, "qclinux-fit-image.its")
    fitname = os.path.join(fit_dir, "qclinuxfitImage")

    # Add the custom 'lib' directory to sys.path
    # so that Python can import our custom FIT image helper module (qcom_dtb_only_fitimage.py)
    # during BitBake build time.
    libdir = Path(d.getVar('QCOM_PYTHON_LIB'))
    if libdir and str(libdir) not in sys.path:
        sys.path.insert(0, str(libdir))

    from qcom_dtb_only_fitimage import QcomItsNodeRoot

    root_node = QcomItsNodeRoot(
        d.getVar("FIT_DESC"),
        d.getVar("FIT_ADDRESS_CELLS"),
        d.getVar("FIT_CONF_PREFIX"),
        d.getVar("MKIMAGE"),
    )

    root_node.set_extra_opts(d.getVar("FIT_DTB_MKIMAGE_EXTRA_OPTS") or "")

    # Prepend qcom-metadata.dtb to KERNEL_DEVICETREE
    kernel_devicetree = d.getVar('KERNEL_DEVICETREE') or ""
    kernel_devicetree = ('qcom-metadata.dtb ' + kernel_devicetree).strip()

    deploy_dir_image = d.getVar('DEPLOY_DIR_IMAGE') 
    dtb_dir = os.path.join(d.getVar('B'), "arch", d.getVar('ARCH'), "boot", "dts", "qcom")
    qcom_meta_src = os.path.join(deploy_dir_image, 'qcom-metadata.dtb')
    qcom_meta_dst = os.path.join(dtb_dir, 'qcom-metadata.dtb')
    if not os.path.exists(qcom_meta_src):
        bb.fatal("Missing qcom-metadata.dtb at %s (ensure qcom-dtb-metadata:do_deploy ran)" % qcom_meta_src)
    shutil.copy(qcom_meta_src, qcom_meta_dst)

    for dtb in kernel_devicetree.split():
        dtb_name = os.path.basename(dtb)
        dtb_base = os.path.splitext(dtb_name)[0]
        compatible = d.getVarFlag("FIT_DTB_COMPATIBLE", dtb_base) or ""
        dtb_path = os.path.join(dtb_dir, f"{dtb_base}.dtb")
        if not compatible:
            bb.warn(f"FIT_DTB_COMPATIBLE[{dtb_base}] is not set. ")
        root_node.fitimage_emit_section_dtb(
            dtb_name, dtb_path,
            False,
            False,
            True,
            compatible_str=compatible,
        )

    root_node.fitimage_emit_section_config(d.getVar("FIT_CONF_DEFAULT_DTB"))

    root_node.write_its_file(itsfile)

    # Generate custom ITS file
    with open(itsfile, 'r') as f:
        content = f.read()

    # Replace type for qcom metadata node
    content = re.sub(
        r'(fdt-qcom-metadata\.dtb\s*\{[^}]*?)type\s*=\s*"flat_dt";',
        r'\1type = "qcom_metadata";',
        content, flags=re.DOTALL
    )

    # Remove conf-0 entry which corresponds to fdt-0
    content = re.sub(r'conf-0\s*\{[^}]*\};', '', content, flags=re.DOTALL)

    with open(itsfile, 'w') as f:
        f.write(content)

    root_node.run_mkimage_assemble(itsfile, fitname)
}
addtask generate_qcom_fitimage after do_populate_sysroot do_packagedata before do_qcom_dtbbin_deploy

# Setup sstate, see deploy.bbclass
SSTATETASKS += "do_generate_qcom_fitimage"
do_generate_qcom_fitimage[sstate-inputdirs] = "${QCOMFIT_DEPLOYDIR}"
do_generate_qcom_fitimage[sstate-outputdirs] = "${DEPLOY_DIR_IMAGE}"

python do_generate_qcom_fitimage_setscene () {
    sstate_setscene(d)
}
addtask do_generate_qcom_fitimage_setscene

do_generate_qcom_fitimage[stamp-extra-info] = "${MACHINE_ARCH}"
