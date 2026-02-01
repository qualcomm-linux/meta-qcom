# Copyright (c) 2023-2024 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear

inherit image_types

IMAGE_TYPES += "qcomflash"

QCOM_BOOT_FIRMWARE ?= ""

QCOM_ESP_IMAGE ?= "${@bb.utils.contains("MACHINE_FEATURES", "efi", "esp-qcom-image", "", d)}"
QCOM_ESP_FILE ?= "${@'${DEPLOY_DIR_IMAGE}/${QCOM_ESP_IMAGE}-${MACHINE}${IMAGE_NAME_SUFFIX}.vfat' if d.getVar('QCOM_ESP_IMAGE') else ''}"

# When not specified, assume the board supports FIT-based
# multi-DTB and mention this as the default DTB to be flashed.
QCOM_DTB_DEFAULT ?= "multi-dtb"
QCOM_DTB_FILE ?= "dtb.bin"

QCOM_BOOT_FILES_SUBDIR ?= ""
QCOM_PARTITION_FILES_SUBDIR ??= "${QCOM_BOOT_FILES_SUBDIR}"
QCOM_PARTITION_FILES_SUBDIR_SPINOR ??= ""

QCOM_PARTITION_CONF ?= "qcom-partition-conf"

IMAGE_QCOMFLASH_FS_TYPE ??= "ext4"

QCOMFLASH_DIR = "${IMGDEPLOYDIR}/${IMAGE_NAME}.qcomflash"
IMAGE_CMD:qcomflash = "create_qcomflash_pkg"
do_image_qcomflash[dirs] = "${QCOMFLASH_DIR}"
do_image_qcomflash[cleandirs] = "${QCOMFLASH_DIR}"
do_image_qcomflash[depends] += "${@ ['', '${QCOM_PARTITION_CONF}:do_deploy'][d.getVar('QCOM_PARTITION_CONF') != '']} \
                                ${@ ['', '${QCOM_BOOT_FIRMWARE}:do_deploy'][d.getVar('QCOM_BOOT_FIRMWARE') != '']} \
                                virtual/kernel:do_deploy \
				${@'virtual/bootloader:do_deploy' if d.getVar('PREFERRED_PROVIDER_virtual/bootloader') else  ''} \
				${@'${QCOM_ESP_IMAGE}:do_image_complete' if d.getVar('QCOM_ESP_IMAGE') != '' else  ''} \
				${@'abl2esp:do_deploy' if d.getVar('ABL_SIGNATURE_VERSION') else  ''}"
IMAGE_TYPEDEP:qcomflash += "${IMAGE_QCOMFLASH_FS_TYPE}"

deploy_partition_files() {
    for pbin in $1/gpt_main*.bin $1/gpt_backup*.bin \
                $1/gpt_both*.bin $1/zeros_*.bin \
                $1/rawprogram[0-9].xml $1/patch*.xml ; do
        install -m 0644 ${pbin} $2
    done

    if [ -e "$1/contents.xml" ]; then
        install -m 0644 "$1/contents.xml" $2/contents.xml
    fi
}

