HOMEPAGE = "https://github.com/qualcomm-linux/rtss-mailbox-kmd"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e"

SRC_URI = "git://github.com/qualcomm-linux/rtss-mailbox-kmd.git;branch=rtss-mailbox-kernel.le.0.0;protocol=https;tag=v${PV}"
SRCREV = "9ce7c2f1ad860beb86e922e62b14fdcd178e129f"

COMPATIBLE_MACHINE = "qcs9100-ride-sx|qcs8300-ride-sx|iq-9075-evk|iq-8275-evk"
