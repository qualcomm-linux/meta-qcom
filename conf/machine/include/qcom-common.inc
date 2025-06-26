SOC_FAMILY:prepend = "qcom:"
require conf/machine/include/soc-family.inc

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

PREFERRED_VERSION_linux-yocto ?= "6.12%"

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

# Increase INITRAMFS_MAXSIZE to 384 MiB to cover initramfs-kerneltest-full
# image.  All our boards (except db410c) have 2GiB and db410c has 1GiB of RAM,
# so this image would fit.
INITRAMFS_MAXSIZE = "393216"

# Use systemd-boot as the EFI bootloader
EFI_PROVIDER = "systemd-boot"

# Install packages at root of ESP
EFI_PREFIX = ""

# Location of dtb inside ESP
EFI_DTB_DIR ?= "${EFI_PREFIX}dtb"

# Unified Kernel Image (UKI) name
EFI_LINUX_IMG ?= "linux-${MACHINE}.efi"

# dtb file to pack into UKI.
# empty value would skip packing dtb.
EFI_LINUX_IMG_DTB ?= ""

# Place dtb at EFIDTDIR to seamlessly package
KERNEL_DTBDEST = "${EFI_DTB_DIR}"
