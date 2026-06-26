require recipes-test/images/initramfs-test-image.bb

DESCRIPTION = "Relatively larger ramdisk image for running tests (bootrr, etc)"

PACKAGE_INSTALL += " \
    bootrr \
    coreutils \
    expect \
    hdparm \
    igt-gpu-tools-tests \
    kexec \
    lsof \
    ncurses \
    ncurses-terminfo \
    ncurses-terminfo-base \
    net-tools \
    openssh-sftp-server \
    pulseaudio-misc \
    rng-tools \
    stress-ng \
    util-linux \
    util-linux-chrt \
    util-linux-lsblk \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'weston-examples', '', d)} \
"

PACKAGE_INSTALL:append:aarch64 = " \
    fastrpc-tests \
"

PACKAGE_INSTALL:append:libc-glibc = " \
    rt-tests \
"

# We'd like to include extra packages provided by layers which we do not depend
# on. This can be handled by .bbappends, but then image recipes including this
# one would not get all these tools. So simulate dynamic bbappend here.

# ncurses-terminfo is provided by oe-core layer, but it's only needed for gps (cgps), so include it here
PACKAGE_INSTALL_openembedded-layer += " \
    ${@['crash', '']['${TCLIBC}' == 'musl']} \
    dhrystone \
    gpsd \
    gpsd-machine-conf \
    gps-utils \
    iozone3 \
    libgpiod \
    libgpiod-tools \
    lmbench \
    makedumpfile \
    mbw \
    ncurses-terminfo-base \
    sysbench \
    tinymembench \
    tiobench \
    whetstone \
"

PACKAGE_INSTALL_multimedia-layer += " \
    pipewire-tools \
"
