
# Enable selinux support in the kernel if the feature is enabled
require recipes-kernel/linux/linux-yocto_selinux.inc

KERNEL_CONFIG_FRAGMENTS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', '${UNPACKDIR}/selinux.cfg', '', d)}"
