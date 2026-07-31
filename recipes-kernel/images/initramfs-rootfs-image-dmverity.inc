# Extend Qualcomm initramfs-rootfs-image with dm-verity runtime setup bits.
# Included conditionally from initramfs-rootfs-image.bbappend when

# Ensure dm-verity userspace/runtime pieces are available in initramfs.
PACKAGE_INSTALL += " \
    cryptsetup \
    initramfs-module-dmverity \
    lvm2 \
    udev \
    util-linux-mount \
"

# Keep default staging path aligned with dm-verity-img.bbclass.
STAGING_VERITY_DIR ?= "${TMPDIR}/work-shared/${MACHINE}/dm-verity"

python __anonymous() {
    verity_image = d.getVar('DM_VERITY_IMAGE')
    verity_type = d.getVar('DM_VERITY_IMAGE_TYPE')

    if verity_image and verity_type:
        dep = ' %s:do_image_%s' % (verity_image, verity_type.replace('-', '_'))
        d.appendVarFlag('do_image', 'depends', dep)
}

# Ensure dm-verity.env gets refreshed if protected image is rebuilt.
do_image[nostamp] = "1"

deploy_verity_hash() {
    install -D -m 0644 \
        ${STAGING_VERITY_DIR}/${DM_VERITY_IMAGE}.${DM_VERITY_IMAGE_TYPE}.verity.env \
        ${IMAGE_ROOTFS}${datadir}/misc/dm-verity.env
}
IMAGE_PREPROCESS_COMMAND += " deploy_verity_hash"
