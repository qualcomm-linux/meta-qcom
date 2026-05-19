FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

DEFAULTBACKEND:qcom ?= "drm"

SRC_URI:append:qcom = " \
    file://additional-devices.conf \
    file://weston-start.sh \
    file://0001-weston-init-Enable-early-Weston-startup-using-DRM-de.patch \
"

do_install:append:qcom() {
    install -d ${D}${systemd_system_unitdir}/weston.service.d
    install -m 0644 ${UNPACKDIR}/additional-devices.conf \
        ${D}${systemd_system_unitdir}/weston.service.d/additional-devices.conf
    sed -i -e 's:@bindir@:${bindir}:g' \
        ${D}${systemd_system_unitdir}/weston.service.d/additional-devices.conf

    install -d ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/weston-start.sh \
        ${D}${bindir}/weston-start.sh
    sed -i -e 's:@bindir@:${bindir}:g' \
        ${D}${bindir}/weston-start.sh
}

FILES:${PN} += "${systemd_system_unitdir}/weston.service.d/additional-devices.conf"
FILES:${PN} += "${bindir}/weston-start.sh"
