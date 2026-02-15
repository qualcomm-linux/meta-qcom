require conf/machine/include/soc-family.inc
require conf/machine/include/fit-dtb-compatible.inc
MACHINEOVERRIDES =. "qcom:"

# Set a default set of MACHINE_FEATURES, indivudual MACHINEs can append it
MACHINE_FEATURES = "alsa bluetooth usbgadget usbhost wifi"

XSERVER_OPENGL ?= " \
    xf86-video-modesetting \
    xserver-xorg-extension-glx \
"

XSERVER ?= " \
    xserver-xorg \
    xserver-xorg-module-libint10 \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', '${XSERVER_OPENGL}', 'xf86-video-fbdev', d)} \
    xf86-input-evdev \
"

PREFERRED_PROVIDER_virtual/kernel ??= "linux-yocto"

# Device Trees specific to linux-qcom family of kernels
LINUX_QCOM_KERNEL_DEVICETREE ??= ""
KERNEL_DEVICETREE:append:pn-linux-qcom = " ${LINUX_QCOM_KERNEL_DEVICETREE}"
KERNEL_DEVICETREE:append:pn-linux-qcom-next = " ${LINUX_QCOM_KERNEL_DEVICETREE}"
KERNEL_DEVICETREE:append:pn-linux-qcom-rt = " ${LINUX_QCOM_KERNEL_DEVICETREE}"
KERNEL_DEVICETREE:append:pn-linux-qcom-next-rt = " ${LINUX_QCOM_KERNEL_DEVICETREE}"

PREFERRED_PROVIDER_android-tools-conf = "android-tools-conf-configfs"

# Fastboot expects an ext4 image, which needs to be 4096 aligned
IMAGE_FSTYPES ?= "ext4.gz"
IMAGE_ROOTFS_ALIGNMENT ?= "4096"

# Android boot image settings
QCOM_BOOTIMG_KERNEL_BASE ?= "0x80000000"
QCOM_BOOTIMG_PAGE_SIZE ?= "4096"

# Default vfat sector size to 4096, compatible with UFS
QCOM_VFAT_SECTOR_SIZE ??= "4096"

# Default serial console for QCOM devices
SERIAL_CONSOLES ?= "115200;ttyMSM0"

# Increase INITRAMFS_MAXSIZE to 640 MiB to cover initramfs-kerneltest-full
# image.  All our boards (except db410c) have 2GiB and db410c has 1GiB of RAM,
# so this image would fit.
INITRAMFS_MAXSIZE = "655360"
# This initramfs is bigger than the others
INITRAMFS_MAXSIZE:pn-initramfs-kerneltest-full-image = "983040"

# Use systemd-boot as the EFI bootloader
EFI_PROVIDER = "systemd-boot"

# Unified Kernel Image (UKI) name
EFI_LINUX_IMG ?= "linux-${MACHINE}.efi"

# Pass additional options to mkimage command
# The -E option enables external data in FIT images, which is essential for
# modular boot setups, secure boot workflows, and reducing image size.
# The -B 8 option enforces 8-byte alignment, as per QCOM UEFI requirements.
FIT_DTB_MKIMAGE_EXTRA_OPTS ?= "-E -B 8"

# Default values (machines override these)
QCOM_RT_CPU           ?= ""
QCOM_IRQAFF           ?= ""
QCOM_RCU_NOCBS        ?= ""
QCOM_RCU_EXPEDITED    ?= ""
QCOM_CPUIDLE_OFF      ?= ""

# Per-arg conditionals
RT_ARGS_ISOL  = "${@oe.utils.conditional('QCOM_RT_CPU',        '', '', \
                'isolcpus=${QCOM_RT_CPU}',              d)}"
RT_ARGS_IRQ   = "${@oe.utils.conditional('QCOM_IRQAFF',        '', '', \
                'irqaffinity=${QCOM_IRQAFF}',           d)}"
RT_ARGS_NOCBS = "${@oe.utils.conditional('QCOM_RCU_NOCBS',     '', '', \
                'rcu_nocbs=${QCOM_RCU_NOCBS}',          d)}"
RT_ARGS_EXP   = "${@oe.utils.conditional('QCOM_RCU_EXPEDITED', '', '', \
                'rcupdate.rcu_expedited=${QCOM_RCU_EXPEDITED}', d)}"
RT_ARGS_IDLE  = "${@oe.utils.conditional('QCOM_CPUIDLE_OFF',   '', '', \
                'cpuidle.off=${QCOM_CPUIDLE_OFF}',      d)}"

# Combine
RT_KERNEL_CMDLINE = "${RT_ARGS_ISOL} ${RT_ARGS_IRQ} ${RT_ARGS_NOCBS} \
                     ${RT_ARGS_EXP} ${RT_ARGS_IDLE}"

# Append only when RT kernel is selected
KERNEL_CMDLINE_EXTRA:append = "${@bb.utils.contains_any('PREFERRED_PROVIDER_virtual/kernel',\
                              'linux-qcom-rt linux-qcom-next-rt', \
                              ' ${RT_KERNEL_CMDLINE}', '', d)}"
