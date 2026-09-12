SUMMARY = "U-Boot boot script for FIT image based boot on Qualcomm targets"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS = "u-boot-mkimage-native"

SRC_URI = "file://boot.cmd.in"

INHIBIT_DEFAULT_DEPS = "1"

inherit deploy nopackages

PACKAGE_ARCH = "${MACHINE_ARCH}"

UBOOT_ARCH = "${@oe.kernel.map_uboot_arch(d)}"

S = "${UNPACKDIR}"

KERNEL_CMDLINE_EXTRA ?= ""
QCOM_FIT_KERNEL_CMDLINE = "root=${QCOM_BOOTIMG_ROOTFS} rw rootwait console=${KERNEL_CONSOLE} ${KERNEL_CMDLINE_EXTRA}"

# Configurations bootm selects, as "#<config>[#<overlay-config>...]". Empty boots
# the default configuration of the FIT.
QCOM_FIT_BOOT_CONF ?= ""

do_compile() {
    sed -e "s|@KERNEL_CMDLINE@|${QCOM_FIT_KERNEL_CMDLINE}|g" \
        -e "s|@FIT_CONF@|${QCOM_FIT_BOOT_CONF}|g" boot.cmd.in > boot.cmd
    mkimage -A ${UBOOT_ARCH} -T script -C none -n "Boot script" -d boot.cmd boot.scr
}
do_install[noexec] = "1"

do_deploy() {
    install -d ${DEPLOYDIR}
    install -m 0644 boot.scr ${DEPLOYDIR}
}

addtask do_deploy after do_compile before do_build
