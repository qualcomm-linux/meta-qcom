SUMMARY = "initramfs-framework module for copying kernel modules from initramfs to rootfs"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
RDEPENDS:${PN} = "initramfs-framework-base ${VIRTUAL-RUNTIME_base-utils}"

SRC_URI = "file://copy-modules.sh"

S = "${UNPACKDIR}"

do_install() {
    install -d ${D}/init.d
    install -m 0755 ${S}/copy-modules.sh ${D}/init.d/95-copy_modules
}

FILES:${PN} = "/init.d/"
