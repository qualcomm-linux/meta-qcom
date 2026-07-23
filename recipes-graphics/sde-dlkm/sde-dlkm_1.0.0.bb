DESCRIPTION = "Qualcomm SDE (Snapdragon Display Engine) display driver"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=223037c4be0bfc6cf757035432adf983"

inherit module

PV = "1.0.0"
SRCREV = "70ef6d41350020df80c406fcc30db4d63e9d776b"
SRC_URI = " \
    git://github.com/qualcomm-linux/display-driver.git;tag=v${PV};branch=main;protocol=https \
"

EXTRA_OEMAKE += "MACHINE='${MACHINE}'"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

do_install:append() {
    rm -rf ${D}${includedir}/${PN}
}
