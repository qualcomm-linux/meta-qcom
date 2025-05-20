SOC_FAMILY = "sdx55"
require conf/machine/include/qcom-common.inc
require conf/machine/include/arm/armv7a/tune-cortexa7.inc

MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS += " \
    qrtr \
    rmtfs \
"

# UBI filesystem settings
IMAGE_FSTYPES ?= "ubi"
QCOM_BOOTIMG_PAGE_SIZE ?= "4096"
