SUMMARY = "Scripts to resize root filesystem at first boot"

LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

SRC_URI += " \
    file://resize-rootfs.service \
    file://resize-rootfs.sh \
"

S = "${UNPACKDIR}"

inherit features_check systemd
REQUIRED_DISTRO_FEATURES = "systemd"

do_install () {
    install -d ${D}${sbindir}
    install -m 0755 ${S}/resize-rootfs.sh ${D}${sbindir}

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/resize-rootfs.service ${D}${systemd_unitdir}/system
}

SYSTEMD_SERVICE:${PN} = "resize-rootfs.service"
RDEPENDS:${PN} += "e2fsprogs-resize2fs"
