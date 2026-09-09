SUMMARY = "Qualcomm RTSS CAN userspace daemon"
DESCRIPTION = "Userspace daemon acting as a gateway between SocketCAN \
applications and RTSS using the RTSS mailbox UMD libraries."

HOMEPAGE = "https://github.com/qualcomm/rtss-can"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=223037c4be0bfc6cf757035432adf983"

# Build-time dependencies.
DEPENDS = " \
    glib-2.0 \
    qcom-rtss-mailbox-umd \
    qcom-rtss-mailbox-dlkm \
"

SRC_URI = "git://github.com/qualcomm/rtss-can.git;branch=rtss-can.le.1.0;protocol=https"
SRCREV = "c7ba2c41bbca75699c44d1c9f25890b8efa0c9c1"

inherit cmake pkgconfig

# The RTSS mailbox UMD and DLKM recipes support these machines.
COMPATIBLE_MACHINE = "qcs9100-ride-sx|qcs8300-ride-sx|iq-9075-evk|iq-8275-evk"

# Runtime commands, userspace libraries, and kernel modules.
RDEPENDS:${PN} = " \
    iproute2 \
    kmod \
    qcom-rtss-mailbox-umd \
    can-utils-access \
    kernel-module-can \
    kernel-module-can-raw \
    kernel-module-can-gw \
    kernel-module-vcan \
    kernel-module-rtss-mailbox \
"

# The daemon is installed in this phase but is not automatically started.
# No systemd service is provided by this change.