create_qcomflash_pkg() {
    # esp image
    [ -n "${QCOM_ESP_FILE}" ] && install -m 0644 ${QCOM_ESP_FILE} efi.bin

    # dtb image
    if [ -n "${QCOM_DTB_DEFAULT}" ] && \
                [ -f "${DEPLOY_DIR_IMAGE}/dtb-${QCOM_DTB_DEFAULT}-image.vfat" ]; then
        # default image
        install -m 0644 ${DEPLOY_DIR_IMAGE}/dtb-${QCOM_DTB_DEFAULT}-image.vfat ${QCOM_DTB_FILE}
        # copy all images so they can be made available via the same tarball
        for dtbimg in ${DEPLOY_DIR_IMAGE}/dtb-*-image.vfat; do
            install -m 0644 ${dtbimg} .
        done
    fi

    # vmlinux
    [ -e "${DEPLOY_DIR_IMAGE}/vmlinux" -a \
        ! -e "vmlinux" ] && \
        install -m 0644 "${DEPLOY_DIR_IMAGE}/vmlinux" vmlinux

    # Legacy boot images
    if [ -n "${QCOM_DTB_DEFAULT}" ]; then
        [ -e "${DEPLOY_DIR_IMAGE}/boot-initramfs-${QCOM_DTB_DEFAULT}-${MACHINE}.img" -a \
            ! -e "boot.img" ] && \
            install -m 0644 "${DEPLOY_DIR_IMAGE}/boot-initramfs-${QCOM_DTB_DEFAULT}-${MACHINE}.img" boot.img
        [ -e "${DEPLOY_DIR_IMAGE}/boot-${QCOM_DTB_DEFAULT}-${MACHINE}.img" -a \
            ! -e "boot.img" ] && \
            install -m 0644 "${DEPLOY_DIR_IMAGE}/boot-${QCOM_DTB_DEFAULT}-${MACHINE}.img" boot.img
    fi
    [ -e "${DEPLOY_DIR_IMAGE}/boot-${MACHINE}.img" -a \
        ! -e "boot.img" ] && \
        install -m 0644 "${DEPLOY_DIR_IMAGE}/boot-${MACHINE}.img" boot.img

    # rootfs image
    install -m 0644 ${IMGDEPLOYDIR}/${IMAGE_LINK_NAME}.${IMAGE_QCOMFLASH_FS_TYPE} rootfs.img

    # partition bins/xml files
    if [ -n "${QCOM_PARTITION_FILES_SUBDIR}" ]; then
        deploy_partition_files ${DEPLOY_DIR_IMAGE}/${QCOM_PARTITION_FILES_SUBDIR} .
    fi

    if [ -n "${QCOM_BOOT_FILES_SUBDIR}" ]; then
        # install CDT file if present,for targets with spinor, CDT file
        # will be in spinor subfolder instead of root folder
        if [ -n "${QCOM_CDT_FILE}" ] && [ -e "${DEPLOY_DIR_IMAGE}/${QCOM_BOOT_FILES_SUBDIR}/${QCOM_CDT_FILE}.bin" ]; then
            install -m 0644 ${DEPLOY_DIR_IMAGE}/${QCOM_BOOT_FILES_SUBDIR}/${QCOM_CDT_FILE}.bin cdt.bin
        fi

        # boot firmware
        for bfw in `find ${DEPLOY_DIR_IMAGE}/${QCOM_BOOT_FILES_SUBDIR} -maxdepth 1 -type f \
                \( -name '*.elf' ! -name 'abl2esp*.elf' ! -name 'xbl_config*.elf' \) -o \
                -name '*.mbn*' -o \
                -name '*.fv' -o \
                -name 'cdt_*.bin' -o \
                -name 'logfs_*.bin' -o \
                -name 'sec.dat'` ; do
            install -m 0644 ${bfw} .
        done

        # xbl_config
        xbl_config="xbl_config.elf"
        if ${@bb.utils.contains('DISTRO_FEATURES', 'kvm', 'true', 'false', d)}; then
            xbl_config="xbl_config_kvm.elf"
        fi

        if [ -f "${DEPLOY_DIR_IMAGE}/${QCOM_BOOT_FILES_SUBDIR}/${xbl_config}" ]; then
            install -m 0644 "${DEPLOY_DIR_IMAGE}/${QCOM_BOOT_FILES_SUBDIR}/${xbl_config}" xbl_config.elf
        fi

        # sail nor firmware
        if [ -d "${DEPLOY_DIR_IMAGE}/${QCOM_BOOT_FILES_SUBDIR}/sail_nor" ]; then
            install -d sail_nor
            find "${DEPLOY_DIR_IMAGE}/${QCOM_BOOT_FILES_SUBDIR}/sail_nor" -maxdepth 1 -type f -exec install -m 0644 {} sail_nor \;
        fi

        # SPI-NOR firmware, partition bins, CDT etc.
        if [ -d "${DEPLOY_DIR_IMAGE}/${QCOM_BOOT_FILES_SUBDIR}/spinor" ]; then
            install -d spinor
            find "${DEPLOY_DIR_IMAGE}/${QCOM_BOOT_FILES_SUBDIR}/spinor" -maxdepth 1 -type f -exec install -m 0644 {} spinor \;

            # partition bins/xml files
            if [ -n "${QCOM_PARTITION_FILES_SUBDIR_SPINOR}" ]; then
                deploy_partition_files ${DEPLOY_DIR_IMAGE}/${QCOM_PARTITION_FILES_SUBDIR_SPINOR} spinor
            fi

            # cdt file
            if [ -n "${QCOM_CDT_FILE}" ]; then
                install -m 0644 ${DEPLOY_DIR_IMAGE}/${QCOM_BOOT_FILES_SUBDIR}/spinor/${QCOM_CDT_FILE}.bin spinor/cdt.bin
            fi

            # dtb image
            if [ -n "${QCOM_DTB_FILE}" ]; then
                install -m 0644 ${DEPLOY_DIR_IMAGE}/dtb-${QCOM_DTB_DEFAULT}-image.vfat spinor/${QCOM_DTB_FILE}
            fi

            # copy programer to support flash of HLOS images
            find "${DEPLOY_DIR_IMAGE}/${QCOM_BOOT_FILES_SUBDIR}/spinor" -maxdepth 1 -type f -name 'xbl_s_devprg_ns.melf' -exec install -m 0644 {} . \;
        fi
    fi

    # abl2esp
    if [ -e "${DEPLOY_DIR_IMAGE}/abl2esp-${ABL_SIGNATURE_VERSION}.elf" ]; then
        install -m 0644 "${DEPLOY_DIR_IMAGE}/abl2esp-${ABL_SIGNATURE_VERSION}.elf" .
    fi

    # Create symlink to ${QCOMFLASH_DIR} dir
    ln -rsf ${QCOMFLASH_DIR} ${IMGDEPLOYDIR}/${IMAGE_LINK_NAME}.qcomflash

    # Create qcomflash tarball
    ${IMAGE_CMD_TAR} --sparse --numeric-owner --transform="s,^\./,${IMAGE_BASENAME}-${MACHINE}/," -cf- . | gzip -f -9 -n -c --rsyncable > ${IMGDEPLOYDIR}/${IMAGE_NAME}.qcomflash.tar.gz
    ln -sf ${IMAGE_NAME}.qcomflash.tar.gz ${IMGDEPLOYDIR}/${IMAGE_LINK_NAME}.qcomflash.tar.gz
}

create_qcomflash_pkg[vardepsexclude] += "DATETIME"
