SUMMARY = "Qualcomm boot requirements"
DESCRIPTION = "Storage and boot-critical kernel modules required for Qualcomm platforms"

inherit packagegroup

RRECOMMENDS:${PN} += " \
    kernel-module-gpucc-sa8775p \
    kernel-module-phy-qcom-snps-femto-v2 \
    kernel-module-ufs-qcom \
"
