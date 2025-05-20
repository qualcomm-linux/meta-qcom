# This is a bbappend to add support for generating Android style boot images for chainloading u-boot from ABL

DEPENDS:append:qcom = " skales-native xxd-native"

FILESEXTRAPATHS:prepend:qcom := "${THISDIR}/files:"
SRC_URI:append:qcom = " \
           file://0001-clk-stub-generic-rpm-requests-compatible.patch \
           file://0002-arm-dts-Add-override-for-RB1.patch \
           file://0003-arm-mach-snapdragon-Enable-OF_UPSTREAM_BUILD_VENDOR.patch \
           file://0004-qcom_defconfig-Disable-MMC-HS200-mode-support.patch \
"

# Don't add extra dependencies for non-qcom machines and layers
COMPILE_EXTRA_DEPENDS = ""
COMPILE_EXTRA_DEPENDS:qcom = "virtual/kernel:do_deploy"
do_compile[depends] += "${COMPILE_EXTRA_DEPENDS}"

uboot_compile_config:append:qcom() {
    cd ${B}/${config}
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
