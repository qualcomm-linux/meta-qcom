SOC_FAMILY = "apq8064"
require conf/machine/include/qcom-common.inc
DEFAULTTUNE = "cortexa15thf-neon-vfpv4"
require conf/machine/include/arm/armv7a/tune-cortexa15.inc

# Android boot image settings
QCOM_BOOTIMG_PAGE_SIZE = "2048"
