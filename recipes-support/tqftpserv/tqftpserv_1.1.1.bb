SUMMARY = "Qualcomm tqftpserv application"
HOMEPAGE = "https://github.com/linux-msm/tqftpserv.git"
SECTION = "devel"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=424e013ed97b36284f3b9ce27bb16a56"

DEPENDS = "qrtr zstd"

inherit systemd meson pkgconfig

SRCREV = "443c82aadae2862dc7c12af48ac0b900f4bb0fe7"
SRC_URI = "git://github.com/linux-msm/${BPN}.git;branch=master;protocol=https;tag=v${PV} \
"

EXTRA_OEMESON = "-Dsystemd-unit-prefix=${systemd_system_unitdir}"

SYSTEMD_SERVICE:${PN} = "tqftpserv.service"
RDEPENDS:${PN} += "qrtr"
RPROVIDES:${PN} += "virtual-tftp-server"
RCONFLICTS:${PN} += "tftp-server"
