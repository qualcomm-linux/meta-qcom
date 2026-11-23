DESCRIPTION = "QCOM Iris Video Driver"
LICENSE = "GPL-2.0-only"

LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e"

SRC_URI = " \
    git://github.com/qualcomm-linux/video-driver.git;protocol=https;branch=video.qclinux.main;tag=v${PV} \
    file://blacklist-video.conf.venus \
    file://blacklist-video.conf.vidc \
"
SRCREV  = "1875fc182ba4c69f64705af6794d837fcee968c7"

inherit module update-alternatives

MAKE_TARGETS = "modules"

# This package is designed to run exclusively on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

do_install:append() {
    install -d ${D}${sysconfdir}/modprobe.d
    install -Dm 0644 ${UNPACKDIR}/blacklist-video.conf.venus \
            ${D}${sysconfdir}/modprobe.d/blacklist-video.conf.venus
    install -Dm 0644 ${UNPACKDIR}/blacklist-video.conf.vidc \
            ${D}${sysconfdir}/modprobe.d/blacklist-video.conf.vidc
}

PACKAGES += "${PN}-bl-venus ${PN}-bl-vidc"
RDEPENDS:${PN} += "${PN}-bl-venus ${PN}-bl-vidc"

FILES:${PN}-bl-venus += "${sysconfdir}/modprobe.d/blacklist-video.conf.venus"
FILES:${PN}-bl-vidc += "${sysconfdir}/modprobe.d/blacklist-video.conf.vidc"

ALTERNATIVE:${PN}-bl-vidc  = "blacklist-video"
ALTERNATIVE_TARGET_${PN}-bl-vidc = "${sysconfdir}/modprobe.d/blacklist-video.conf.vidc"
# By default, use the in-kernel qcom_iris driver.
ALTERNATIVE_PRIORITY_${PN}-bl-vidc = "150"

# On kodiak (qcm6490), lemans (qcs9100), monaco (qcs8300) and glymur, keep
# the out-of-tree iris_vidc driver: let the Venus blacklist (priority 100) win.
ALTERNATIVE_PRIORITY_${PN}-bl-vidc:qcm6490 = "50"
ALTERNATIVE_PRIORITY_${PN}-bl-vidc:qcs9100 = "50"
ALTERNATIVE_PRIORITY_${PN}-bl-vidc:qcs8300 = "50"
ALTERNATIVE_PRIORITY_${PN}-bl-vidc:glymur = "50"

ALTERNATIVE:${PN}-bl-venus = "blacklist-video"
ALTERNATIVE_TARGET_${PN}-bl-venus = "${sysconfdir}/modprobe.d/blacklist-video.conf.venus"
ALTERNATIVE_PRIORITY_${PN}-bl-venus = "100"

ALTERNATIVE_LINK_NAME[blacklist-video] = "${sysconfdir}/modprobe.d/blacklist-video.conf"
