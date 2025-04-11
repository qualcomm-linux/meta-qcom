# Limit to qrb2210
COMPATIBLE_MACHINE = "(qcm2290)"

require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

DEPENDS += "bc-native dtc-native gnutls-native gzip-native python3-pyelftools-native skales-native xxd-native"

PV = "2025.03+2025.04-rc6"
SRCREV = "82b69fc4224432d5aefa7ca750d950374cbc7fb2"
SRC_URI:append:qcom = " \
           file://0001-clk-stub-generic-rpm-requests-compatible.patch \
           file://0002-arm-dts-Add-override-for-RB1.patch \
           file://0003-arm-mach-snapdragon-Enable-OF_UPSTREAM_BUILD_VENDOR.patch \
           file://0004-qcom_defconfig-Disable-MMC-HS200-mode-support.patch \
"

do_compile[depends] += "virtual/kernel:do_deploy"

uboot_compile_config:append:qcom() {
    cd ${B}/${config}
    rm -f u-boot-nodtb.bin.gz
    gzip -k u-boot-nodtb.bin
    touch empty-file

    # Iterate over all entries in KERNEL_DEVICETREE to generate Android
    # boot images
    for dtb in ${KERNEL_DEVICETREE} ; do
        UBOOT_DTB="$dtb"
	# KERNEL_DEVICETREE contains the relative path to the DTBs in
	# the kernel tree, but most, if not all MACHINEs in OE put the
	# DTB file straight into DEPLOY_DIR_IMAGE. Support both by
	# falling back to the bare file if it doesn't exist in a
	# subfolder.
        if ! [ -e ${DEPLOY_DIR_IMAGE}/$UBOOT_DTB ] ; then
            UBOOT_DTB="$(basename $dtb)"
        fi
        cat u-boot-nodtb.bin.gz ${DEPLOY_DIR_IMAGE}/$UBOOT_DTB > u-boot-nodtb.bin.gz-dtb
        ${STAGING_BINDIR_NATIVE}/skales/mkbootimg --base 0x80000000 --pagesize 4096 --kernel u-boot-nodtb.bin.gz-dtb  --cmdline "root=/dev/notreal" --ramdisk empty-file --output ${MACHINE}-u-boot-$(basename $dtb .dtb)-${PV}-boot.img
    done

}

uboot_deploy_config:append:qcom() {
    cd ${B}/${config} && install -m 0644 ${MACHINE}-u-boot-*-${PV}-boot.img ${DEPLOYDIR}
    UBOOT_DTB="${@os.path.basename(d.getVar('KERNEL_DEVICETREE').split()[0][:-4]) if d.getVar('KERNEL_DEVICETREE') else ''}"
    cd ${DEPLOYDIR} && ln -sf ${MACHINE}-u-boot-$(basename $UBOOT_DTB .dtb)-${PV}-boot.img boot-${MACHINE}.img && ln -sf  ${MACHINE}-u-boot-$(basename $UBOOT_DTB .dtb)-${PV}-boot.img boot.img
}
