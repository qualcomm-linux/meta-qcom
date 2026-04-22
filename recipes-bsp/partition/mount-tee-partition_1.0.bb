SUMMARY = "Systemd unit to mount persist parition at /var/lib/tee"
DESCRIPTION  = "Mount persist partition at /var/lib/tee to store \
encryped data and support security functions"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

SRC_URI = " \
    file://var-lib-tee.mount \
    file://check-tee-partition-fs.sh \
    file://format-tee-partition.service \
    file://persist.rules \
    file://var-usbfw.mount \
    file://xhci-bind.rules \
"

inherit features_check systemd
REQUIRED_DISTRO_FEATURES = "systemd"

INHIBIT_DEFAULT_DEPS = "1"

S = "${UNPACKDIR}"

do_compile[noexec] = "1"

do_install() {
    install -Dm 0644 ${UNPACKDIR}/var-lib-tee.mount \
            ${D}${systemd_system_unitdir}/var-lib-tee.mount
    install -Dm 0755 ${UNPACKDIR}/check-tee-partition-fs.sh \
            ${D}${sbindir}/check-tee-partition-fs.sh
    install -Dm 0644 ${UNPACKDIR}/format-tee-partition.service \
            ${D}${systemd_system_unitdir}/format-tee-partition.service
    sed -i -e "s,@sbindir@,${sbindir},g" \
            ${D}${systemd_system_unitdir}/format-tee-partition.service
    install -Dm 0644 ${UNPACKDIR}/persist.rules \
            ${D}${nonarch_base_libdir}/udev/rules.d/99-persist.rules
}

do_install:append:qcm6490 () {
    install -d ${D}${systemd_unitdir}/system/local-fs.target.wants
    install -Dm 0644 ${UNPACKDIR}/var-usbfw.mount ${D}${systemd_system_unitdir}/system/var-usbfw.mount
    install -Dm 0644 ${UNPACKDIR}/xhci-bind.rules ${D}${nonarch_libdir}/udev/rules.d/99-xhci-bind.rules
    install -d ${D}${nonarch_base_libdir}/firmware/
    ln -sf ${systemd_unitdir}/system/var-usbfw.mount ${D}${systemd_unitdir}/system/local-fs.target.wants/var-usbfw.mount
    ln -sf /var/usbfw/renesas_usb_fw.mem ${D}${nonarch_base_libdir}/firmware/renesas_usb_fw.mem
}

FILES:${PN}:append:qcm6490 = " ${systemd_unitdir}/system/* \
                               ${nonarch_base_libdir}/firmware/*"

PACKAGES = "${PN}"

SYSTEMD_SERVICE:${PN} = "var-lib-tee.mount format-tee-partition.service"

RDEPENDS:${PN} += "e2fsprogs-e2fsck e2fsprogs-mke2fs util-linux-blkid"
