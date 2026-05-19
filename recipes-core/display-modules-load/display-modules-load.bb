
SUMMARY = "Display DRM modules preload and udev rules"
DESCRIPTION = "Installs DRM module load configs and udev rules"
LICENSE = "CLOSED"

SRC_URI = "file://00-drm-modules.conf \
           file://00-drm-modprobe.conf \
           file://01-display-modules.conf \
           file://41-drm.rules \
"

S = "${WORKDIR}"

do_install() {
        # Create required directories
        install -d ${D}${sysconfdir}/modules-load.d
        install -d ${D}${sysconfdir}/modprobe.d
        install -d ${D}${sysconfdir}/udev/rules.d

        # Install modules-load configs
        install -m 0644 ${WORKDIR}/00-drm-modules.conf ${D}${sysconfdir}/modules-load.d/00-drm.conf
        install -m 0644 ${WORKDIR}/01-display-modules.conf ${D}${sysconfdir}/modules-load.d/01-display.conf

        # Install modprobe config
        install -m 0644 ${WORKDIR}/00-drm-modprobe.conf ${D}${sysconfdir}/modprobe.d/00-drm.conf

        # Install udev rule
        install -m 0644 ${WORKDIR}/41-drm.rules ${D}${sysconfdir}/udev/rules.d/41-drm.rules
}

