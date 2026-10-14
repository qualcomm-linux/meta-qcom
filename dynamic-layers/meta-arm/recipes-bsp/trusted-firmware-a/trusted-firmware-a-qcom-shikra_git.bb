require trusted-firmware-a-qcom.inc

DEPENDS += "optee-os-qcom-shikra"

TFA_PLATFORM = "shikra_qli"
FIP_ELF_ADDR = "0x87980000"

# Fetch sources from Shikra specific branch
SRC_URI = "git://github.com/qualcomm-linux/trusted-firmware-a.git;protocol=https;name=tfa-shikra;branch=early/hwe/shikra"
SRCREV_tfa-shikra = "b3244f89d3697edbf2dea5a7429663f998449f58"
