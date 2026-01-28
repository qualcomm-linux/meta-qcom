SUMMARY = "Prebuilt Qualcomm tftp-server application"
DESCRIPTION = "Prebuilt Qualcomm proprietary application for handling Remote File Systems (RFS) Requests"

inherit systemd

LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://usr/share/doc/tftp-server/NO.LOGIN.BINARY.LICENSE.QTI.pdf;md5=7a5da794b857d786888bbf2b7b7529c8"

SRC_URI = "https://softwarecenter.qualcomm.com/nexus/generic/software/chip/component/core-technologies.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/tftp-server_15.0+really${PV}_armv8a.tar.gz"

PBT_BUILD_DATE = "260127"
SRC_URI[sha256sum] = "6dffae734607ce7bbde40a839b60960558df45796ed66dfd78a74ddf912a8cbd"

S = "${UNPACKDIR}"

DEPENDS += "glib-2.0 qmi-framework libcap"
RPROVIDES:${PN} += "virtual-tftp-server"
RCONFLICTS:${PN} += "tqftpserv"

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

FILES:${PN} += "${nonarch_libdir}/tmpfiles.d/*"

SYSTEMD_SERVICE:${PN} += "tftp_server.service"

do_install() {
	# Install all files from the prebuilt tarball
	cp -r ${S}/* ${D}/

	# Move tmpfiles.d to correct location
	if [ -f ${D}/etc/tmpfiles.d/tftp-server.conf ]; then
		install -d ${D}${nonarch_libdir}/tmpfiles.d
		cp ${D}/etc/tmpfiles.d/tftp-server.conf ${D}${nonarch_libdir}/tmpfiles.d/
		rm ${D}/etc/tmpfiles.d/tftp-server.conf
	fi
}

pkg_postinst:${PN} () {
	if [ -n "$D" ]; then
	# During image creation, don't run systemd-tmpfiles
		exit 0
	fi

        # On target device, create tmpfiles immediately
	if command -v systemd-tmpfiles >/dev/null 2>&1; then
		systemd-tmpfiles --create ${nonarch_libdir}/tmpfiles.d/tftp-server.conf 2>/dev/null || true
	fi
}
