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

do_install:compile:qcom() {
    # Strip whitespace from ${KERNEL_DEVICETREE}
    UBOOT_DTB=""
    for dtb in ${KERNEL_DEVICETREE} ; do
	UBOOT_DTB="$dtb"
    done

    if [ -n "${UBOOT_CONFIG}" ]
    then
        for config in ${UBOOT_MACHINE}; do
            i=$(expr $i + 1);
            for type in ${UBOOT_CONFIG}; do
                j=$(expr $j + 1);
                if [ $j -eq $i ]
                then
	            cd ${B}/${config}
                    rm -f u-boot-nodtb.bin.gz
	            gzip -k u-boot-nodtb.bin
	            cat u-boot-nodtb.bin.gz dts/upstream/src/arm64/$UBOOT_DTB > u-boot-nodtb.bin.gz-dtb
	            ${STAGING_BINDIR_NATIVE}/skales/mkbootimg --base 0x80000000 --pagesize 4096 --kernel u-boot-nodtb.bin.gz-dtb  --cmdline "root=/dev/notreal" --ramdisk u-boot.bin --output ${MACHINE}-u-boot-${PV}-boot.img
                fi
            done
            unset j
        done
        unset i
    fi
}

do_deploy:append:qcom() {
    if [ -n "${UBOOT_CONFIG}" ]
    then
        for config in ${UBOOT_MACHINE}; do
            i=$(expr $i + 1);
            for type in ${UBOOT_CONFIG}; do
                j=$(expr $j + 1);
                if [ $j -eq $i ]
                then
	            cd ${B}/${config} && install -m 0644 ${MACHINE}-u-boot-${PV}-boot.img ${DEPLOYDIR}
	            cd ${DEPLOYDIR} && ln -sf ${MACHINE}-u-boot-${PV}-boot.img boot-${MACHINE}.img && ln -sf ${MACHINE}-u-boot-${PV}-boot.img boot.img
                fi
            done
            unset j
        done
        unset i
    fi
}
