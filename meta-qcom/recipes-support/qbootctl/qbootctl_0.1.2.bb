SUMMARY = "A port of the Qualcomm Android bootctrl HAL for musl/glibc userspace"
HOMEPAGE = "https://gitlab.com/sdm845-mainline/qbootctl"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

SRCREV = "77b48f092a3690d587e2d3b1e30cf8bc2abf87e7"
SRC_URI = "git://gitlab.com/sdm845-mainline/qbootctl.git;protocol=https;branch=main \
           file://qbootclt-bless-boot.service.in \
           file://0001-Fix-to-uint32_t-has-not-been-declared.patch \
           "

DEPENDS = "zlib"

S = "${WORKDIR}/git"

PV = "0.1.2"

inherit meson systemd

do_install:append () {
	install -d ${D}${systemd_system_unitdir}
	sed 's:@bindir@:${bindir}:' < ${UNPACKDIR}/qbootclt-bless-boot.service.in > ${D}${systemd_system_unitdir}/qbootclt-bless-boot.service
}

SYSTEMD_SERVICE:${PN} = "qbootclt-bless-boot.service"
