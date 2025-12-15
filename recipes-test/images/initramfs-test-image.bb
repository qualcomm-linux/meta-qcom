require recipes-test/images/initramfs-tiny-image.bb

DESCRIPTION = "Small ramdisk image for running tests (bootrr, etc)"

IMAGE_FEATURES += "enable-adbd"
inherit image-adbd

PACKAGE_INSTALL += " \
    alsa-utils-alsaucm \
    alsa-utils-amixer \
    alsa-utils-aplay \
    alsa-utils-speakertest \
    bluez5 \
    bootrr \
    dhcpcd \
    virtual-diag-router \
    dropbear \
    e2fsprogs \
    e2fsprogs-e2fsck \
    e2fsprogs-mke2fs \
    e2fsprogs-resize2fs \
    e2fsprogs-tune2fs \
    ethtool \
    gptfdisk \
    i2c-tools \
    iw \
    libdrm-tests \
    lrzsz \
    mybw \
    pciutils \
    strace \
    usbutils \
    util-linux-lscpu \
    util-linux-taskset \
    wpa-supplicant \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opencl', 'clinfo', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'kmscube', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'vulkan', 'vulkan-tools', '', d)} \
"

# We'd like to include extra packages provided by layers which we do not depend
# on. This can be handled by .bbappends, but then image recipes including this
# one would not get all these tools. So simulate dynamic bbappend here.
PACKAGE_INSTALL_openembedded-layer += " \
    cpufrequtils \
    cryptsetup \
    devmem2 \
    lmsensors-config-libsensors \
    lmsensors-sensors \
    media-ctl \
    read-edid \
    v4l-utils \
    yavta \
"

PACKAGE_INSTALL_networking-layer += " \
    iperf2 \
    iperf3 \
    phytool \
    tcpdump \
"
