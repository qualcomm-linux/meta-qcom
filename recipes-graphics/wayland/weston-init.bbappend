FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

DEFAULTBACKEND:qcom ?= "drm"

SRC_URI:append:qcom = " \
    file://additional-devices.conf \
    file://weston-start.sh \
"

do_compile:append:qcom() {
    sed -i -e 's:@bindir@:${bindir}:g' ${UNPACKDIR}/additional-devices.conf
    sed -i -e 's:@bindir@:${bindir}:g' ${UNPACKDIR}/weston-start.sh
    sed -i -e "/^\[core\]/a repaint-window=10" ${UNPACKDIR}/weston.ini
}

do_install:append:qcom() {
    install -d ${D}${systemd_system_unitdir}/weston.service.d
    install -m 0644 ${UNPACKDIR}/additional-devices.conf \
        ${D}${systemd_system_unitdir}/weston.service.d/additional-devices.conf

    install -d ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/weston-start.sh \
        ${D}${bindir}/weston-start.sh
}

FILES:${PN} += "${systemd_system_unitdir}/weston.service.d/additional-devices.conf"
FILES:${PN} += "${bindir}/weston-start.sh"
