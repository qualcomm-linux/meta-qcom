SUMMARY = "Qualcomm RTSS CAN userspace daemon"
DESCRIPTION = "Userspace daemon acting as a gateway between SocketCAN \
applications and RTSS using the RTSS mailbox UMD libraries."

HOMEPAGE = "https://github.com/qualcomm/rtss-can"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=223037c4be0bfc6cf757035432adf983"

DEPENDS = "glib-2.0 qcom-rtss-mailbox-umd linux-libc-headers can-utils"

SRC_URI = "git://github.com/qualcomm/rtss-can.git;branch=rtss-can.le.1.0;protocol=https;tag=v${PV}"
SRCREV  = "78ba2481657f17cbe0fdb396232e7d540322b9fb"

inherit cmake pkgconfig

EXTRA_OECMAKE += "-DSYSROOTINC_PATH=${STAGING_INCDIR} \
                  -DSYSROOT_INCLUDEDIR=${STAGING_INCDIR} \
                  -DSTAGING_LIBDIR=${STAGING_LIBDIR}"

# Number of active CAN controllers, keyed by MACHINE target.
# Injected into rtss_can.conf during install.
RTSS_CAN_ACTIVE_CONTROLLERS = "0"
RTSS_CAN_ACTIVE_CONTROLLERS:iq-9075-evk = "1"
RTSS_CAN_ACTIVE_CONTROLLERS:iq-8275-evk = "1"

do_compile:append() {
    if [ ! -f "${B}/src/rtss_can" ]; then
        bbfatal "rtss_can binary was not created"
    fi
}

do_install:append() {
    if [ ! -f "${D}${bindir}/rtss_can" ]; then
        bbfatal "rtss_can binary not found after CMake installation"
    fi

    if [ ! -f "${D}${sysconfdir}/rtss_can/rtss_can.conf" ]; then
        bbfatal "rtss_can.conf not found after CMake installation"
    fi

    sed -i "s/^active_controllers=.*/active_controllers=${RTSS_CAN_ACTIVE_CONTROLLERS}/" \
        "${D}${sysconfdir}/rtss_can/rtss_can.conf"

    bbnote "Set active_controllers=${RTSS_CAN_ACTIVE_CONTROLLERS} in rtss_can.conf for MACHINE=${MACHINE}"
}

RDEPENDS:${PN} = "can-utils qcom-rtss-mailbox-umd can-utils-access"
RDEPENDS:${PN} += "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)}"
RRECOMMENDS:${PN} = "kernel-module-can kernel-module-can-raw kernel-module-vcan"

FILES:${PN} += "${bindir}/rtss_can \
                ${sysconfdir}/rtss_can/rtss_can.conf"
