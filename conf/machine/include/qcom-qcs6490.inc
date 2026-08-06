# Configurations and variables for QCS6490 SoC family.

SOC_FAMILY = "qcm6490"
require conf/machine/include/qcom-base.inc
require conf/machine/include/qcom-common.inc

DEFAULTTUNE = "armv8-2a-crypto"
require conf/machine/include/arm/arch-armv8-2a.inc

MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS += " \
    packagegroup-qcom-boot-essential \
    packagegroup-machine-essential-qcom-qcs6490-soc \
"

MACHINE_EXTRA_RRECOMMENDS += " \
    packagegroup-qcom-boot-additional \
"

# Post-DDR DTB embedded inside xbl_config.elf for QCM6490 (Kodiak).
# Used by qcom-capsule.bbclass to locate the DTB into which the OEM
# capsule root certificate is injected at build time.
XBLCONFIG_DTB ?= "post-ddr-kodiak-1.0.dtb"
