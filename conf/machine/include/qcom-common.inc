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
# The -B 8 option enforces 8-byte alignment, ensuring compatibility with boot
# ROM requirements, improving memory access efficiency, and supporting
# predictable offsets for multi-component FIT images.
FIT_DTB_MKIMAGE_EXTRA_OPTS = "-E -B 8"
