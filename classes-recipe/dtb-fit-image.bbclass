
inherit kernel-arch

require conf/image-fitimage.conf

DEPENDS += "\
    u-boot-tools-native dtc-native \
    qcom-dtb-metadata \
"

# Set default configuration
FIT_CONF_DEFAULT_DTB = "1"

do_compile_qcom_fitimage[depends] += "qcom-dtb-metadata:do_deploy \
                                      u-boot-tools-native:do_populate_sysroot \
                                     "

MKIMAGE ?= "${STAGING_BINDIR_NATIVE}/mkimage"

python do_compile_qcom_fitimage() {
    import sys, os, shutil, re
    import oe.types
    from pathlib import Path

    itsfile = os.path.join(d.getVar('DEPLOYDIR'), "qclinux-fit-image.its")
    fitname = os.path.join(d.getVar('DEPLOYDIR'), "qclinuxfitImage")

    # Prevent Python from writing .pyc/.pyo files during build.
    sys.dont_write_bytecode = True
    os.environ['PYTHONDONTWRITEBYTECODE'] = '1'

    # Add the custom 'lib' directory (two levels up from this recipe file) to sys.path
    # so that Python can import our custom FIT image helper module (qcom_dtb_only_fitimage.py)
    # during BitBake build time.
    customfit = Path(d.getVar('FILE')).parents[2] / 'lib'
    if customfit.is_dir() and str(customfit) not in sys.path:
        sys.path.insert(0, str(customfit))
        bb.note("Added to sys.path (build-time): %s" % customfit)

    from qcom_dtb_only_fitimage import QcomItsNodeRoot

    root_node = QcomItsNodeRoot(
        d.getVar("FIT_DESC"),
        d.getVar("FIT_ADDRESS_CELLS"),
        d.getVar("FIT_CONF_PREFIX"),
        d.getVar("MKIMAGE"),
    )
    bb.debug(1, "Global root_node initialized")

    root_node.set_extra_opts(d.getVar("MKIMAGE_EXTRA_OPTS") or "")

    deploy_dir_image = d.getVar('DEPLOY_DIR_IMAGE')
    deploy_dir = d.getVar('DEPLOYDIR')

    # Prepend qcom-metadata.dtb to KERNEL_DEVICETREE
    kernel_devicetree = d.getVar('KERNEL_DEVICETREE') or ""
    kernel_devicetree = ('qcom-metadata.dtb ' + kernel_devicetree).strip()

    qcom_meta_src = os.path.join(deploy_dir_image, 'qcom-metadata.dtb')
    qcom_meta_dst = os.path.join(deploy_dir, 'qcom-metadata.dtb')
    if not os.path.exists(qcom_meta_src):
        bb.fatal("Missing qcom-metadata.dtb at %s (ensure qcom-dtb-metadata:do_deploy ran)" % qcom_meta_src)
    shutil.copy(qcom_meta_src, qcom_meta_dst)

    for dtb in kernel_devicetree.split():
        dtb_name = os.path.basename(dtb)
        dtb_base = os.path.splitext(dtb_name)[0]
        compatible = d.getVarFlag("FIT_DTB_COMPATIBLE", dtb_base) or ""
        abs_path = os.path.join(deploy_dir_image, dtb_name)
        root_node.fitimage_emit_section_dtb(
            dtb_name, dtb_name,
            False,
            False,
            True,
            compatible_str=compatible,
            dtb_abspath=abs_path
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

do_deploy_qcom_fitimage() {
    install -m 0644 "${DEPLOYDIR}/qclinuxfitImage" "${DEPLOY_DIR_IMAGE}/qclinuxfitImage"
    install -m 0644 "${DEPLOYDIR}/qclinux-fit-image.its" "${DEPLOY_DIR_IMAGE}/qclinux-fit-image.its"
}


addtask compile_qcom_fitimage after do_deploy
addtask deploy_qcom_fitimage after do_compile_qcom_fitimage do_deploy
