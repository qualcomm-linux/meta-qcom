FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

DEFAULTBACKEND:qcom ?= "drm"

SRC_URI:append:qcom = " \
    file://additional-devices.conf \
    file://weston-start.sh \
    file://weston-qdemo-launcher.ini \
"

do_compile:append:qcom() {
    sed -i -e 's:@bindir@:${bindir}:g' ${WORKDIR}/sources/additional-devices.conf
    sed -i -e 's:@bindir@:${bindir}:g' ${WORKDIR}/sources/weston-start.sh
}

do_install:append:qcom() {
    install -d ${D}${systemd_system_unitdir}/weston.service.d
    install -m 0644 ${WORKDIR}/sources/additional-devices.conf \
        ${D}${systemd_system_unitdir}/weston.service.d/additional-devices.conf

    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/sources/weston-start.sh \
        ${D}${bindir}/weston-start.sh
   
    install -d ${D}${sysconfdir}/xdg/weston
    echo "" >> ${D}${sysconfdir}/xdg/weston/weston.ini
    cat ${WORKDIR}/sources/weston-qdemo-launcher.ini >> ${D}${sysconfdir}/xdg/weston/weston.ini
}

FILES:${PN} += "${systemd_system_unitdir}/weston.service.d/additional-devices.conf"
FILES:${PN} += "${bindir}/weston-start.sh"
