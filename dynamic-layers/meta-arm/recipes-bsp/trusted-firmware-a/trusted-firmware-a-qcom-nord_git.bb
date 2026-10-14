require trusted-firmware-a-qcom.inc

DEPENDS += "optee-os-qcom-nord"

TFA_PLATFORM = "nord"
FIP_ELF_ADDR = "0xaf000000"
