# Extra firmware files, which are covered by the separate license.
# They are not a part of linux-firmware and will never be:
# https://lists.infradead.org/pipermail/ath6kl/2017-July/000296.html

SUMMARY = "Additional firmware files for Qualcomm/Atheros Ath6k SoC"

LICENSE = "Firmware-qualcommAthos_ath6kl"
LIC_FILES_CHKSUM = "file://LICENSE.qca_firmware;md5=2a397c0e988f4c52d3d526133b617c8d"
NO_GENERIC_LICENSE[Firmware-qualcommAthos_ath6kl] = "LICENSE.qca_firmware"

SRC_URI = "git://github.com/qca/ath6kl-firmware;protocol=https;branch=master"
SRCREV = "2e02576c1dab6fd35118eea1004f50aaaed3794f"

PV = "3.5.0.349-1+git"

inherit allarch

CLEANBROKEN = "1"

do_compile() {
	:
}

FWDIR = "${nonarch_base_libdir}/firmware"

do_install() {
    install -d ${D}${FWDIR}/ath6k/AR6004/hw1.3
    install -d ${D}${FWDIR}/ath6k/AR6004/hw3.0

    install -m 0644 ath6k/AR6004/hw1.3/* ${D}${FWDIR}/ath6k/AR6004/hw1.3
    install -m 0644 ath6k/AR6004/hw3.0/* ${D}${FWDIR}/ath6k/AR6004/hw3.0

    install -m 0644 LICENSE.qca_firmware ${D}${FWDIR}
}

FILES:${PN} += "${FWDIR}"

# There is a conflict between linux-firmware version and and the updated one

inherit update-alternatives
ALTERNATIVE:${PN} = "ar6004-hw13-bdata"
ALTERNATIVE_LINK_NAME[ar6004-hw13-bdata] = "${nonarch_base_libdir}/firmware/ath6k/AR6004/hw1.3/bdata.bin"
ALTERNATIVE_PRIORITY = "100"

# Firmware files are generally not ran on the CPU, so they can be
# allarch despite being architecture specific
INSANE_SKIP = "arch"
