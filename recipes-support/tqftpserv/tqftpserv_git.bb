SUMMARY = "Qualcomm tqftpserv application"
HOMEPAGE = "https://github.com/linux-msm/tqftpserv.git"
SECTION = "devel"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=424e013ed97b36284f3b9ce27bb16a56"

DEPENDS = "qrtr zstd"

inherit systemd meson pkgconfig

SRCREV = "0ed681362b6f7ac7381e0320501823be6d843006"
SRC_URI = "git://github.com/linux-msm/${BPN}.git;branch=master;protocol=https \
"

PV = "0.0+"

EXTRA_OEMESON = "-Dsystemd-unit-prefix=${systemd_system_unitdir}"

SYSTEMD_SERVICE:${PN} = "tqftpserv.service"
RDEPENDS:${PN} += "qrtr"
