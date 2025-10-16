# This is a bbappend to add support for generating Android style boot images for chainloading u-boot from ABL

DEPENDS:append:qcom = " skales-native xxd-native"

# Don't add extra dependencies for non-qcom machines and layers
COMPILE_EXTRA_DEPENDS = ""
COMPILE_EXTRA_DEPENDS:qcom = "virtual/kernel:do_deploy"
do_compile[depends] += "${COMPILE_EXTRA_DEPENDS}"

uboot_compile_config:append:qcom() {
    cd ${B}/${builddir}
    touch empty-file
    rm -f u-boot-nodtb.bin.gz
    gzip -k u-boot-nodtb.bin
    cat u-boot-nodtb.bin.gz ${DEPLOY_DIR_IMAGE}/${type}.dtb > u-boot-nodtb.bin.gz-${type}
    ${STAGING_BINDIR_NATIVE}/skales/mkbootimg --base 0x80000000 --pagesize 4096 --kernel u-boot-nodtb.bin.gz-${type} --cmdline "root=/dev/notreal" --ramdisk empty-file --output u-boot-${type}.bin
}

# Symlink the 'main' u-boot.bin to boot.img so the qcom image bbclass pick it up
uboot_deploy_config:append:qcom() {
    cd ${DEPLOYDIR} && ln -sf u-boot-${type}-${PV}-${PR}.bin boot-${MACHINE}.img
}
