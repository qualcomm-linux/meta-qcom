SOC_FAMILY = "apq8016"
require conf/machine/include/qcom-common.inc
require conf/machine/include/arm/armv8a/tune-cortexa53.inc

MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS += " \
    qrtr \
    rmtfs \
"

MACHINE_EXTRA_RRECOMMENDS += " \
    fastrpc \
"

# Android boot image settings
QCOM_BOOTIMG_PAGE_SIZE = "2048"
