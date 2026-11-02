SOC_FAMILY = "sdx75"
require conf/machine/include/qcom-common.inc

# SDX75 has no PCIe controller
MACHINE_FEATURES:remove = "pci"

PREFERRED_PROVIDER_virtual/kernel ?= "linux-qcom-next"

DEFAULTTUNE = "cortexa55"
require conf/machine/include/arm/armv8-2a/tune-cortexa55.inc

KERNEL_IMAGETYPE = "Image"

MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS += " \
    qrtr \
    rmtfs \
    tqftpserv \
    pd-mapper \
"

# UBI filesystem settings
IMAGE_FSTYPES = "ubi"
QCOM_BOOTIMG_PAGE_SIZE ?= "4096"
