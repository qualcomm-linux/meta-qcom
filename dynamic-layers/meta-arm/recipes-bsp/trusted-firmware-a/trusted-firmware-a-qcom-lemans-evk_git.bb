require trusted-firmware-a-qcom.inc

DEPENDS += "optee-os-qcom-lemans"

TFA_PLATFORM = "lemans_evk"
FIP_ELF_ADDR = "0xaf000000"
