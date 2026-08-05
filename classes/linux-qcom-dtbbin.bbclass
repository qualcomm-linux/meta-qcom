#
# Copyright (c) 2024 Qualcomm Innovation Center, Inc. All rights reserved.
#
# SPDX-License-Identifier: BSD-3-Clause-Clear
#

inherit_defer ${@bb.utils.contains('QCOM_DTB_DEFAULT', 'multi-dtb', 'dtb-fit-image', '', d)}

DTBBIN_DEPLOYDIR = "${WORKDIR}/qcom_dtbbin_deploy-${PN}"
DTBBIN_SIZE ?= "4096"

do_qcom_dtbbin_deploy[depends] += "dosfstools-native:do_populate_sysroot mtools-native:do_populate_sysroot"
do_qcom_dtbbin_deploy[cleandirs] = "${DTBBIN_DEPLOYDIR}"
do_qcom_dtbbin_deploy() {
    # Source the DTBs from what do_deploy published: unlike ${D}, which
    # only exists when do_install ran in this build, DEPLOY_DIR_IMAGE is
    # also populated when do_deploy is restored from sstate.
    deployDir="${DEPLOY_DIR_IMAGE}"
    if [ -n "${KERNEL_DEPLOYSUBDIR}" ]; then
        deployDir="${DEPLOY_DIR_IMAGE}/${KERNEL_DEPLOYSUBDIR}"
    fi

    for dtbf in ${KERNEL_DEVICETREE}; do
        bbdebug 1 " combining: $dtbf"
        dtb=`normalize_dtb "$dtbf"`
        dtb_ext=${dtb##*.}
        # Skip DTBOs
        [ "$dtb_ext" = "dtbo" ] && continue
        dtb_base_name=`basename $dtb .$dtb_ext`
        mkdir -p ${DTBBIN_DEPLOYDIR}/$dtb_base_name
        cp $deployDir/$dtb_base_name.dtb ${DTBBIN_DEPLOYDIR}/$dtb_base_name/combined-dtb.dtb
        mkfs.vfat -S ${QCOM_VFAT_SECTOR_SIZE} -C ${DTBBIN_DEPLOYDIR}/dtb-${dtb_base_name}-image.vfat ${DTBBIN_SIZE}
        mcopy -i "${DTBBIN_DEPLOYDIR}/dtb-${dtb_base_name}-image.vfat" -vsmpQ ${DTBBIN_DEPLOYDIR}/$dtb_base_name/* ::/
        rm -rf ${DTBBIN_DEPLOYDIR}/$dtb_base_name
    done

    if ${@bb.utils.contains('QCOM_DTB_DEFAULT', 'multi-dtb', 'true', 'false', d)}; then
        # Generate an image with qclinuxfitImage (multi-dtb image) alongside individual DTB images.
        mkfs.vfat -S ${QCOM_VFAT_SECTOR_SIZE} -C ${DTBBIN_DEPLOYDIR}/dtb-multi-dtb-image.vfat ${DTBBIN_SIZE}
        mcopy -i "${DTBBIN_DEPLOYDIR}/dtb-multi-dtb-image.vfat" -vsmpQ ${DEPLOY_DIR_IMAGE}/qclinuxfitImage ::/qclinux_fit.img
    fi
}
addtask qcom_dtbbin_deploy after do_deploy before do_build

# Setup sstate, see deploy.bbclass
SSTATETASKS += "do_qcom_dtbbin_deploy"
do_qcom_dtbbin_deploy[sstate-inputdirs] = "${DTBBIN_DEPLOYDIR}"
do_qcom_dtbbin_deploy[sstate-outputdirs] = "${DEPLOY_DIR_IMAGE}"

python do_qcom_dtbbin_deploy_setscene () {
    sstate_setscene(d)
}
addtask do_qcom_dtbbin_deploy_setscene

do_qcom_dtbbin_deploy[stamp-extra-info] = "${MACHINE_ARCH}"
