SUMMARY = "Systemd unit to mount persist parition at /var/lib/tee"
DESCRIPTION  = "Mount persist partition at /var/lib/tee to store \
encryped data and support security functions"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

SRC_URI = "file://var-lib-tee.mount"

inherit allarch features_check systemd
REQUIRED_DISTRO_FEATURES = "systemd"

INHIBIT_DEFAULT_DEPS = "1"

S = "${UNPACKDIR}"

do_compile[noexec] = "1"

do_install() {
    install -Dm 0644 ${UNPACKDIR}/var-lib-tee.mount \
            ${D}${systemd_unitdir}/system/var-lib-tee.mount
}

FILES:${PN} += " ${systemd_unitdir}/*"

PACKAGES = "${PN}"

FILES:${PN} += "${systemd_unitdir}"
