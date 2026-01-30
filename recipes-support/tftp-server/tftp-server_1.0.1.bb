SUMMARY = "Prebuilt Qualcomm tftp-server application"
DESCRIPTION = "Prebuilt Qualcomm proprietary application for handling Remote File Systems (RFS) Requests"

inherit systemd

LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://usr/share/doc/tftp-server/NO.LOGIN.BINARY.LICENSE.QTI.pdf;md5=7a5da794b857d786888bbf2b7b7529c8"

SRC_URI = "https://softwarecenter.qualcomm.com/nexus/generic/software/chip/component/core-technologies.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/tftp-server_15.0+really${PV}_armv8a.tar.gz"

PBT_BUILD_DATE = "260127"
SRC_URI[sha256sum] = "6dffae734607ce7bbde40a839b60960558df45796ed66dfd78a74ddf912a8cbd"

S = "${UNPACKDIR}"

DEPENDS += "glib-2.0 virtual/kernel qmi-framework libcap"
RPROVIDES:${PN} += "virtual-tftp-server"
RCONFLICTS:${PN} += "tqftpserv"

FILES:${PN} += "/lib/systemd/*"
FILES:${PN} += "/etc/tmpfiles.d/*"

SYSTEMD_SERVICE:${PN} += "tftp_server.service"

do_install() {
	# Install all files from the prebuilt tarball
	cp -r ${S}/* ${D}/
}

do_install:append() {
   # Ensure systemd service is properly installed
   if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
       if [ -f ${D}/usr/lib/systemd/system/tftp_server.service ]; then
           install -d ${D}${systemd_system_unitdir}
           if [ ! -f ${D}${systemd_system_unitdir}/tftp_server.service ]; then
               cp ${D}/usr/lib/systemd/system/tftp_server.service ${D}${systemd_system_unitdir}/
           fi
       fi
   fi
}
