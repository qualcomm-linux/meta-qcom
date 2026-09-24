# Configurations and variables for glymur SoC family.

SOC_FAMILY = "glymur"
require conf/machine/include/qcom-base.inc
require conf/machine/include/qcom-common.inc

DEFAULTTUNE = "armv8-6a-crypto"
require conf/machine/include/arm/arch-armv8-6a.inc

QCOM_HEXAGON_ARCHS = "v81"

# The secure TPM device is not yet available on Glymur, so avoid
# systemd's boot-time wait for /dev/tpm0.
KERNEL_CMDLINE_EXTRA:append = " systemd.tpm2_wait=false"

MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS += " \
    packagegroup-qcom-boot-essential \
    packagegroup-machine-essential-qcom-glymur-soc \
"

MACHINE_EXTRA_RRECOMMENDS += " \
    packagegroup-qcom-boot-additional \
"
