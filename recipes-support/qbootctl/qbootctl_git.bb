SUMMARY = "A port of the Qualcomm Android bootctrl HAL for musl/glibc userspace"
HOMEPAGE = "https://gitlab.com/sdm845-mainline/qbootctl"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7475d4a045b602c247a1b641ad13d139"

SRCREV = "39a6e6daaf029fb0a083777679a15ea2c18f72de"
SRC_URI = "git://github.com/linux-msm/qbootctl.git;protocol=https;branch=main \
           file://qbootctl-bless-boot.service.in \
           "

PV = "0.2.2"

inherit meson systemd

do_install:append () {
	install -d ${D}${systemd_system_unitdir}
	sed 's:@bindir@:${bindir}:' < ${UNPACKDIR}/qbootctl-bless-boot.service.in > ${D}${systemd_system_unitdir}/qbootctl-bless-boot.service
}

SYSTEMD_SERVICE:${PN} = "qbootctl-bless-boot.service"
