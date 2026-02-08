require camera-service.inc

inherit systemd

DEPENDS += "camera-common"

SRC_URI += "file://cam-server.service"

# Build camera server binary
EXTRA_OECMAKE += "-DBUILD_CATEGORY=SERVER"

SYSTEMD_SERVICE:${PN} = "cam-server.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

do_install:append () {
    # Install service file
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${UNPACKDIR}/cam-server.service ${D}${systemd_system_unitdir}
}
