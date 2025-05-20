SOC_FAMILY = "qcs404"
require conf/machine/include/qcom-common.inc
require conf/machine/include/arm/arch-armv8a.inc

MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS += " \
    qrtr \
"
