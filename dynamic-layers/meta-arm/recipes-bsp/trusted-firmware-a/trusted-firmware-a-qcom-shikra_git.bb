require trusted-firmware-a-qcom.inc

DEPENDS += "optee-os-qcom-shikra"

TFA_PLATFORM = "shikra_evk"
FIP_ELF_ADDR = "0x87980000"
