SUMMARY = "DIAG implements routing of diagnostics related messages between host and various subsystems."
HOMEPAGE = "https://github.com/linux-msm/diag"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f6832ae4af693c6f31ffd931e25ef580"

SRC_URI = "git://github.com/linux-msm/${BPN}.git;branch=master;protocol=https"

PV = "0.0+git"
SRCREV = "d06e599d197790c9e84ac41a51bf124a69768c4f"

DEPENDS = "qrtr udev"

do_compile () {
	oe_runmake
}

do_install () {
	oe_runmake install 'DESTDIR=${D}'
}
