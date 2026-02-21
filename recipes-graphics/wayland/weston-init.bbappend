FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

DEFAULTBACKEND:qcom ?= "drm"

SRC_URI += "file://additional-devices.conf \
            file://weston-start.sh \
           "

do_install:append:qcom() {
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        # Install systemd drop-in file
        install -d ${D}${systemd_system_unitdir}/weston.service.d
        install -m 0644 ${UNPACKDIR}/additional-devices.conf ${D}${systemd_system_unitdir}/weston.service.d/additional-devices.conf
        sed -i -e s:@sysconfdir@:${sysconfdir}:g \
                ${D}${systemd_system_unitdir}/weston.service.d/additional-devices.conf

        # Install weston script
        install -d ${D}${sysconfdir}/xdg/weston
        install -m 0755 ${UNPACKDIR}/weston-start.sh ${D}${sysconfdir}/xdg/weston/weston-start.sh
        sed -i -e s:@bindir@:${bindir}:g \
                ${D}${sysconfdir}/xdg/weston/weston-start.sh
    fi
}

FILES:${PN} += "${systemd_system_unitdir}/weston.service.d/additional-devices.conf \
                ${sysconfdir}/xdg/weston/weston-start.sh \
               "
