DESCRIPTION = "Tiny ramdisk image for board bringup"

PACKAGE_INSTALL = " \
    ${ROOTFS_BOOTSTRAP_INSTALL} \
    busybox \
    base-passwd \
    packagegroup-core-boot \
    udev \
"

PACKAGE_INSTALL += " \
    debugcc \
    fastrpc \
    pd-mapper \
    qrtr \
    rmtfs \
    tqftpserv \
"

# Do not pollute the initrd image with rootfs features
IMAGE_FEATURES = "allow-empty-password empty-root-password allow-root-login post-install-logging"
IMAGE_LINGUAS = ""

LICENSE = "MIT"

IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
IMAGE_NAME_SUFFIX ?= ""
inherit core-image

IMAGE_ROOTFS_SIZE = "8192"
IMAGE_ROOTFS_EXTRA_SPACE = "0"

# Disable installation of kernel and modules via packagegroup-core-boot
NO_RECOMMENDATIONS ?= "1"

# Enable local auto-login (on systemd) of the root user (local = serial port and
# virtual console by default, can be configured).
LOCAL_GETTY ?= " \
    ${IMAGE_ROOTFS}${systemd_system_unitdir}/serial-getty@.service \
    ${IMAGE_ROOTFS}${systemd_system_unitdir}/getty@.service \
"
local_autologin () {
    sed -i -e 's/^\(ExecStart *=.*getty \)/\1--autologin root /' ${LOCAL_GETTY}
}
ROOTFS_POSTPROCESS_COMMAND += "${@oe.utils.conditional('VIRTUAL-RUNTIME_init_manager', 'systemd', 'local_autologin;', '', d)}"

# We'd like to include extra packages provided by layers which we do not depend
# on. This can be handled by .bbappends, but then image recipes including this
# one would not get all these tools. So simulate dynamic bbappend here.
#
# To use it define PACKAGE_INSTALL_foo-layer variable containing the list of
# packages to be installed if (and only if) layer foo-layer is enabled.
python() {
    for layer in d.getVar("BBFILE_COLLECTIONS", True).split():
        extra = d.getVar("PACKAGE_INSTALL_%s" % layer)
        if extra:
            d.appendVar("PACKAGE_INSTALL", " " + extra)
}
