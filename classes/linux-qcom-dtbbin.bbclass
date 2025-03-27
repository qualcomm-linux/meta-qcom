#
# Copyright (c) 2024 Qualcomm Innovation Center, Inc. All rights reserved.
#
# SPDX-License-Identifier: BSD-3-Clause-Clear
#

# The list of DTBOs to be overlaid on top of the base kernel device-tree must
# be specified in the DTB-specific KERNEL_DTB_OVERLAYS flag. For example,
# to overlay my-dt-overlay.dtbo on top of kernel-dt.dtb, use the following:
# KERNEL_DTB_OVERLAYS[kernel-dt] = "my-dt-overlay.dtbo"
#
# If there are multiple DTBO files, they must be listed in dependency order.
# That is, the DTBO file that depends on another DTBO file should be listed
# later in the list. For example, if my-dtbo1.dtbo depends on my-dtbo2.dtbo,
# they should be mentioned as:
# KERNEL_DTB_OVERLAYS[kernel-dt] = "my-dtbo2.dtbo my-dtbo1.dtbo"
#
# Recipes generating out-of-tree DTBOs must be listed in KERNEL_DTBO_PROVIDERS
# for BitBake to set the correct task dependency order and ensure DTBOs are built
# and available for overlaying. For example, if the 'my-dtb.bb' recipe generates
# 'my-dt-overlay.dtbo', it should be mentioned as:
# KERNEL_DTBO_PROVIDERS = "my-dtb"

KERNEL_DTBO_PROVIDERS ?= ""
def get_dtbo_providers(d) :
    dtbo_providers = ""
    for recipe in d.getVar('KERNEL_DTBO_PROVIDERS').split():
        dtbo_providers = recipe + ":do_deploy " + dtbo_providers

    return dtbo_providers


DTBMERGE_DIR = "${WORKDIR}/qcom_dtbmerge-${PN}"

qcom_dtbmerge[cleandirs] = "${DTBMERGE_DIR}"
python qcom_dtbmerge() {
    import os, shutil, subprocess

    fdtoverlay_bin = d.getVar('STAGING_BINDIR_NATIVE') + "/fdtoverlay"
    org_dtbo_dir = d.getVar('DEPLOY_DIR_IMAGE') + "/" + "devicetree"
    dtbo_dir = d.getVar('DTBMERGE_DIR')

    kernel_dt = d.getVar('KERNEL_DEVICETREE')
    for kdt in kernel_dt.split():
        org_kdtb = os.path.join(d.getVar('D'), d.getVar('KERNEL_DTBDEST'), os.path.basename(kdt))

        # Rename and copy original kernel devicetree files
        kdtb = os.path.basename(org_kdtb) + ".0"
        shutil.copy2(org_kdtb, os.path.join(dtbo_dir, kdtb))

        # Find and append matching dtbos for each dtb
        dtb = os.path.basename(org_kdtb)
        dtb_name = dtb.rsplit('.', 1)[0]
        dtbo_list =(d.getVarFlag('KERNEL_DTB_OVERLAYS', dtb_name) or "").split()
        bb.debug(1, "%s dtbo_list: %s" % (dtb_name, dtbo_list))
        dtbos_found = 0
        for dtbo_file in dtbo_list:
            dtbos_found += 1
            dtbo = os.path.join(org_dtbo_dir, dtbo_file)
            pre_kdtb = os.path.join(dtbo_dir, dtb + "." + str(dtbos_found - 1))
            post_kdtb = os.path.join(dtbo_dir, dtb + "." + str(dtbos_found))
            cmd = fdtoverlay_bin + " -v -i "+ pre_kdtb +" "+ dtbo +" -o "+ post_kdtb
            bb.debug(1, "merge dtbo cmd: %s" % (cmd))
            try:
                subprocess.check_output(cmd, shell=True)
            except RuntimeError as e:
                bb.error("cmd: %s failed with error %s" % (cmd, str(e)))
        if dtbos_found == 0:
            bb.debug(1, "No dtbos to merge into %s" % dtb)

        # Copy latest overlayed dtb file with original name
        output = dtb + "." + str(dtbos_found)
        shutil.copy2(os.path.join(dtbo_dir, output), os.path.join(dtbo_dir, dtb))
}

DTBBIN_DEPLOYDIR = "${WORKDIR}/qcom_dtbbin_deploy-${PN}"
DTBBIN_SIZE ?= "4096"

do_qcom_dtbbin_deploy[prefuncs] += "qcom_dtbmerge"
do_qcom_dtbbin_deploy[vardeps] += "KERNEL_DTBO_PROVIDERS KERNEL_DTB_OVERLAYS"
do_qcom_dtbbin_deploy[depends] += "dtc-native:do_populate_sysroot ${@get_dtbo_providers(d)}"
do_qcom_dtbbin_deploy[depends] += "dosfstools-native:do_populate_sysroot mtools-native:do_populate_sysroot"
do_qcom_dtbbin_deploy[cleandirs] = "${DTBBIN_DEPLOYDIR}"
do_qcom_dtbbin_deploy() {
    for dtbf in ${KERNEL_DEVICETREE}; do
        bbdebug 1 " combining: $dtbf"
        dtb=`normalize_dtb "$dtbf"`
        dtb_ext=${dtb##*.}
        dtb_base_name=`basename $dtb .$dtb_ext`
        mkdir -p ${DTBBIN_DEPLOYDIR}/$dtb_base_name
        cp ${D}/${KERNEL_DTBDEST}/$dtb_base_name.dtb ${DTBBIN_DEPLOYDIR}/$dtb_base_name/combined-dtb.dtb
        mkfs.vfat -S ${QCOM_VFAT_SECTOR_SIZE} -C ${DTBBIN_DEPLOYDIR}/dtb-${dtb_base_name}-image.vfat ${DTBBIN_SIZE}
        mcopy -i "${DTBBIN_DEPLOYDIR}/dtb-${dtb_base_name}-image.vfat" -vsmpQ ${DTBBIN_DEPLOYDIR}/$dtb_base_name/* ::/
        rm -rf ${DTBBIN_DEPLOYDIR}/$dtb_base_name
    done
}
addtask qcom_dtbbin_deploy after do_populate_sysroot do_packagedata before do_deploy

# Setup sstate, see deploy.bbclass
SSTATETASKS += "do_qcom_dtbbin_deploy"
do_qcom_dtbbin_deploy[sstate-inputdirs] = "${DTBBIN_DEPLOYDIR}"
do_qcom_dtbbin_deploy[sstate-outputdirs] = "${DEPLOY_DIR_IMAGE}"

python do_qcom_dtbbin_deploy_setscene () {
    sstate_setscene(d)
}
addtask do_qcom_dtbbin_deploy_setscene

do_qcom_dtbbin_deploy[stamp-extra-info] = "${MACHINE_ARCH}"
