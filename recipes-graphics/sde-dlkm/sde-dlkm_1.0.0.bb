DESCRIPTION = "Qualcomm SDE (Snapdragon Display Engine) display driver"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=223037c4be0bfc6cf757035432adf983"

inherit module

SRCREV = "70ef6d41350020df80c406fcc30db4d63e9d776b"
SRC_URI = " \
    git://github.com/qualcomm-linux/display-driver.git;tag=v${PV};branch=main;protocol=https \
"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"
